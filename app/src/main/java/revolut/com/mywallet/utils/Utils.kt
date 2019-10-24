package revolut.com.mywallet.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val isShowing = imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    if (!isShowing) imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun View.hideKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}
