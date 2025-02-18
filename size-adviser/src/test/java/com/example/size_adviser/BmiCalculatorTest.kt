package com.example.size_adviser

import org.junit.Test

import org.junit.Assert.*

class BmiCalculatorTest {

    private val calculator = BmiCalculator()

    @Test
    public fun testInvalidInputData() {
        var result = calculator.calculate(-1.0, -1.0)
        assertNull(result)

        result = calculator.calculate(-1.0, -1.0)
        assertNull(result)

        result = calculator.calculate(0.0, 0.0)
        assertNull(result)

        result = calculator.calculate(-1.0, 1.0)
        assertNull(result)

        result = calculator.calculate(1.0, -1.0)
        assertNull(result)
    }

    @Test
    public fun testCalculations() {
        //S = <18.5
        var result = calculator.calculate(10.0, 1.0)
        assertEquals(SizeAdviser.ProposedSize.S, result)

        result = calculator.calculate(18.4, 1.0)
        assertEquals(SizeAdviser.ProposedSize.S, result)

        //M = 18.5–24.9
        result = calculator.calculate(18.5, 1.0)
        assertEquals(SizeAdviser.ProposedSize.M, result)

        result = calculator.calculate(20.0, 1.0)
        assertEquals(SizeAdviser.ProposedSize.M, result)

        result = calculator.calculate(24.9, 1.0)
        assertEquals(SizeAdviser.ProposedSize.M, result)

        //L = 25–29.9
        result = calculator.calculate(25.0, 1.0)
        assertEquals(SizeAdviser.ProposedSize.L, result)

        result = calculator.calculate(27.0, 1.0)
        assertEquals(SizeAdviser.ProposedSize.L, result)

        result = calculator.calculate(29.9, 1.0)
        assertEquals(SizeAdviser.ProposedSize.L, result)

        //XL = >=30
        result = calculator.calculate(30.0, 1.0)
        assertEquals(SizeAdviser.ProposedSize.XL, result)

        result = calculator.calculate(100.0, 1.0)
        assertEquals(SizeAdviser.ProposedSize.XL, result)
    }
}