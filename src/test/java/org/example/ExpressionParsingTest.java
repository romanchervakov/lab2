package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionParsingTest {
    ExpressionParsing right;
    ExpressionParsing wrong1;
    ExpressionParsing wrong2;
    ExpressionParsing wrong3;
    ExpressionParsing wrong4;
    ExpressionParsing wrong5;
    ExpressionParsing wrong6;
    ExpressionParsing wrong7;
    ExpressionParsing wrong8;
    ExpressionParsing wrong9;
    ExpressionParsing wrong10;
    ExpressionParsing wrong11;
    ExpressionParsing wrong12;
    ExpressionParsing test;

    @BeforeEach
    void set_up() {
                right = new ExpressionParsing("-9.99+(sin(a^2+b*cos(pi)))");
                wrong1 = new ExpressionParsing("&+(sin(a^2+b*cos(pi)))");
                wrong2 = new ExpressionParsing("-9.99+(sin(a^2+b*cos(pi))");
                wrong3 = new ExpressionParsing("-9.99+(sin(a^2+-b*cos(pi)))");
                wrong4 = new ExpressionParsing("-9.99+(sin(a^2+b*cos(pi-)))");
                wrong5 = new ExpressionParsing("-9.99+(sin(ab*cos(pi)))");
                wrong6 = new ExpressionParsing("-9.9.9+(sin(a^2+b*cos(pi)))");
                wrong7 = new ExpressionParsing("-9.99+(sin(a^2^2+b*cos(pi)))");
                wrong8 = new ExpressionParsing("-9.9+9(sin(a^2^2+b*cos(pi)))");
                wrong9 = new ExpressionParsing("-a.9+9(sin(a^2^2+b*cos(pi)))");
                wrong10 = new ExpressionParsing("-9.99+(sin(a^2+b*cas(pi)))");
                wrong11 = new ExpressionParsing("-9.99+(sin(5b*cos(pi)))");
                wrong12 = new ExpressionParsing("-9.99+(sin()+1)");
    }

    @Test
    void parBalCheck() {
                     assertEquals(0, right.ParBalCheck());
                     assertEquals(1, wrong2.ParBalCheck());
                     assertEquals(2, wrong12.ParBalCheck());
    }

    @Test
    void symbCheck() {
                   right.ParBalCheck();
                   wrong1.ParBalCheck();
                   assertTrue(right.SymbCheck());
                   assertFalse(wrong1.SymbCheck());
    }

    @Test
    void serv1() {
               assertEquals("sin", right.serv1(10,3));
    }

    @Test
    void funcConstCheck() {
                        right.ParBalCheck();
                        right.SymbCheck();
                        wrong10.ParBalCheck();
                        wrong10.SymbCheck();
                        assertTrue(right.FuncConstCheck());
                        assertFalse(wrong10.FuncConstCheck());
    }

    @Test
    void symbAnalysis() {
                      right.serv3();
                      wrong3.serv3();
                      wrong4.serv3();
                      wrong5.serv3();
                      wrong6.serv3();
                      wrong7.serv3();
                      wrong8.serv3();
                      wrong9.serv3();
                      wrong11.serv3();
                      assertTrue(right.SymbAnalysis());
                      assertFalse(wrong3.SymbAnalysis());
                      assertFalse(wrong4.SymbAnalysis());
                      assertFalse(wrong5.SymbAnalysis());
                      assertFalse(wrong6.SymbAnalysis());
                      assertFalse(wrong7.SymbAnalysis());
                      assertFalse(wrong8.SymbAnalysis());
                      assertFalse(wrong9.SymbAnalysis());
                      assertFalse(wrong11.SymbAnalysis());
    }

    @Test
    void varInputCheck() {
        test = new ExpressionParsing("a");
        String var_r1 = "-9.9";
        String var_r2 = "pi";
        String var_r3 = "e";
        String var_w1 = "-";
        String var_w2 = "+";
        String var_w3 = "a";
        String var_w4 = "9.9.9";
        assertTrue(test.VarInputCheck(var_r1));
        assertTrue(test.VarInputCheck(var_r2));
        assertTrue(test.VarInputCheck(var_r3));
        assertFalse(test.VarInputCheck(var_w1));
        assertFalse(test.VarInputCheck(var_w2));
        assertFalse(test.VarInputCheck(var_w3));
        assertFalse(test.VarInputCheck(var_w4));
    }

}