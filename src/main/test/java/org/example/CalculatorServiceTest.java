package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {
    private final CalculatorService calc = new CalculatorService();

    @Test
    void testAdd() {
        assertEquals(8, calc.add(4, 4));
    }
    @Test
    void testSubtract() {
        assertEquals(2, calc.subtract(5, 3));
    }
    @Test
    void testMultiply() {
        assertEquals(12, calc.multiply(3, 4));
    }
    @Test
    void testDivide() {
        assertEquals(5, calc.divide(10, 2));
    }
    @Test
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calc.divide(1, 0));
    }
}
