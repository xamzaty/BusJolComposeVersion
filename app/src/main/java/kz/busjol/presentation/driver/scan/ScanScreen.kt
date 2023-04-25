package kz.busjol.presentation.driver.scan

import android.Manifest
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.Loader
import kz.busjol.presentation.destinations.PassengerVerificationScreenDestination
import kz.busjol.utils.QrCodeAnalyzer

@Destination
@Composable
fun ScanScreen(
    navigator: DestinationsNavigator,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var hasCamPermission by rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCamPermission = granted }
    )
    val scope = rememberCoroutineScope()
    val isUserCanScanQr = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
        onDispose { }
    }

    LaunchedEffect(state.isTicketValid) {
        if (state.isTicketValid) {
            scope.launch {
                navigator.navigate(PassengerVerificationScreenDestination(
                    ticketInfo = state.ticketInfo
                ))
            }
        }
    }

    if (state.isLoading) {
        Loader(isDialogVisible = true)
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppBar(title = stringResource(id = R.string.scan_title)) {
                scope.launch {
                    navigator.navigateUp()
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (hasCamPermission) {
                    MyCameraPreview(
                        lifecycleOwner = lifecycleOwner,
                        cameraProviderFuture = cameraProviderFuture,
                        modifier = Modifier.fillMaxSize(),
                        onQrCodeDetected = { result ->
                            scope.launch {
                                if (isUserCanScanQr.value) {
                                    viewModel.onEvent(
                                        ScanEvent.CheckTicket(qrCode = result)
                                    )
                                    isUserCanScanQr.value = false
                                    delay(5000)
                                    isUserCanScanQr.value = true
                                    if (!state.isTicketValid) {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Неправильный QR-код"
                                        )
                                    }
                                }

                                if (state.error?.isNotEmpty() == true) {
                                    scaffoldState.snackbarHostState.showSnackbar(state.error)
                                }
                            }
                        },
                    )

                    ScanOverlay()
                }
            }
        }

        LaunchedEffect(state.error, state.isTicketValid) {
            if (state.error?.isNotEmpty() == true && !state.isTicketValid) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(state.error)
                }
            }
        }
    }
}

@Composable
fun ScanOverlay() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.camera_frame),
            contentDescription = "camera_frame"
        )

        Text(
            text = stringResource(id = R.string.point_the_camera),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 18.dp)
        )
    }
}

@Composable
private fun MyCameraPreview(
    lifecycleOwner: LifecycleOwner,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    onQrCodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(
                    Size(
                        previewView.width,
                        previewView.height
                    )
                )
                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                QrCodeAnalyzer { result ->
                    onQrCodeDetected(result)
                }
            )
            try {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            previewView
        },
        modifier = modifier.fillMaxSize()
    )
}