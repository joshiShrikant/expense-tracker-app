import { HttpClientModule } from '@angular/common/http';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { MatModule } from './mat.module';
import { AuthService } from './services/auth.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  imports: [RouterModule,RouterOutlet,CommonModule, MatModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
   isDarkMode = false;
  protected title = 'expense-tracker-ui';
  isLoggedIn = false;
  userDetails : any = {};

  
constructor(
  private auth: AuthService,
  private router: Router,
  private cdRef: ChangeDetectorRef,
  private userService: UserService
) {
}
  getUserDetails() {
     this.userService.userDetails$.subscribe((data) => {
      this.userDetails = data?.user;
      console.log('Updated user:', data?.user, this.userDetails.username);
      this.isLoggedIn = !!this.userDetails.username;
      this.cdRef.detectChanges(); // Ensure the view is updated with the new user details
    });
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
    console.log('App component initialized');
    
    this.getUserDetails();
    this.setInitialTheme();
    this.isUserLoggedIn()
  }

  logout() {
  this.auth.logout();
  this.router.navigate(['/login']);
}
}
