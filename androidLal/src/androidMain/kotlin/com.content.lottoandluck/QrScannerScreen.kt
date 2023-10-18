import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.content.lottoandluck.BarcodeAnalyzer
import com.content.lottoandluck.nativespecific.qr.QrScannerStateViewModel
import java.util.concurrent.Executors
import com.content.lottoandluck.R
import com.content.lottoandluck.mainFont
import com.content.lottoandluck.nativespecific.qr.QrScannerEvent


@ExperimentalGetImage
@Composable
fun QrScanScreen(
    qrScannerStateViewModel: QrScannerStateViewModel,
    moveToContent: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(.2f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .weight(.6f),
        ) {
            AndroidView(
                { context ->
                    val cameraExecutor = Executors.newSingleThreadExecutor()
                    val previewView = PreviewView(context).also {
                        it.scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                        val imageCapture = ImageCapture.Builder().build()

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { content ->
                                    moveToContent(content)
                                })
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            // Unbind use cases before rebinding
                            cameraProvider.unbindAll()

                            // Bind use cases to camera
                            cameraProvider.bindToLifecycle(
                                context as ComponentActivity,
                                cameraSelector,
                                preview,
                                imageCapture,
                                imageAnalyzer
                            )

                        } catch (e: Exception) {
                            Log.e("QrScanScreen", "Use case binding failed", e)
                        }
                    }, ContextCompat.getMainExecutor(context))
                    previewView
                },
            )
        }
        Box(
            modifier = Modifier.weight(.2f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 40.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        colorResource(id = R.color.yellow_background)
                    )
                    .clickable {
                        qrScannerStateViewModel.onEvent(QrScannerEvent.CloseQrScreenScanner)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "나가기",
                    fontSize = 18.sp,
                    fontFamily = mainFont,
                    color = colorResource(id = R.color.black)
                )
            }
        }
    }
}