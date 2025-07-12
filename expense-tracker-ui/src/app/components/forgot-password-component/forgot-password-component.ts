import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatModule } from '../../mat.module';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password-component',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatModule],
  templateUrl: './forgot-password-component.html',
  styleUrl: './forgot-password-component.css'
})
export class ForgotPasswordComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.authService.forgotPassword(this.form.value.email).subscribe({
      next: () => {
        this.submitted = true;
        this.error = null;
          if (window.confirm('Password reset link sent to your email. Click OK to go to the login page.')) {
            this.form.reset();
            this.router.navigate(['/login']);
          } else {
            this.form.reset();
          }
      }
    });
  }
}
