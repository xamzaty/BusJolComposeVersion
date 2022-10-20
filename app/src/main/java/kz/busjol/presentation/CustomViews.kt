package kz.busjol.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import kz.busjol.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun Loader(isDialogVisible: Boolean) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun BackButton(
    modifier: Modifier,
    isArrow: Boolean,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(
            id = if (isArrow) R.drawable.back_button
            else R.drawable.cross
        ),
        contentDescription = "backButton",
        modifier = modifier.clickable { onClick() },
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun CustomTextField(
    text: String,
    @DrawableRes iconId: Int,
    @StringRes hintId: Int,
    @StringRes labelId: Int,
    modifier: Modifier
) {
    Card(
        border = BorderStroke(1.dp, GrayBorder),
        modifier = modifier
            .height(62.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "icon"
                )
            },
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(text = stringResource(id = hintId)) },
            label = { Text(text = stringResource(id = labelId)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ClickableTextField(
    text: String,
    @DrawableRes iconId: Int,
    @StringRes hintId: Int,
    @StringRes labelId: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(62.dp)
            .clickable { onClick() }
    ) {
        TextField(
            value = text,
            onValueChange = {},
            enabled = false,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "icon"
                )
            },
            placeholder = { Text(text = stringResource(id = hintId)) },
            label = { Text(text = stringResource(id = labelId)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ProgressButton(
    @StringRes textId: Int, modifier: Modifier,
    isProgressAvailable: Boolean, isEnabled: Boolean, onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        enabled = isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (!isProgressAvailable) {
            Text(
                text = stringResource(id = textId),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        } else {
            CircularProgressIndicator(
                color = Color.White
            )
        }
    }
}

@Composable
fun CitiesLayout(
    fromCityValue: String,
    toCityValue: String,
    fromCitiesClick: () -> Unit,
    toCitiesClick: () -> Unit,
    swapButtonOnClick: () -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(1.9.dp, GrayBorder))
            .border(BorderStroke(1.9.dp, GrayBorder))
    ) {
        val (fromCityEt, toCityEt, swapButton, divider) = createRefs()

        CitiesTextField(
            value = fromCityValue,
            labelId = R.string.cities_layout_from_label,
            onClick = fromCitiesClick,
            modifier = Modifier.constrainAs(fromCityEt) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(divider.top)
                width = Dimension.fillToConstraints
            })

        Divider(
            color = GrayBorder,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        CitiesTextField(
            value = toCityValue,
            labelId = R.string.cities_layout_to_label,
            onClick = toCitiesClick,
            modifier = Modifier.constrainAs(toCityEt) {
                top.linkTo(divider.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            })

        SwapCitiesButton(
            onClick = swapButtonOnClick,
            modifier = Modifier.constrainAs(swapButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 16.dp)
            })
    }
}

@Composable
private fun CitiesTextField(
    value: String,
    @StringRes labelId: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = {},
        enabled = false,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black),
        placeholder = { Text(text = stringResource(id = R.string.cities_layout_hint)) },
        label = { Text(text = stringResource(id = labelId)) },
        modifier = modifier
            .clickable { onClick() }
            .height(62.dp)
    )
}

@Composable
private fun SwapCitiesButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.size(52.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.swap_cities),
            contentDescription = "swapCities",
            modifier = Modifier.fillMaxSize()
        )
    }
}
