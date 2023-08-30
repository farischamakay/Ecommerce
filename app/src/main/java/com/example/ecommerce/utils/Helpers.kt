package com.example.ecommerce.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Helpers {
    fun showSoftKeyboard(context: Context, view: View) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}