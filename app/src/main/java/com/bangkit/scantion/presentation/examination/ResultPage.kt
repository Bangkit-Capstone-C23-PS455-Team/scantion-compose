package com.bangkit.scantion.presentation.examination

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bangkit.scantion.model.CancerType
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.model.UserLog
import com.bangkit.scantion.navigation.HomeScreen
import kotlinx.coroutines.delay

@Composable
fun ResultPage(navController: NavHostController, userLog: UserLog, skinCase: SkinCase, isFromDetail: Boolean = false) {
    val name = userLog.name
    val uriHandler = LocalUriHandler.current
    val hospitalParamSearch = "rumah+sakit"

    val cancerTypes = CancerType.getData()
    var cancerType: CancerType? = null

    if (skinCase.cancerType in CancerType.listKey){
        cancerType = cancerTypes.getValue(skinCase.cancerType)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ColumnPartResult {
            TitleTextResult(text = "Detail Pemeriksaan")
            RowSpaceBetweenTwoText(
                modifier = Modifier.fillMaxWidth(),
                textFirst = skinCase.id,
                textSecond = ""
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
            TitleTextResult(text = "Hasil")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
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
                        PercentageCircleBox(accuracy = skinCase.accuracy, 110.dp, isFromDetail = isFromDetail)
                        Text(
                            text = skinCase.cancerType,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        if (cancerType != null){
            ResultSpacer()
            ColumnPartResult{
                TitleTextResult(text = "Penjelasan tentang ${cancerType.displayName}")
                Column {
                    Text(text = cancerType.desc, maxLines = 4)
                    Text(text = "...", maxLines = 1)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .alpha(0.3f)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Spacer(modifier = Modifier.fillMaxSize())
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    Text(
                        text = "Baca Selengkapnya",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            navController.navigate(HomeScreen.Explanation.createRoute(cancerType.displayName))
                        }
                    )
                }
            }
        }
        ResultSpacer()
        ColumnPartResult {
            TitleTextResult(text = "Keterangan Pasien")
            RowSpaceBetweenTwoText(
                modifier = Modifier.fillMaxWidth(),
                textFirst = "Bagian kulit",
                textSecond = skinCase.bodyPart,
                oneThirdFirst = true
            )
            RowSpaceBetweenTwoText(
                modifier = Modifier.fillMaxWidth(),
                textFirst = "Sejak",
                textSecond = skinCase.howLong,
                oneThirdFirst = true
            )
            RowSpaceBetweenTwoText(
                modifier = Modifier.fillMaxWidth(),
                textFirst = "Gejala",
                textSecond = skinCase.symptom,
                oneThirdFirst = true
            )
        }
        ResultSpacer()
        ColumnPartResult {
            Text(
                text = "Ingin memastikan pemeriksaan",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cari rumah sakit",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    uriHandler.openUri("https://www.google.co.id/maps/search/$hospitalParamSearch/")
                }
            )
        }
    }
}

@Composable
fun PercentageCircleBox(accuracy: Float, circleSize: Dp, strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth, isFromDetail: Boolean = false) {
    var animationProgress by rememberSaveable { mutableStateOf(false) }
    val animationDuration = 2500

    val currentPercentage by animateFloatAsState(
        targetValue = if(animationProgress) accuracy else 0f,
        animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
        label = ""
    )

    LaunchedEffect(key1 = true){
        delay(500)
        animationProgress = true
    }

    val percentage = if (isFromDetail) accuracy else currentPercentage

    Box(
        modifier = Modifier
            .size(circleSize)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = percentage,
            strokeWidth = strokeWidth,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "${(percentage * 100).toInt()}%",
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
    textSecond: String,
    oneThirdFirst: Boolean = false
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (oneThirdFirst) Arrangement.Start else Arrangement.SpaceBetween
    ) {
        Text(
            text = textFirst,
            style = MaterialTheme.typography.titleSmall,
            modifier = if (oneThirdFirst) Modifier.fillMaxWidth(.35f) else Modifier
        )
        Text(
            text = textSecond,
            style = MaterialTheme.typography.titleSmall,
            modifier = if (oneThirdFirst) Modifier.fillMaxWidth() else Modifier
        )
    }
}

@Composable
fun TitleTextResult(
    text: String
){
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}