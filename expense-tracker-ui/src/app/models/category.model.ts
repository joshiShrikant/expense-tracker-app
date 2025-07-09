export interface Category {
  id: number;
  name: string;
  parentId?: number;
}
export interface CategoryResponse {
  categories: Category[];
}