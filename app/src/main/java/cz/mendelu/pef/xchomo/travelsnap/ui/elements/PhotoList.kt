package cz.mendelu.pef.xchomo.travelsnap.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.addedit.ImageLoadState
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors

@Composable
fun PhotoList(photoUrls: List<String>) {

    val photoUrl: List<String> =
        if (photoUrls?.first() == null || photoUrls?.first() == "" || photoUrls?.first() == "string") {
            listOf("https://www.thermaxglobal.com/wp-content/uploads/2020/05/image-not-found.jpg")
        } else {
            photoUrls
        }
    val imageLoadState: MutableState<ImageLoadState> = remember {
        mutableStateOf(ImageLoadState.Success)
    }

    val defaultImageUrl =
        "https://www.thermaxglobal.com/wp-content/uploads/2020/05/image-not-found.jpg"

    val validPhotoUrls =
        photoUrls.map { if (it.isBlank() || it == "string") defaultImageUrl else it }

    val painters = validPhotoUrls.map { url ->
        url to rememberAsyncImagePainter(model = url)
    }.toMap()
    LazyRow(
        contentPadding = PaddingValues(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 16.dp)
    ) {
        photoUrl.forEach { photoUrl ->
            items(validPhotoUrls.size) { index ->

                val url = validPhotoUrls[index]
                val painter = painters[url]!!

                Card(
                    modifier = Modifier
                        .size(height = 280.dp, width = 320.dp)
                        .padding(end = 8.dp, start = 8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = AppColors.PrimaryColor),

                    ) {
                    if (imageLoadState.value == ImageLoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(top = 110.dp)
                                .align(Alignment.CenterHorizontally),
                            strokeWidth = 5.dp,
                            color = AppColors.AccentColor,
                        )
                    }
                    Image(
                        painter = painter,
                        contentDescription = "Place Photo",
                        modifier = Modifier
                            .size(height = 280.dp, width = 340.dp)
                            .padding(8.dp)
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(8.dp))
                            .testTag("PhotoList"),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
