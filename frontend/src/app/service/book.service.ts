import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private apiUrl = 'http://localhost:8083/api/books';

  constructor(private http: HttpClient) { }

  addBook(book: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, book);
  }

  getBooks(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/view-all`);
  }

  deleteBook(bookId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${bookId}`);
  }

  getBookById(bookId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/view/${bookId}`);
  }
}