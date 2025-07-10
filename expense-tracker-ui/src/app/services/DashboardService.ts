// src/app/services/dashboard.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Expense } from '../models/expense.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = 'http://localhost:8080/api/expenses';

  constructor(private http: HttpClient) {}

  // getDashboardData(): Observable<any> {
  //   return this.http.get<any>(`${this.apiUrl}/dashboard`);
  // }

  getExpenseByUserName(username: string): Observable<Expense[]> {
   return this.http.get<Expense[]>(`${this.apiUrl}/${username}`);
  }
}
