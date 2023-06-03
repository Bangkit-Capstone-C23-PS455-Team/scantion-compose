package com.bangkit.scantion.presentation.examination

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bangkit.scantion.model.SkinCase
import kotlinx.coroutines.delay

@Composable
fun ResultPage(skinCase: SkinCase) {
    val name = "Alfachri Ghani"
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ColumnPartResult {
            Text(
                text = "Detail Pemeriksaan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            RowSpaceBetweenTwoText(
                modifier = Modifier.fillMaxWidth(),
                textFirst = "Nama",
                textSecond = name
            )
            RowSpaceBetweenTwoText(
                modifier = Modifier.fillMaxWidth(),
                textFirst = "Tanggal Pemeriksaan",
                textSecond = skinCase.dateCreated
            )
        }
        ResultSpacer()
        ColumnPartResult {
            Text(
                text = "Hasil",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(.5f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = skinCase.photoUri.ifEmpty { null },
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                            ){
                        PercentageCircleBox(accuracy = skinCase.accuracy, 110.dp)
                        Text(
                            text = skinCase.cancerType,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        ResultSpacer()
    }
}

@Composable
fun PercentageCircleBox(accuracy: Float, circleSize: Dp, strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth) {
    var accuracyInt by remember { mutableIntStateOf(0) }
    var animationProgress by remember { mutableStateOf(false) }

    val currentPercentage by animateFloatAsState(
        targetValue = if(animationProgress) accuracy else 0f,
        animationSpec = tween(durationMillis = 4000, easing = FastOutSlowInEasing)
    )

    val accuracyProgress by animateIntAsState(
        targetValue = accuracyInt,
        animationSpec = tween(
            durationMillis = 4000,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true){
        delay(4000)
        animationProgress=true
        accuracyInt = (accuracy * 100).toInt()
    }

    Box(
        modifier = Modifier
            .size(circleSize)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = currentPercentage,
            strokeWidth = strokeWidth,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "${accuracyProgress}%",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ResultSpacer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .alpha(0.3f)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Spacer(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ColumnPartResult(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        content()
    }
}

@Composable
fun RowSpaceBetweenTwoText(
    modifier: Modifier,
    textFirst: String,
    textSecond: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = textFirst,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = textSecond,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}