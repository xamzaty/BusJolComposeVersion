package kz.busjol.presentation

import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.style.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import kz.busjol.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.getSpans
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.presentation.theme.GrayText
import kz.busjol.utils.MaskVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    isCross: Boolean = false,
    onClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.W700,
                    color = Color.Black
                )
            },
            navigationIcon = {
                BackButton(
                    modifier = Modifier.padding(start = 15.dp),
                    isCross = isCross
                ) {
                    onClick()
                }
            },
            backgroundColor = Color.Transparent,
            contentColor = Color.Black,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 7.dp)
        )

        Divider(
            color = GrayBorder,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.7f)
        )
    }
}

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    isCross: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        elevation = 0.dp,
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable { onClick() }
            .size(32.dp)
    ) {
        Image(
            painter = painterResource(
                id = if (!isCross) R.drawable.arrow_back else R.drawable.cross
            ),
            contentDescription = "arrow",
            contentScale = ContentScale.Inside,
            modifier = Modifier
        )
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun CustomAlertDialog(
    openDialog: Boolean,
    title: String,
    description: String,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onConfirmButtonClicked: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(openDialog) }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = description)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onConfirmButtonClicked()
                    }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }) {
                    Text(dismissButtonText)
                }
            }
        )
    }
}

@Composable
fun NotFoundView(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int = R.drawable.bus_with_city_image,
    @StringRes textId: Int = R.string.not_found
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "notFound",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.size(128.dp)
        )

        Text(
            text = stringResource(id = textId),
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun Loader(isDialogVisible: Boolean) {
    var showDialog by rememberSaveable { mutableStateOf(isDialogVisible) }

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
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconId: Int? = null,
    @StringRes hintId: Int,
    @StringRes labelId: Int,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation? = null,
    maskVisualTransformation: MaskVisualTransformation? = null,
    maxChar: Int? = null,
    isHiddenToggleVisible: Boolean = false,
    isError: Boolean = false,
    errorText: String = "",
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var textValue by rememberSaveable { mutableStateOf(text) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Card(
            border = BorderStroke(1.dp, GrayBorder),
            elevation = 0.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            TextField(
                value = textValue,
                onValueChange = {
                    if (it.length <= (maxChar ?: 1000)) textValue = it
                    onValueChange(it)
                },
                maxLines = 1,
                singleLine = true,
                visualTransformation = if (isHiddenToggleVisible) {
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                } else {
                    maskVisualTransformation ?: visualTransformation ?: VisualTransformation.None
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                isError = isError,
                trailingIcon = {
                    if (iconId != null) {
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = "icon"
                        )
                    }

                    if (isHiddenToggleVisible) {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector = image, description)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text(text = stringResource(id = hintId)) },
                label = { Text(text = stringResource(id = labelId)) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        if (isError) {
            Text(
                text = errorText,
                color = Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun MaskTextField(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconId: Int? = null,
    @StringRes hintId: Int,
    @StringRes labelId: Int,
    onValueChange: (String) -> Unit,
    maxChar: Int? = null,
    textFieldMaskVariants: TextFieldMaskVariants,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    var textValue by rememberSaveable { mutableStateOf(text) }

    val phoneMask = "+7 ### ### ## ##"
    val otherMask = "############################################################"

    Card(
        border = BorderStroke(1.dp, GrayBorder),
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        TextField(
            value = textValue,
            onValueChange = {
                if (it.length <= (maxChar ?: 1000)) textValue = it
                onValueChange(it)
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                iconId?.let { painterResource(id = it) }?.let {
                    Icon(
                        painter = it,
                        contentDescription = "icon"
                    )
                }
            },
            visualTransformation = when (textFieldMaskVariants) {
                TextFieldMaskVariants.PHONE -> MaskVisualTransformation(phoneMask)
                else -> MaskVisualTransformation(otherMask)
            },
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(text = stringResource(id = hintId)) },
            label = { Text(text = stringResource(id = labelId)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Transparent,
                backgroundColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

enum class TextFieldMaskVariants {
    PHONE, EMAIL, PASSWORD
}

@Composable
fun CustomTextFieldWithMask(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconId: Int? = null,
    @StringRes hintId: Int,
    @StringRes labelId: Int,
    onValueChange: (String) -> Unit,
    maxChar: Int? = null,
    maskVisualTransformation: MaskVisualTransformation,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    var textValue by rememberSaveable { mutableStateOf(text) }

    Card(
        border = BorderStroke(1.dp, GrayBorder),
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        TextField(
            value = textValue,
            onValueChange = {
                if (it.length <= (maxChar ?: 1000)) textValue = it
                onValueChange(it)
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                iconId?.let { painterResource(id = it) }?.let {
                    Icon(
                        painter = it,
                        contentDescription = "icon"
                    )
                }
            },
            visualTransformation = maskVisualTransformation,
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(text = stringResource(id = hintId)) },
            label = { Text(text = stringResource(id = labelId)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Transparent,
                backgroundColor = White,
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
        elevation = 0.dp,
        modifier = modifier
            .clickable { onClick() }
            .height(62.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {},
            enabled = false,
            trailingIcon = {
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = "icon",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            },
            placeholder = { Text(text = stringResource(id = hintId)) },
            label = { Text(text = stringResource(id = labelId)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF444444),
                disabledTextColor = Color.Transparent,
                backgroundColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color(0xFF444444)
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CustomSimpleTextField(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    var textValue by rememberSaveable { mutableStateOf(text) }

    BasicTextField(modifier = modifier
        .background(
            MaterialTheme.colors.surface,
            MaterialTheme.shapes.small,
        )
        .fillMaxWidth(),
        value = text,
        onValueChange = {
            textValue = it
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}

@Composable
fun ProgressButton(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    isProgressBarActive: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val alpha = remember {
        mutableStateOf(
            if (enabled) 1f else 0.6f
        )
    }

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = Blue500
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .alpha(alpha.value)
    ) {
        if (!isProgressBarActive) {
            Text(
                text = stringResource(id = textId),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = White,
                modifier = Modifier
                    .alpha(alpha.value)
            )
        } else {
            CircularProgressIndicator(
                color = White
            )
        }
    }
}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Blue500),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = stringResource(id = textId),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Blue500
        )
    }
}

@Composable
fun MultiStyleTextRow(
    text1: String,
    color1: Color,
    text2: String,
    color2: Color,
    fontSize: Int,
    modifier: Modifier = Modifier
) {
    Row {
        Text(
            text = text1,
            color = color1,
            fontSize = fontSize.sp,
            modifier = Modifier.padding(end = 2.dp)
        )

        Text(
            text = text2,
            color = color2,
            fontSize = fontSize.sp,
            modifier = Modifier.padding(end = 2.dp)
        )
    }
}


@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    @StringRes textId: Int,
    urlSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colors.secondary),
    colorMapping: Map<Color, Color> = emptyMap(),
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val context = LocalContext.current
    val annotatedString = context.resources.getText(textId).toAnnotatedString(urlSpanStyle, colorMapping)
    val clickable = annotatedString.getStringAnnotations(0, annotatedString.length - 1).any { it.tag == "url" }

    val uriHandler = LocalUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        modifier = modifier.then(if (clickable) Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { pos ->
                layoutResult.value?.let { layoutResult ->
                    val position = layoutResult.getOffsetForPosition(pos)
                    annotatedString.getStringAnnotations(position, position)
                        .firstOrNull()
                        ?.let { sa ->
                            if (sa.tag == "url") { // NON-NLS
                                uriHandler.openUri(sa.item)
                            }
                        }
                }
            })
        } else Modifier),
        text = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        style = style
    )
}

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    urlSpanStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colors.secondary),
    colorMapping: Map<Color, Color> = emptyMap(),
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val annotatedString = if (Build.VERSION.SDK_INT <24) {
        Html.fromHtml(text)
    } else {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }.toAnnotatedString(urlSpanStyle, colorMapping)

    val clickable = annotatedString.getStringAnnotations(0, annotatedString.length - 1).any { it.tag == "url" }

    val uriHandler = LocalUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        modifier = modifier.then(if (clickable) Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { pos ->
                layoutResult.value?.let { layoutResult ->
                    val position = layoutResult.getOffsetForPosition(pos)
                    annotatedString.getStringAnnotations(position, position)
                        .firstOrNull()
                        ?.let { sa ->
                            if (sa.tag == "url") { // NON-NLS
                                uriHandler.openUri(sa.item)
                            }
                        }
                }
            })
        } else Modifier),
        text = annotatedString,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        style = style
    )
}

fun CharSequence.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue
    ),
    colorMapping: Map<Color, Color> = emptyMap()
): AnnotatedString {
    return if (this is Spanned) {
        this.toAnnotatedString(urlSpanStyle, colorMapping)
    } else {
        buildAnnotatedString {
            append(this@toAnnotatedString.toString())
        }
    }
}

fun Spanned.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue
    ),
    colorMapping: Map<Color, Color> = emptyMap()
): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())
        val urlSpans = getSpans<URLSpan>()
        val styleSpans = getSpans<StyleSpan>()
        val colorSpans = getSpans<ForegroundColorSpan>()
        val underlineSpans = getSpans<UnderlineSpan>()
        val strikethroughSpans = getSpans<StrikethroughSpan>()
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation("url", urlSpan.url, start, end) // NON-NLS
        }
        colorSpans.forEach { colorSpan ->
            val start = getSpanStart(colorSpan)
            val end = getSpanEnd(colorSpan)
            addStyle(SpanStyle(color = colorMapping.getOrElse(Color(colorSpan.foregroundColor)) { Color(colorSpan.foregroundColor) }), start, end)
        }
        styleSpans.forEach { styleSpan ->
            val start = getSpanStart(styleSpan)
            val end = getSpanEnd(styleSpan)
            when (styleSpan.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic), start, end)
            }
        }
//        underlineSpans.forEach { underlineSpan ->
//            val start = getSpanStart(underlineSpan)
//            val end = getSpanEnd(underlineSpan)
//            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
//        }
        strikethroughSpans.forEach { strikethroughSpan ->
            val start = getSpanStart(strikethroughSpan)
            val end = getSpanEnd(strikethroughSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), start, end)
        }
    }
}

@Composable
fun DashLine(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(modifier.fillMaxWidth().height(5.dp)) {

        drawLine(
            color = GrayBorder,
            start = Offset(0f, 0f),
            end = Offset(size.width, 1f),
            pathEffect = pathEffect
        )
    }
}