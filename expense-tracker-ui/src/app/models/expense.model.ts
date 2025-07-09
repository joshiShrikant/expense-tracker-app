export interface Expense {
  id?: number;
  expenseName: string;
  expenseAmount: number;
  expenseDate: string; // ISO format
  expenseCategory: string;
}
