import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { MatModule } from './mat.module';
import { AuthService } from './services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterModule,RouterOutlet,CommonModule, MatModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
   isDarkMode = false;
  protected title = 'expense-tracker-ui';
  isLoggedIn = false;
  userDetails : any = {};

  
constructor(private auth: AuthService, private router: Router) {
}

  getUserDetails() {
    const token = localStorage.getItem('accessToken');
    if (token) {
      this.auth.getUserDetails().subscribe({
        next: (res) => {
          this.userDetails = res;
        },
        error: (err) => {
          console.error('Error fetching user details:', err);
        }
      });
    }
  }

    toggleTheme(): void {
    this.isDarkMode = !this.isDarkMode;
    const theme = this.isDarkMode ? 'dark-theme' : 'light-theme';
    document.body.classList.remove('dark-theme', 'light-theme');
    document.body.classList.add(theme);
    localStorage.setItem('theme', theme);
  }

   private setTheme(theme: string): void {
    document.body.classList.remove('dark-theme', 'light-theme');
    document.body.classList.add(theme);
  }
  setInitialTheme(): void {
    const savedTheme = localStorage.getItem('theme') || 'light-theme';
    this.isDarkMode = savedTheme === 'dark-theme';
    this.setTheme(savedTheme);
  }
  isUserLoggedIn() {
    this.isLoggedIn = this.auth.isLoggedIn();
  }

  ngOnInit() {

    this.getUserDetails();
    this.setInitialTheme();
    this.isUserLoggedIn()
  }

  logout() {
  this.auth.logout();
  this.router.navigate(['/login']);
}

}
