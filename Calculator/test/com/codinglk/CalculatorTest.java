package com.codinglk;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Math Operations in Calculator class")
class CalculatorTest {

    Calculator calculator;

    @BeforeAll
    static void setup(){
        System.out.println("Executing @BeforeAll method.");
    }

    @AfterAll
    static void cleanup(){
        System.out.println("Executing @AfterAll method.");
    }

    @BeforeEach
    void beforeEachTestMethod(){
        calculator = new Calculator();
        System.out.println("Executing @BeforeEach method.");
    }

    @AfterEach
    void afterEachTestMethod(){
        System.out.println("Executing @AfterEach method.");
    }

    // test<System Under Test>_< Condition or State Change>_<Expected Result>
    @DisplayName("4/2 = 2")
    @Test
    void testIntegerDivision_WhenFourIsDividedByTwo_ShouldReturnTwo() {
        System.out.println("4/2 = 2");
        //Arrange //Given
        int dividend=4;
        int divisor=2;
        int expectedResult=2;
        //Act //When
        int actualResult = calculator.integerDivision(dividend,divisor);
        //Assert //Then
        assertEquals(expectedResult, actualResult, "4/2 did not produce 2");
    }

   // @Disabled("TODO: Still need to work on it.")
    @DisplayName("Division By Zero")
    @Test
    void testIntegerDivision_WhenDividendIsDividedByZero_ShouldThrowArithmeticException() {
        System.out.println("Division By Zero");
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

    @DisplayName("Using MethodSource - Test integer subtraction [minuend, subtrahend, expectedResult]")
    @ParameterizedTest
    @MethodSource()
    void integerSubtraction(int minuend, int subtrahend, int expectedResult) {
        System.out.println("Running Test "+minuend+ "-"+subtrahend+ "="+ expectedResult);
//        int minuend = 41;
//        int subtrahend = 2;
//        int expectedResult = 39;
        int actualResult = calculator.integerSubtraction(minuend,subtrahend);
        // We are using Lambda for message to improve the performance, It will work on the message when it is actually needed
        assertEquals(expectedResult,actualResult,()-> minuend + "-"+ subtrahend+" did not produce "+expectedResult);
    }

    // Arguments Source for integerSubtraction
    private static Stream<Arguments> integerSubtraction() {

        return Stream.of(
                Arguments.of(41,2,39),
                Arguments.of(55,2,53),
                Arguments.of(28,2,26)
        );
    }

    @DisplayName("Using CsvSource - Test integer subtraction [minuend, subtrahend, expectedResult]")
    @ParameterizedTest
    @CsvSource({
            "41,2,39",
            "55,2,53",
            "28,2,26"
    })
    void integerSubtractionUsingCsvSource(int minuend, int subtrahend, int expectedResult) {
        System.out.println("Running Test "+minuend+ "-"+subtrahend+ "="+ expectedResult);
        int actualResult = calculator.integerSubtraction(minuend,subtrahend);
        // We are using Lambda for message to improve the performance, It will work on the message when it is actually needed
        assertEquals(expectedResult,actualResult,()-> minuend + "-"+ subtrahend+" did not produce "+expectedResult);
    }

    @DisplayName("Using CsvSource - Test String Values [firstValue, secondValue]")
    @ParameterizedTest
    @CsvSource({
            "codinglk, fun",
            "codinglk,''", // use '' for empty string value
            "codonglk," // use nothing after , to pass null
    })
    void testStringParametersUsingCsvSource(String firstValue, String secondValue) {
        System.out.println("First Value "+firstValue+ " Second Value "+secondValue);
    }

    @DisplayName("Using CsvFileSource - Test integer subtraction [minuend, subtrahend, expectedResult]")
    @ParameterizedTest
    @CsvFileSource(resources = "/IntegerSubtraction.csv")
    void integerSubtractionUsingCsvFileSource(int minuend, int subtrahend, int expectedResult) {
        System.out.println("Running Test "+minuend+ "-"+subtrahend+ "="+ expectedResult);
        int actualResult = calculator.integerSubtraction(minuend,subtrahend);
        // We are using Lambda for message to improve the performance, It will work on the message when it is actually needed
        assertEquals(expectedResult,actualResult,()-> minuend + "-"+ subtrahend+" did not produce "+expectedResult);
    }

    @DisplayName("ValueSource Demonstration")
    @ParameterizedTest
    @ValueSource(strings={"codinglk", "Mike", "Everett"})
    // This method will run three times for each value
    void valueSourceDemonstration(String firstName){
        System.out.println(firstName);
        assertNotNull(firstName);
    }
}