import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ExpenseService } from '../../services/expense.service';
import { Expense } from '../../models/expense.model';
import { MatModule } from '../../mat.module';
import { HttpClient } from '@angular/common/http';
import { Category } from '../../models/category.model';
import { CategoryService } from '../../services/CategoryService';

@Component({
  standalone: true,
  selector: 'app-expense-form-component',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, MatModule],
  templateUrl: './expense-form-component.html',
  styleUrl: './expense-form-component.css'
})
export class ExpenseFormComponent implements OnInit {

  form!: FormGroup;
  isEditMode = false;
  expenseId: number | null = null;


  mainCategories: string[] = [];
  subCategories: string[] = [];


  constructor(
    private fb: FormBuilder,
    private expenseService: ExpenseService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    private categoryService: CategoryService 
  ) {}

  initializeForm(){
      this.form = this.fb.group({
      expenseName: ['', Validators.required],
      expenseAmount: [0, [Validators.required, Validators.min(1)]],
      expenseDate: ['', Validators.required],
      mainCategory: ['', Validators.required],
      subCategory: ['', Validators.required],
    });
  }

  checkAndLoadExpense(){
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.isEditMode = true;
        this.expenseId = +idParam;
        this.expenseService.getById(this.expenseId).subscribe(expense => {
          this.form.patchValue(expense);
        });
      }
    });
  }

  private loadMainCategories(): void {
  this.categoryService.getMainCategories().subscribe(data => {
    this.mainCategories = data;
  });
}


  ngOnInit(): void {
    this.checkAndLoadExpense();
    this.loadMainCategories();
    this.initializeForm();
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    const expense = this.form.value as Expense;
    if (this.isEditMode && this.expenseId !== null) {
      expense.id = this.expenseId; // Ensure the ID is set for update
      this.expenseService.update(this.expenseId, expense).subscribe(() => {
        alert('✅ Expense updated successfully');
        this.router.navigate(['/expenses']);
      });
    } else {
      this.expenseService.create(expense).subscribe(() => {
        alert('✅ Expense added successfully');
        this.form.reset();
        this.form.markAsPristine();
        this.form.markAsUntouched();
      });
    }
  }

  onMainCategoryChange(main: string) {
      this.categoryService.getSubCategories(main).subscribe(data => {
    this.subCategories = data.map(c => c.name);
    this.form.get('subCategory')?.setValue('');
  });
}


// INSERT INTO category (name, type) VALUES
// ('Rent', 'Housing'),
// ('Mortgage', 'Housing'),
// ('Property Tax', 'Housing'),
// ('Home Insurance', 'Housing'),
// ('Home Maintenance', 'Housing'),
// ('Utilities', 'Housing'),
// ('Renovation/Repairs', 'Housing'),
// ('Security', 'Housing'),

// ('Fuel', 'Transportation'),
// ('Local Transport', 'Transportation'),
// ('Flights', 'Transportation'),
// ('Vehicle Maintenance', 'Transportation'),
// ('Vehicle Insurance', 'Transportation'),
// ('Parking & Tolls', 'Transportation'),

// ('Electricity', 'Utilities'),
// ('Internet', 'Utilities'),
// ('Water', 'Utilities'),
// ('Gas', 'Utilities'),
// ('Mobile Phone', 'Utilities'),
// ('Garbage Collection', 'Utilities'),
// ('Home Insurance', 'Utilities'),
// ('Property Taxes', 'Utilities'),
// ('Home Maintenance', 'Utilities'),

// ('Vegetables and Fruits', 'Food'),
// ('Groceries', 'Food'),
// ('Restaurants & Dining', 'Food'),
// ('Coffee & Snacks', 'Food'),
// ('Takeout/Delivery', 'Food'),

// ('Medicines', 'Health_Medical'),
// ('Health Insurance', 'Health_Medical'),
// ('Doctor Visits', 'Health_Medical'),

// ('Clothing', 'Personal'),
// ('Personal Care', 'Personal'),
// ('Hobbies', 'Personal'),
// ('Haircuts & Beauty', 'Personal'),
// ('Education', 'Personal'),

// ('Childcare', 'Family'),
// ('Education', 'Family'),
// ('Family Activities', 'Family'),

// ('Bank Fees', 'Finance'),
// ('Insurance', 'Finance'),
// ('Investments', 'Finance'),

// ('Emergency Fund', 'Savings'),
// ('Loan Repayments', 'Savings'),
// ('Credit Card Payments', 'Savings'),
// ('Savings/Investments', 'Savings'),

// ('Movies & Shows', 'Entertainment'),
// ('Travel & Vacations', 'Entertainment'),
// ('Subscriptions', 'Entertainment'),
// ('Hobbies', 'Entertainment'),
// ('Games', 'Entertainment'),
// ('Books', 'Entertainment'),

// ('Birthday', 'Gifts_Donations'),
// ('Holiday', 'Gifts_Donations'),
// ('Charity', 'Gifts_Donations'),

// ('Miscellaneous', 'Miscellaneous'),
// ('Unexpected Expenses', 'Miscellaneous'),
// ('Stationery', 'Miscellaneous'),
// ('Pet Care', 'Miscellaneous'),
// ('Other Expenses', 'Miscellaneous');


}
