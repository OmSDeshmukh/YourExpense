package com.example.expensetracker.presentation.Home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.expensetracker.MyApplication
import com.google.android.play.integrity.internal.c
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException


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
            setEnabledUseCases( CameraController.IMAGE_CAPTURE )
        }
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            uri?.let {
                val bitmap = uriToBitmap(context, it)
                if (bitmap != null) {
                    capturedImage = bitmap
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if( capturedImage != null ) {
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
        else {
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
                        modifier = Modifier
                            .size(64.dp)
                            .padding(2.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(bottom = 120.dp, end = 24.dp)
                    .size(60.dp)
                    .border(2.dp, Color.White, CircleShape)
                    .align(Alignment.BottomEnd)
            ) {
                IconButton(
                    onClick = { photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    ) },
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.Center)
                ) {
                    Icon(Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream).also {
            inputStream?.close()
        }
    } catch (e: IOException) {
        Log.e("CameraScreen", "Error converting URI to Bitmap", e)
        null
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