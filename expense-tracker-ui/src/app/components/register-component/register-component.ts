import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-component',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './register-component.html',
  styleUrls: ['./register-component.css'],
   standalone: true
})
export class RegisterComponent implements OnInit {
 registerForm!: FormGroup;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: (res) => {
          this.successMessage = 'User registered successfully!';
          this.errorMessage = '';
          window.alert('Registration successful! You can now log in.');
          this.registerForm.reset(); // Reset the form after successful registration
          this.router.navigate(['/login']); // redirect to login page
        },
        error: (err) => {
          this.errorMessage = 'Registration failed!';
          this.successMessage = '';
          console.error(err);
        }
      });
    }
  }
}
