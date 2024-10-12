package com.example.expensetracker.presentation.Home

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.MyApplication
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun CameraScreenRoute(
    navigator: DestinationsNavigator
) {
    CameraScreen(
        navigator = navigator
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CameraScreen(
    navigator : DestinationsNavigator
) {
    val viewModel : HomeScreenViewModel = hiltViewModel()

    val context = MyApplication.instance

    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases( CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if( capturedImage != null )
        {
            ImagePreview(
                bitmap = capturedImage!!,
                onSave = {
                    Log.d("Camera", "Image saved!")
                    viewModel.sendPhoto(capturedImage!!)
                    capturedImage = null
                    navigator.navigateUp()
                },
                onCancel = { capturedImage = null }
            )
        }
        else
        {
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .padding(bottom = 116.dp)
                    .size(72.dp)
                    .border(2.dp, Color.White, CircleShape)
                    .align(Alignment.BottomCenter)
            ) {
                IconButton(
                    onClick = {
                        controller.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: ImageProxy) {
                                    super.onCaptureSuccess(image)
                                    val matrix = Matrix().apply {
                                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                                    }
                                    val rotatedBitmap = Bitmap.createBitmap(
                                        image.toBitmap(),
                                        0,
                                        0,
                                        image.width,
                                        image.height,
                                        matrix,
                                        true
                                    )
                                    capturedImage = rotatedBitmap
                                    image.close()
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    super.onError(exception)
                                    Log.e("Camera", "Couldn't take photo: ", exception)
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp).padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ImagePreview(
    bitmap: Bitmap,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(600.dp)
                .padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onCancel() }
            )
            Icon(
                Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onSave() }
            )
        }
    }
}