package kz.busjol.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kz.busjol.Language
import kz.busjol.R

@Composable
fun SelectLanguageAlertDialog(
    setShowDialog: (Boolean) -> Unit,
    setValue: (Language) -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Выберите язык:",
                        fontSize = 17.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    CustomOutlinedButton(
                        textId = R.string.kazakh_language,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        setValue(Language.KAZAKH)
                    }

                    ProgressButton(
                        textId = R.string.russian_language,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        setValue(Language.RUSSIAN)
                    }
                }
            }
        }
    }
}