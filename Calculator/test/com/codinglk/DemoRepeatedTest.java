package com.codinglk;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DemoRepeatedTest {

    Calculator calculator;

    @BeforeEach
    void beforeEachTestMethod(){
        calculator = new Calculator();
        System.out.println("Executing @BeforeEach method.");
    }

    @DisplayName("Division By Zero")
   // @RepeatedTest(3) // It will be called three times
    // We can use template variables to change the repetition display name
    @RepeatedTest(value = 3, name = "{displayName}. Repetition {currentRepetition} of {totalRepetitions}")
    void testIntegerDivision_WhenDividendIsDividedByZero_ShouldThrowArithmeticException(RepetitionInfo repetitionInfo, TestInfo testInfo) {
        System.out.println("Running +"+ testInfo.getTestMethod().get().getName());
        System.out.println("Repetition #"+repetitionInfo.getCurrentRepetition()+" of "+repetitionInfo.getTotalRepetitions());
        // Arrange
        int dividend = 4;
        int divisor = 0;
        String expectedExceptionMessage="/ by zero";

        // Act & Assert
        ArithmeticException arithmeticException = assertThrows(ArithmeticException.class, ()->{
            //Act
            calculator.integerDivision(dividend, divisor);
        }, "Division by zero should have thrown an Arithmetic exception.");

        // Assert
        assertEquals(expectedExceptionMessage, arithmeticException.getMessage(), "Unexpected exception message");

    }
}
