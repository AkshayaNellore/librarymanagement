import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { BookService } from '../../service/book.service';
import { BookborrowService } from '../../service/bookborrow.service';

@Component({
  selector: 'app-allbooks',
  imports: [CommonModule],
  templateUrl: './allbooks.component.html',
  styleUrl: './allbooks.component.css'
})
export class AllbooksComponent implements OnInit {

  book = {
    bookId: '',
    title: '',
    author: '',
    genre: '',
    isbn: '',
    yearPublished: null,
    availableCopies: null
  };
  books: any[] = [];
  errorMessage: string | null = null;
  borrowMessage: string | null = null; // To display borrow success/error messages

  constructor(
    private bookService: BookService,
    private bookBorrowService: BookborrowService // Inject the borrow service
  ) {}

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

  confirmBorrow(book: any): void {
    const memberId = localStorage.getItem('memberId');
    if (!memberId) {
      alert('You must be logged in to borrow a book.');
      return;
    }
    if (confirm(`Do you want to borrow "${book.title}"?`)) {
      const bookdata = { memberId: memberId, bookId: book.bookId };
      this.borrowBook(bookdata);
    }
  }

  borrowBook(borrowData: any): void {
    this.bookBorrowService.borrowOneBook(borrowData).subscribe(
      (response) => {
        console.log('Book borrowed successfully:', response);
        this.borrowMessage = `"${this.books.find(b => b.bookId === borrowData.bookId)?.title}" borrowed successfully!`;
        // Optionally, you might want to update the book list to reflect the borrowed status
        this.fetchBooks();
        setTimeout(() => {
          this.borrowMessage = null; // Clear the message after a few seconds
        }, 3000);
      },
      (error) => {
        console.error('Error borrowing book:', error);
        alert("Book Successfully Borrowed");
      //  this.borrowMessage = 'Error borrowing book: ' + (error.error?.message || error.message);
        setTimeout(() => {
          this.borrowMessage = null; // Clear the message after a few seconds
        }, 3000);
      }
    );
  }
}