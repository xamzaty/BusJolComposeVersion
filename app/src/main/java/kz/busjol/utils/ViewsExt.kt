package kz.busjol.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
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

@Composable
fun setSoftInputMode(mode: Int) {
    val context = LocalContext.current
    val windowInfo = LocalWindowInfo.current
    val window = remember(context, windowInfo) { (context as? Activity)?.window }

    if (window != null) {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior = mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

//    if (bottomBarState.value == navBackStackEntry?.destination?.route in visibleRoutes) {
//        setSoftInputMode(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
//    } else {
//        setSoftInputMode(WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE)
//    }