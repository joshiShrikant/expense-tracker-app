export interface Expense {
  id?: number;
  expenseName: string;
  expenseAmount: number;
  expenseDate: string; // ISO format
  expenseCategory: string;
  totalExpenses?: number;
  categoryTotals?: { [key: string]: number };
  userId?: number; // Optional, if you want to associate with a user
}
