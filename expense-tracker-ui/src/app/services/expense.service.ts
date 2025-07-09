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

  update(id: number, expense: Expense): Observable<Expense> {
    return this.http.put<Expense>(`${this.baseUrl}/${id}`, expense);
  }

  getById(id: number) {
  return this.http.get<Expense>(`${this.baseUrl}/${id}`);
  }
  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
