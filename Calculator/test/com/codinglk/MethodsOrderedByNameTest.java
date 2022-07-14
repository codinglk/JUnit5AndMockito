package com.codinglk;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MethodsOrderedByNameTest {

    @Test
    void testD() {
        System.out.println("Running Test D");
    }

    @Test
    void testA() {
        System.out.println("Running Test A");
    }

    @Test
    void testC() {
        System.out.println("Running Test C");
    }

    @Test
    void testB() {
        System.out.println("Running Test B");
    }

}
