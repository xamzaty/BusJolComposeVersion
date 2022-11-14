package kz.busjol.presentation.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.BuildConfig
import kz.busjol.Language
import kz.busjol.presentation.profile.change_language.ChangeLanguageScreen
import kz.busjol.presentation.profile.rate_the_app.RateTheApp
import kz.busjol.R
import kz.busjol.UserState
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.destinations.LoginScreenDestination
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.utils.setLocale

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var currentBottomSheet: ProfileBottomSheetScreen? by remember {
        mutableStateOf(null)
    }

    val closeSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    BackHandler(scaffoldState.bottomSheetState.isExpanded) {
        scope.launch { scaffoldState.bottomSheetState.collapse() }
    }

    val openSheet: (ProfileBottomSheetScreen) -> Unit = {
        currentBottomSheet = it
        scope.launch { scaffoldState.bottomSheetState.expand() }

    }

    if (scaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null

    BottomSheetScaffold(sheetPeekHeight = 0.dp, scaffoldState = scaffoldState,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet, navigator, closeSheet)
            }
        }) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            MainContent(openSheet, scope, navigator)
        }
    }
}

@Composable
private fun SheetLayout(
    currentScreen: ProfileBottomSheetScreen,
    navigator: DestinationsNavigator,
    onCloseBottomSheet: () -> Unit) {
    when (currentScreen) {
        is ProfileBottomSheetScreen.ChangeLanguageScreen -> ChangeLanguageScreen(
            onCloseBottomSheet, navigator
        )
        is ProfileBottomSheetScreen.RateTheAppScreen -> RateTheApp(
            onCloseBottomSheet
        )
    }
}

@Composable
private fun MainContent(
    openSheet: (ProfileBottomSheetScreen) -> Unit,
    scope: CoroutineScope,
    navigator: DestinationsNavigator,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val isUserAuthorized = remember { mutableStateOf(
        state.userState == UserState.REGISTERED
    )}

    val isDriverAuthorized = remember { mutableStateOf(
        state.userState == UserState.DRIVER
    )}

    val isAuthorized = remember { mutableStateOf(
        isUserAuthorized.value || isDriverAuthorized.value
    )}

    val emailValue = remember { "h.yerzhanov@gmail.com" }

    val appVersion = remember { mutableStateOf(BuildConfig.VERSION_NAME) }

    val verticalScrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 22.dp, bottom = 24.dp)
            .verticalScroll(verticalScrollState)
    ) {

        Text(
            text = stringResource(id = R.string.profile_title),
            color = Color.Black,
            fontWeight = FontWeight.W700,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        )

        WeDoNotRecogniseYouLayout(
            isUserAuthorized = isAuthorized.value,
            modifier = Modifier.padding(
                start = 15.dp, top = 38.dp, end = 15.dp, bottom = 8.dp
            ),
            scope = scope,
            navigator = navigator
        )

        DriverLayout(
            isDriverAuthorized = isDriverAuthorized.value,
            email = emailValue,
            modifier = Modifier.padding(top = 48.dp)
        )

        AuthorizedUserLayout(
            isUserAuthorized = isUserAuthorized.value,
            email = emailValue,
            modifier = Modifier.padding(top = 24.dp)
        )

        SettingsLayout(
            modifier = Modifier.padding(top = if (isAuthorized.value) 20.dp else 28.dp),
            openSheet
        )

        AdditionalLayout(
            modifier = Modifier.padding(top = 20.dp),
            openSheet
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.busjol),
            contentDescription = "",
            modifier = Modifier
                .width(111.dp)
                .height(20.dp)
        )

        Text(
            text = stringResource(id = R.string.app_version, appVersion.value),
            textAlign = TextAlign.Center,
            color = Color(0xFF8B98A7),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun WeDoNotRecogniseYouLayout(
    modifier: Modifier = Modifier,
    isUserAuthorized: Boolean,
    scope: CoroutineScope,
    navigator: DestinationsNavigator
) {
    if (!isUserAuthorized) {
        Card(
            elevation = 0.dp,
            backgroundColor = Color(0xFFF2F6F8),
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.we_did_not_recognise_you_title),
                    fontWeight = FontWeight.W600,
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Text(
                    text = stringResource(id = R.string.we_did_not_recognise_you_text),
                    fontSize = 14.sp,
                    color = Color(0xFF8B98A7),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                ProgressButton(
                    textId = R.string.enter_button,
                    isProgressAvailable = false,
                    isEnabled = true
                ) {
                    scope.launch {
                        navigator.navigate(
                            LoginScreenDestination()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverLayout(
    modifier: Modifier = Modifier,
    email: String? = null,
    isDriverAuthorized: Boolean
) {
    if (isDriverAuthorized) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = email.orEmpty(),
                color = Color.Black,
                fontWeight = FontWeight.W600,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.exit_button),
                fontSize = 13.sp,
                color = Color.Red
            )
        }

        Subtitle(
            text = stringResource(id = R.string.main_subtitle),
            modifier = Modifier.padding(top = 28.dp)
        )
        
        ClickableLayout(
            text = stringResource(id = R.string.my_data_subtitle),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            
        }
        
        ClickableLayout(text = stringResource(id = R.string.my_trips_subtitle)) {
            
        }
    }
}

@Composable
private fun AuthorizedUserLayout(
    modifier: Modifier = Modifier,
    email: String? = null,
    isUserAuthorized: Boolean
) {
    if (isUserAuthorized) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = email.orEmpty(),
                    color = Color.Black,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(id = R.string.exit_button),
                    fontSize = 13.sp,
                    color = Color.Red
                )
            }

            Subtitle(
                text = stringResource(id = R.string.main_subtitle),
                modifier = Modifier.padding(top = 28.dp)
            )

            ClickableLayout(
                text = stringResource(id = R.string.my_data_subtitle),
                modifier = Modifier.padding(top = 4.dp)
            ) {

            }

            ClickableLayout(text = stringResource(id = R.string.passengers_subtitle)) {

            }
        }
    }
}

@Composable
private fun SettingsLayout(
    modifier: Modifier = Modifier,
    openSheet: (ProfileBottomSheetScreen) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Subtitle(text = stringResource(id = R.string.settings_subtitle))

        ClickableLayout(
            text = stringResource(id = R.string.change_language),
            language = stringResource(id = R.string.app_language),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            openSheet(ProfileBottomSheetScreen.ChangeLanguageScreen)
        }

        NotificationLayout(

        )
    }
}

@Composable
private fun AdditionalLayout(
    modifier: Modifier = Modifier,
    openSheet: (ProfileBottomSheetScreen) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Subtitle(text = stringResource(id = R.string.additionally_subtitle))

        ClickableLayout(text = stringResource(id = R.string.rate_the_app)) {
            openSheet(ProfileBottomSheetScreen.RateTheAppScreen)
        }
    }
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text.uppercase(),
        color = Color(0xFF8B98A7),
        fontSize = 12.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp)
    )
}

@Composable
private fun ClickableLayout(
    modifier: Modifier = Modifier,
    text: String,
    language: String = "",
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.5.dp, bottom = 15.5.dp, start = 15.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color(0xFF1A1C1F)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = language,
                fontSize = 13.sp,
                color = Color(0xFF1A1C1F)
            )

            Image(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "",
                modifier = Modifier.padding(start = 13.33.dp, end = 19.dp)
            )
        }

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
private fun NotificationLayout(
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val switchState = rememberSaveable { mutableStateOf(viewModel.state.isNotificationsEnabled) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.5.dp, bottom = 15.5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.notifications),
                fontSize = 16.sp,
                color = Color(0xFF1A1C1F)
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = switchState.value,
                onCheckedChange = {
                    viewModel.onEvent(ProfileEvent.SetNotificationStatus(it))
                    switchState.value = it
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Blue500,
                    checkedThumbColor = Color.White
                ),
                modifier = Modifier
                    .padding(end = 25.dp)
                    .height(20.dp)
                    .width(12.dp)
            )
        }

        Divider(
            color = GrayBorder,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.7f)
        )
    }
}
