package com.jts.expensetracker.dtoTest;

import com.jts.expensetracker.model.Category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryCreation() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Groceries");
        category.setType("Food");

        assertEquals(1L, category.getId());
        assertEquals("Groceries", category.getName());
        assertEquals("Food", category.getType());
    }

    @Test
    void testCategoryEquality() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Groceries");
        category1.setType("Food");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Groceries");
        category2.setType("Food");

        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }
}