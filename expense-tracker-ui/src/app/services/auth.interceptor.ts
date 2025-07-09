// auth.interceptor.ts
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { catchError, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

 const authService = inject(AuthService);


  // Do not add token for login or register
  const isAuthUrl = req.url.includes('/api/auth/login') || req.url.includes('/api/auth/register') || req.url.includes('/api/auth/refresh-token');
  if (isAuthUrl) return next(req);

const clonedReq = token
  ? req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
      withCredentials: true
    })
  : req.clone({ withCredentials: true }); // add even if no token

  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Token expired, attempt refresh
        return authService.refreshToken().pipe(
          switchMap((res: any) => {
            localStorage.setItem('token', res.token);
            localStorage.setItem('accessToken', res.accessToken);
            localStorage.setItem('refreshToken', res.refreshToken);
            const retryReq = req.clone({
              setHeaders: {
                Authorization: `Bearer ${res.accessToken}`,
              },
              withCredentials: true
            });
            return next(retryReq); // Retry original request
          }),
          catchError(refreshError => {
            authService.logout();
            return throwError(() => refreshError);
          })
        );
      }
      return throwError(() => error);
    })
  );
};
