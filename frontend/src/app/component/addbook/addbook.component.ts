import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BookService } from '../../service/book.service';

@Component({
  selector: 'app-addbook',
  imports: [FormsModule, CommonModule],
  templateUrl: './addbook.component.html',
  styleUrls: ['./addbook.component.css']
})
export class AddBookComponent implements OnInit {

  book = {
    bookId: '',
    title: '',
    author: '',
    genre: '',
    isbn: '',
    yearPublished: null,
    availableCopies: null
  };
  showModal: boolean = false;
  books: any[] = [];
  responseMessage: string | null = null;
  errorMessage: string | null = null;
  showPopup: boolean = false;
  addButtonDisabled: boolean = false;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.fetchBooks();
  }

  fetchBooks() {
    this.bookService.getBooks().subscribe(
      (data) => {
        this.books = data;
        console.log(this.books);
      },
      (error) => {
        console.error('Error fetching books:', error);
        this.errorMessage = 'Error fetching books: ' + error.message;
      }
    );
  }

  showAddBookPopup() {
    this.showPopup = true;
    this.addButtonDisabled = true;
  }

  closePopup() {
    this.showPopup = false;
    this.addButtonDisabled = false;
    this.resetForm();
  }

  onSubmit() {
    this.bookService.addBook(this.book).subscribe(
      (response) => {
        this.responseMessage = 'Book added successfully';
        this.errorMessage = null;
        this.closePopup();
        this.fetchBooks();
        this.showModal = true; // Show success modal after adding
      },
      (error) => {
        this.errorMessage = 'Error adding book: ' + error.message;
        this.responseMessage = null;
      }
    );
  }

  closeModal() {
    this.showModal = false; // Ensure this method sets showModal to false
  }

  resetForm() {
    this.book = {
      bookId: '',
      title: '',
      author: '',
      genre: '',
      isbn: '',
      yearPublished: null,
      availableCopies: null
    };
  }

  confirmDelete(book: any): void {
    if (confirm(`Do you want to delete ${book.title}?`)) {
      this.deleteBook(book.bookId);
    }
  }

  deleteBook(bookId: string): void {
    this.bookService.deleteBook(bookId).subscribe(
      (response) => {
        this.responseMessage = 'Book deleted successfully';
        this.errorMessage = null;
        this.fetchBooks(); // Refresh the book list after deletion
      },
      (error) => {
        if (error.status === 400 && error.error && error.error.message && error.error.message.includes('as it is currently borrowed.')) {
          alert(`Unable to delete the book as it is currently borrowed by users.`);
        } else {
          //alert(`Unable to delete the book as it is currently borrowed by users.`);
          this.errorMessage = 'Error deleting book: as it is currently borrowed by the users!';
          this.responseMessage = null;
        }
      }
    );
  }
}
