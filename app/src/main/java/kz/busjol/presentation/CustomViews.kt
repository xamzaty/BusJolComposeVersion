package kz.busjol.presentation

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import kz.busjol.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.launch
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
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
                BackButton(modifier = Modifier.padding(start = 15.dp)) {
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
fun NotFoundView(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bus_with_city_image),
            contentDescription = "notFound"
        )

        Text(
            text = stringResource(id = R.string.not_found),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    content: (sheetState: ModalBottomSheetState) -> Composable
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(sheetContent = {

    }) {
        content(sheetState)
    }
}

@Composable
fun GrayDivider(modifier: Modifier) {
    Divider(
        color = GrayBorder,
        thickness = 1.dp,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: () -> Unit
) {
    Card(
        elevation = 0.dp,
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        content()
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
fun BackButton(
    modifier: Modifier,
    onClick: () -> Unit
    ) {
    Card(
        elevation = 0.dp,
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .size(32.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "arrow",
            contentScale = ContentScale.Inside,
            modifier = Modifier
        )
    }
}

fun Modifier.scrollEnabled(
    enabled: Boolean,
) = nestedScroll(
    connection = object : NestedScrollConnection {
        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset = if(enabled) Offset.Zero else available
    }
)

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconId: Int? = null,
    @StringRes hintId: Int,
    @StringRes labelId: Int,
) {

    var textValue by rememberSaveable { mutableStateOf(text) }

    Card(
        border = BorderStroke(1.dp, GrayBorder),
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
    ) {
        TextField(
            value = textValue,
            onValueChange = {
               textValue = it
            },
            trailingIcon = {
                iconId?.let { painterResource(id = it) }?.let {
                    Icon(
                        painter = it,
                        contentDescription = "icon"
                    )
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

    val textValue by rememberSaveable { mutableStateOf(text) }

    Card(
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        modifier = modifier
            .clickable { onClick() }
            .height(62.dp)
    ) {
        TextField(
            value = textValue,
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
    isProgressAvailable: Boolean, isEnabled: Boolean, onClick: () -> Unit
) {
    val isButtonEnabled by rememberSaveable { mutableStateOf(isEnabled) }
    val isLoadingOn by rememberSaveable { mutableStateOf(isProgressAvailable) }

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        enabled = isButtonEnabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (!isLoadingOn) {
            Text(
                text = stringResource(id = textId),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
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
fun NestedLazyList(
    modifier: Modifier = Modifier,
    outerState: LazyListState = rememberLazyListState(),
    innerState: LazyListState = rememberLazyListState(),
    outerContent: LazyListScope.() -> Unit,
    innerContent: LazyListScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()
    val innerFirstVisibleItemIndex by remember {
        derivedStateOf {
            innerState.firstVisibleItemIndex
        }
    }
    SideEffect {
        if (outerState.layoutInfo.visibleItemsInfo.size == 2 && innerState.layoutInfo.totalItemsCount == 0)
            scope.launch { outerState.scrollToItem(outerState.layoutInfo.totalItemsCount) }
        println("outer ${outerState.layoutInfo.visibleItemsInfo.map { it.index }}")
        println("inner ${innerState.layoutInfo.visibleItemsInfo.map { it.index }}")
    }

    BoxWithConstraints(
        modifier = modifier
            .scrollable(
                state = rememberScrollableState {
                    scope.launch {
                        val toDown = it <= 0
                        if (toDown) {
                            if (outerState.run { firstVisibleItemIndex == layoutInfo.totalItemsCount - 1 }) {
                                innerState.scrollBy(-it)
                            } else {
                                outerState.scrollBy(-it)
                            }
                        } else {
                            if (innerFirstVisibleItemIndex == 0 && innerState.firstVisibleItemScrollOffset == 0) {
                                outerState.scrollBy(-it)
                            } else {
                                innerState.scrollBy(-it)
                            }
                        }
                    }
                    it
                },
                Orientation.Vertical,
            )
    ) {
        LazyColumn(
            userScrollEnabled = false,
            state = outerState,
            modifier = Modifier
                .heightIn(maxHeight)
        ) {
            outerContent()
            item {
                LazyColumn(
                    state = innerState,
                    userScrollEnabled = false,
                    modifier = Modifier
                        .height(maxHeight)

                ) {
                    innerContent()
                }
            }
        }

    }
}
