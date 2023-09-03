package com.example.ecommerce.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object Helpers {
    fun showSoftKeyboard(context: Context, view: View) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun ChipGroup.setSelectedChip(chipText: String) {
        for (i in 0 until childCount) {
            val chip = getChildAt(i) as? Chip
            if (chip?.text == chipText) {
                chip.isChecked = true
                return
            }
        }
    }

}