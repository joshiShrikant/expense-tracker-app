import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { MatModule } from '../../mat.module';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-component',
  imports: [MatModule, CommonModule, ReactiveFormsModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})
export class LoginComponent {
form: FormGroup;

constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
  this.form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
}

onLogin(): void {
  const { username, password } = this.form.value;
  this.auth.login(username, password).subscribe({
    next: (res) => {
      localStorage.setItem('token', res.token);
      localStorage.setItem('accessToken', res.token);
      localStorage.setItem('refreshToken', res.refreshToken);
      this.router.navigate(['/dashboard']);
    },
    error: () => alert('Login failed')
  });
}
}
