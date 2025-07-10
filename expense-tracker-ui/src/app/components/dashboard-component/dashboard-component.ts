import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/DashboardService';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { MatModule } from '../../mat.module';

@Component({
  selector: 'app-dashboard-component',
  standalone: true,
  imports: [CommonModule, MatModule],
  templateUrl: './dashboard-component.html',
  styleUrls: ['./dashboard-component.css']
})
export class DashboardComponent implements OnInit {
  totalExpenses = 0;
  categoryTotals: { [key: string]: number } = {};
  categoryKeys: string[] = [];
   loading = true;
  expensesList: any[] = [];
  monthlyExpense = 0;
  userId: number | null = null;
  UserDetail: any = null;
  topThreeCategories: string = '';

  constructor(private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.getUserDetails().subscribe({
      next: (data) => {
        this.UserDetail = data;
      }
    });

    const username = localStorage.getItem('username');
    if (username !== null) {
      this.loadDashboardByUserName(username);
    }
  }

  calculateTotals(data: any[]): void {
    this.totalExpenses = data.reduce((sum, item) => sum + item.expenseAmount, 0);
    this.categoryTotals = data.reduce((acc: any, item: any) => {
      acc[item.mainCategory] = (acc[item.mainCategory] || 0) + item.expenseAmount;
      return acc;
    }, {});
    this.categoryKeys = Object.keys(this.categoryTotals);
    this.topThreeCategories = this.categoryKeys.sort((a, b) => this.categoryTotals[b] - this.categoryTotals[a]).slice(0, 3).join(', ');
  }

  calculateMonthlyExpense(data: any[]): void {
    this.monthlyExpense = data.reduce((sum: any, item: any) => {
      const date = new Date(item.expenseDate);
      const currentMonth = new Date().getMonth();
      if (date.getMonth() === currentMonth) {
        return sum + item.expenseAmount;
      }
      return sum;
    }, 0);
  }

  // Method to load dashboard data by username
  loadDashboardByUserName(username: string): void {
    console.log('Loading dashboard for user:', username);

    this.dashboardService.getExpenseByUserName(username).subscribe({
      next: (data:any) => {
        this.expensesList = data;
        this.calculateTotals(data);
        this.calculateMonthlyExpense(data);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading dashboard data:', error);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
