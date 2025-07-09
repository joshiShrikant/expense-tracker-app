import { Routes } from '@angular/router';
import { AuthGuard } from './services/auth.guard';
import { LoginComponent } from './components/login-component/login-component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'expenses',
    pathMatch: 'full'
  },
{
  path: 'register',
  loadComponent: () => import('./components/register-component/register-component').then(m => m.RegisterComponent)
},
{ path: 'login', component: LoginComponent },
  {
    path: 'expenses',
     canActivate: [AuthGuard],
    loadComponent: () =>
      import('./components/expense-list-component/expense-list-component').then(m => m.ExpenseListComponent)
  },
  {
    path: 'expenses/add',
     canActivate: [AuthGuard],
    loadComponent: () =>
      import('./components/expense-form-component/expense-form-component').then(m => m.ExpenseFormComponent)
  },
  {
    path: 'expenses/edit/:id',
     canActivate: [AuthGuard],
    loadComponent: () =>
      import('./components/expense-form-component/expense-form-component').then(m => m.ExpenseFormComponent)
  },
  {
  path: 'dashboard',
   canActivate: [AuthGuard],
  loadComponent: () =>
    import('./components/dashboard-component/dashboard-component').then(m => m.DashboardComponent)
},
  {
    path: '**',
    redirectTo: 'expenses'
  }
];
