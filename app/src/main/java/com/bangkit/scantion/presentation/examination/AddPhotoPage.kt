package com.bangkit.scantion.presentation.examination

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.ImageFileProvider

@Composable
fun AddPhotoPage(
    context: Context,
    photoUri: Uri?,
    hasImage: Boolean,
    onPhotoUriChange: (Uri) -> Unit,
    onHasImageChange: (Boolean) -> Unit,
    backCallbackEnabled: MutableState<Boolean>
) {
    var tempUri: Uri? = null
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            onPhotoUriChange.invoke(tempUri!!)
            onHasImageChange.invoke(true)
            backCallbackEnabled.value = true
        } else if (hasImage) {
            backCallbackEnabled.value = true
        }

    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uriResult ->
        if (photoUri != uriResult && uriResult != null) {
            onPhotoUriChange.invoke(uriResult)
            onHasImageChange.invoke(true)
            backCallbackEnabled.value = true
        } else if (hasImage) {
            backCallbackEnabled.value = true
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = if (hasImage) photoUri else null,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Crop,

                )
            if (!hasImage) {
                Text(
                    text = "Kosong",
                    style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(.5f)) {
                ScantionButton(
                    modifier = Modifier.fillMaxWidth().padding(end = 5.dp),
                    onClick = {
                        tempUri = ImageFileProvider.getImageUri(context)
                        takePictureLauncher.launch(tempUri)
                    },
                    text = "Kamera",
                    outlineButton = hasImage
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                ScantionButton(
                    modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        ) },
                    text = "Galeri",
                    outlineButton = hasImage
                )
            }
        }
    }
}