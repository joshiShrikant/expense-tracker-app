import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/DashboardService';
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

  monthlyExpense = 0;
  topCategory = 'Food';

  constructor(private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.dashboardService.getDashboardData().subscribe({
      next: (data) => {
        this.totalExpenses = data.totalExpenses;
        this.categoryTotals = data.categoryTotals;
        this.categoryKeys = Object.keys(this.categoryTotals);
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
