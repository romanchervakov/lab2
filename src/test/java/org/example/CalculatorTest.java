package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest{
    @Test
    void calcInPar() {
          Calculator test = new Calculator("(1+1)");
          test.Calculate();
          assertEquals(2, test.result);
    }

    @Test
    void calculate() {
        Calculator test = new Calculator("1+cos(pi/2)");
        test.Calculate();
        assertEquals(1, test.result);
    }
}