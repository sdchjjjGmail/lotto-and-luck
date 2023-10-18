package com.content.lottoandluck.presenter.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.presenter.screen.sharedcomposable.SavedNumberManagerPopup
import com.content.lottoandluck.presenter.viewmodel.RecommendationViewModel
import com.content.lottoandluck.utils.Body1
import com.content.lottoandluck.utils.Body2
import com.content.lottoandluck.utils.ColorUtil
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource
import org.kodein.di.instance

@Composable
fun RecommendationScreen() {
    val viewModel: RecommendationViewModel by ViewModelFactory.viewModel.instance()
    viewModel.init()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        RecommendationScreenCard(viewModel)
    }
}

@Composable
fun RecommendationScreenCard(viewModel: RecommendationViewModel) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 68.dp, bottom = 60.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorResource(MR.colors.gray_border))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(.15f),
                    contentAlignment = Alignment.Center
                ) {
                    RecommendationScreenTitle(viewModel::startSimulation)
                }
                Box(
                    Modifier.weight(.55f),
                    contentAlignment = Alignment.Center
                ) {
                    SimulatedNumberList(viewModel)
                }
                Box(Modifier.weight(.3f)) {
                    NumberOptions(viewModel)
                }
            }
            SavedNumberManagerPopup { viewModel.save() }
        }
    }
}

@Composable
fun RecommendationScreenTitle(
    generate: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "시뮬레이터가 추천하는 번호",
            style = Body1(),
            color = colorResource(MR.colors.black),
        )
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(colorResource(MR.colors.white))
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(MR.images.icon_reset),
                contentDescription = "reset icon",
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    generate()
                }
            )
        }
    }
}

@Composable
fun SimulatedNumberList(viewModel: RecommendationViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        with(viewModel.simulatedNumberState) {
            forEach { value ->
                SimulatedNumberCards(number = value)
            }
        }
    }
}

@Composable
fun SimulatedNumberCards(number: Int) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .size(35.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, colorResource(MR.colors.black))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(ColorUtil.getColorCodeByNumber(number))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = Body2(),
                color = colorResource(MR.colors.white),
            )
        }
    }
}

@Composable
fun NumberOptions(viewModel: RecommendationViewModel) {
    AnimatedVisibility(
        visible = viewModel.isGenerated.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                SaveNumber(viewModel::askSave)
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                ShowStatistics(viewModel::showStatistics)
            }
        }
    }
}

@Composable
fun SaveNumber(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 37.dp, end = 37.dp, bottom = 5.dp)
            .fillMaxWidth()
            .height(37.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(30.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(MR.colors.blue_button)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "저장하기",
                style = Body2(),
                color = colorResource(MR.colors.white),
            )
        }
    }
}

@Composable
fun ShowStatistics(
    showStatistics: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 37.dp, end = 37.dp, top = 5.dp)
            .fillMaxWidth()
            .height(37.dp)
            .clickable {
                showStatistics()
            },
        shape = RoundedCornerShape(30.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(MR.colors.blue_button)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "통계보기",
                style = Body2(),
                color = colorResource(MR.colors.white),
            )
        }
    }
}