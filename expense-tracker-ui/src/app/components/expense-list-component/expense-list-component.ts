import { Component, OnInit, ViewChild } from '@angular/core';
import { Expense } from '../../models/expense.model';
import { ExpenseService } from '../../services/expense.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatModule } from '../../mat.module';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  standalone: true,
  selector: 'app-expense-list-component',
  imports: [CommonModule, RouterModule, MatModule],
  templateUrl: './expense-list-component.html',
  styleUrls: ['./expense-list-component.css']
})
export class ExpenseListComponent implements OnInit {
 
  displayedColumns: string[] = ['id', 'Expense Name', 'amount', 'mainCategory', 'subCategory', 'date', 'actions'];
  
  dataSource = new MatTableDataSource<any>();

  totalRecords = 0;

@ViewChild(MatPaginator) paginator!: MatPaginator;

ngAfterViewInit(): void {
  this.dataSource.paginator = this.paginator;
}

  constructor(
    private expenseService: ExpenseService,
    private router: Router) {}

  ngOnInit() {
    this.loadExpenses();    
  }

  loadExpenses() {
    this.expenseService.getAll().subscribe(data => {
      this.dataSource.data = data;
      this.totalRecords = data.length;
    });
  }

   editExpense(id: number) {
    this.router.navigate(['/expenses/edit', id]);
  }

  deleteExpense(id: number) {
    if (confirm('Are you sure you want to delete this expense?')) {
      this.expenseService.delete(id).subscribe(() => {
        this.dataSource.data = this.dataSource.data.filter(e => e.id !== id);
      });
    }
  }

openAddExpenseDialog() {
  // navigate to a form route
  this.router.navigate(['/add-expense']);
}
  
}

