import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
     private baseUrl = 'http://localhost:8080/api/categories'; // Spring Boot backend

  constructor(private http: HttpClient) {}

  getMainCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/main`);
  }

  getSubCategories(mainCategory: string): Observable<{ id: number; name: string; type: string }[]> {
    return this.http.get<any[]>(`${this.baseUrl}/sub/${mainCategory}`);
  }

  getAllCategories(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }
}
