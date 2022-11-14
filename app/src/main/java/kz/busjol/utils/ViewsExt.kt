package kz.busjol.utils

import android.content.Context
import android.widget.Toast
import kz.busjol.R

fun Context.showDataErrorToast(
    errorState: String?,
    unit: (() -> Unit)? = null
) {
    if (errorState != null) {
        Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show()
    } else {
        if (unit != null) {
            unit()
        }
    }
}