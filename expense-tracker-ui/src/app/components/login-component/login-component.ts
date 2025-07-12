import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { MatModule } from '../../mat.module';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login-component',
  imports: [MatModule, CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})
export class LoginComponent {
form: FormGroup;

constructor(
  private fb: FormBuilder,
  private auth: AuthService,
  private router: Router,
  private userService: UserService
  ) {
  this.form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
}

onLogin(): void {
  const { username, password } = this.form.value;
  this.auth.login(username, password).subscribe({
    next: (user) => {
          
      localStorage.setItem('token', user.token);
      localStorage.setItem('accessToken', user.token);
      localStorage.setItem('refreshToken', user.refreshToken);
      localStorage.setItem('username', username);
      this.userService.setUserDetails(user);
      this.router.navigate(['/dashboard']);
    },
    error: () => alert('Login failed')
  });
}
}
