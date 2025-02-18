package com.example.size_adviser

import android.content.Context
import androidx.annotation.MainThread

/**
 * Class for advising size
 */
public class SizeAdviser {

    /**
     * Display the data input prompt and results
     *
     * @return proposed [ProposedSize] if calculated
     */
    @MainThread
    public suspend fun promptForAdvice(context: Context): ProposedSize? {
        val data = UIHelper.displayInputDialog(context) ?: return null
        val result = BmiCalculator().calculate(data.weight, data.height) ?: return null

        UIHelper.displayResultDialog(context, result)
        return result
    }

    /** Proposed sized */
    public enum class ProposedSize {
        S, M, L, XL
    }
}