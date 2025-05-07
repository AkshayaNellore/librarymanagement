import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { UsersService } from '../../service/users.service';
import { Router, RouterLink } from '@angular/router';
 
@Component({
  selector: 'app-signup',
  imports: [FormsModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  user = {
    name: '',
    username: '',
    email: '',
    phone: '',
    address: '',
    membershipStatus: null,
    userType: '',
    password: ''
  };
  result: any;
  errorMessage: string = '';
 
  constructor(private usersService: UsersService, private router: Router, private location: Location) {}
 
  onSubmit(signupForm: NgForm) { // Ensure you import NgForm
    if (signupForm.invalid) {
      // Mark all controls as touched to trigger immediate error display
      Object.keys(signupForm.controls).forEach(key => {
        const control = signupForm.controls[key];
        control.markAsTouched();
      });
      this.errorMessage = 'Please correct the validation errors below.';
      console.log('Form is invalid. Displaying errors.');
      return; // Stop further execution if the form is invalid
    }
 
    this.usersService.signup(this.user).subscribe(response => {
      this.result = response;
      if (this.result.success) {
        console.log('signup successful!');
        // Optionally navigate the user
        this.router.navigate(['/']);
      } else {
        this.errorMessage = this.result.message;
      }
    }, error => {
      if (error.status === 400) {
        this.result = error.error;
        this.errorMessage = this.result.message;
        console.log(this.errorMessage);
      } else {
        console.error('Error', error);
      }
    });
  }
}
 