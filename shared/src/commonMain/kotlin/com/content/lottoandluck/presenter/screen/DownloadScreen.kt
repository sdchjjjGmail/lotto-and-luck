package com.content.lottoandluck.presenter.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.presenter.viewmodel.DownloadViewModel
import com.content.lottoandluck.presenter.viewmodel.sharedstate.DrawingDownloadState
import com.content.lottoandluck.utils.Body1
import com.content.lottoandluck.utils.Body2
import com.content.lottoandluck.utils.Caption
import dev.icerock.moko.resources.compose.colorResource
import org.kodein.di.instance


@Composable
fun DownloadScreen() {
    val viewModel: DownloadViewModel by ViewModelFactory.viewModel.instance()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Loading(viewModel)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DownloadPopup(viewModel)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Progress(viewModel)
    }
}

@Composable
fun Loading(downloadViewModel: DownloadViewModel) {
    AnimatedVisibility(
        visible = downloadViewModel.loading.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CircularProgressIndicator(
            color = colorResource(MR.colors.yellow_progress_stroke)
        )
    }
}

@Composable
fun DownloadPopup(viewModel: DownloadViewModel) {
    AnimatedVisibility(
        visible = DrawingDownloadState.showPopup.value,
        enter = slideInVertically(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Card(
            modifier = Modifier.size(width = 250.dp, height = 230.dp)
                .background(colorResource(MR.colors.popup_background)),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, colorResource(MR.colors.gray_border))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (DrawingDownloadState.initial.value) {
                        "이전 회차 다운로드가 필요해요."
                    } else {
                        "새로운 회차가 있습니다."
                    },
                    style = Body2(),
                    color = colorResource(MR.colors.black),
                    textAlign = TextAlign.Center
                )
                if (DrawingDownloadState.initial.value) {
                    Text(
                        text = "(회차에 따라 1~2분이 소요됩니다.)",
                        style = Caption(),
                        color = colorResource(MR.colors.black),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                PopupOptions(viewModel)
            }
        }
    }
}

@Composable
fun PopupOptions(viewModel: DownloadViewModel) {
    Column(
        modifier = Modifier.background(colorResource(MR.colors.transparent))
    ) {
        OkButton(viewModel::ok)
        Spacer(modifier = Modifier.height(15.dp))
        CancelButton { viewModel.cancel() }
    }
}

@Composable
fun OkButton(
    okClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { okClicked() },
        shape = RoundedCornerShape(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 90.dp, height = 40.dp)
                .background(colorResource(MR.colors.blue_popup_button)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "다운로드",
                style = Body2(),
                color = colorResource(MR.colors.white)
            )
        }
    }
}

@Composable
fun CancelButton(
    cancelClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { cancelClicked() },
        shape = RoundedCornerShape(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 90.dp, height = 40.dp)
                .background(colorResource(MR.colors.blue_popup_button)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "뒤로가기",
                style = Body2(),
                color = colorResource(MR.colors.white)
            )
        }
    }
}

@Composable
fun Progress(viewModel: DownloadViewModel) {
    AnimatedVisibility(
        visible = viewModel.showProgress.value,
        exit = fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DownloadingText()
            Spacer(modifier = Modifier.height(40.dp))
            ProgressBar(viewModel)
            Spacer(modifier = Modifier.height(40.dp))
            CurrentPercentage(viewModel)
        }
    }
}

@Composable
fun DownloadingText() {
    Text(
        text = "다운로드중",
        style = Body1(),
        color = colorResource(MR.colors.black)
    )
}

//@Composable
//fun ProgressBar(viewModel: DownloadViewModel) {
//    Box(
//        modifier = Modifier.size(120.dp).clip(CircleShape)
//            .background(colorResource(MR.colors.light_grey_progress_background)),
//        contentAlignment = Alignment.Center
//    ) {
//        Box(
//            modifier = Modifier
//                .clip(CircleShape)
//                .size(76.dp)
//                .background(colorResource(MR.colors.white))
//        )
//        CircularProgressIndicator(
//            modifier = Modifier.fillMaxSize(),
//            progress = viewModel.progress.value,
//            color = colorResource(MR.colors.yellow_progress_stroke),
//            strokeWidth = 22.dp,
//        )
//    }
//}

@Composable
fun ProgressBar(viewModel: DownloadViewModel) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(colorResource(MR.colors.yellow_progress_stroke)),
        contentAlignment = Alignment.Center
    ) {
        ProgressInner(viewModel)
    }
}

@Composable
fun ProgressInner(viewModel: DownloadViewModel) {
    Box(
        modifier = Modifier
            .size((120 - (120 * (viewModel.progress.value / 100))).dp)
            .clip(CircleShape)
            .background(colorResource(MR.colors.white)),
    )
}

@Composable
fun CurrentPercentage(viewModel: DownloadViewModel) {
    Text(
        text = viewModel.percentage.value,
        style = Body1(),
        color = colorResource(MR.colors.black)
    )
}
