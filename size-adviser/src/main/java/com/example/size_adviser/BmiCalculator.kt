package com.example.size_adviser

internal class BmiCalculator {

    fun calculate(weight: Double, height: Double): SizeAdviser.ProposedSize? {
        if (weight <= 0 || height <= 0) {
            print("Invalid size")
            return null
        }

        val bmiIndex = weight / height / height

        return if (bmiIndex < 18.5) {
            SizeAdviser.ProposedSize.S
        } else if (bmiIndex < 25) {
            SizeAdviser.ProposedSize.M
        } else if (bmiIndex < 30) {
            SizeAdviser.ProposedSize.L
        } else {
            SizeAdviser.ProposedSize.XL
        }
    }
}