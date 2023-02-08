package kz.busjol.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.material.ScaffoldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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

fun ScaffoldState.showSnackBar(
    scope: CoroutineScope,
    text: String = ""
) {
    scope.launch {
        this@showSnackBar.snackbarHostState.showSnackbar(
            message = text,
            actionLabel = "ОК"
        )
    }
}