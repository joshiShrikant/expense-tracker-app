import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

   register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    location.href = '/login';
    }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
    // this.http.get(`${this.apiUrl}/isLoggedIn`).subscribe({
    //   next: (res) => {
    //     return res as boolean;
    //   }
    // });
    // return false; // Placeholder, should be replaced with actual logic
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserDetails(): Observable<any> {
    const token = this.getToken();
    const username = localStorage.getItem('username');
    
    if (token) {
      return this.http.get(`${this.apiUrl}/user/${username}`);
    } else {
      return new Observable(observer => {
        observer.error('No token found');
      });
    }
  }

    refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post<any>('/api/auth/refresh-token', { refreshToken });
  }

}
