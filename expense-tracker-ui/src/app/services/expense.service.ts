import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Expense } from '../models/expense.model';

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  private baseUrl = 'http://localhost:8080/api/expenses'; // Spring Boot backend

  constructor(private http: HttpClient) {}

  getAll(): Observable<Expense[]> {
    return this.http.get<Expense[]>(this.baseUrl);
  }

  create(expense: Expense): Observable<Expense> {
    return this.http.post<Expense>(this.baseUrl, expense);
  }

  update(expenseId: number, expense: Expense): Observable<Expense> {
    const username = localStorage.getItem('username');
    console.log("/expenses/edit",expense);
    
    return this.http.put<Expense>(`${this.baseUrl}/edit/${username}/${expenseId}`, expense);
  }

  getByUsernameAndId(username: string, id: number) {
  return this.http.get<Expense>(`${this.baseUrl}/${username}/${id}`);
  }
  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
