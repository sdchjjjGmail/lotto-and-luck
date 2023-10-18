package com.content.lottoandluck.presenter.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.domain.model.SavedLotteryNumber
import com.content.lottoandluck.presenter.screen.sharedcomposable.SavedNumberManagerPopup
import com.content.lottoandluck.presenter.viewmodel.SaveViewModel
import com.content.lottoandluck.presenter.viewmodel.sharedstate.SavedNumberManagerPopupState
import com.content.lottoandluck.utils.Body1
import com.content.lottoandluck.utils.Body2
import dev.icerock.moko.resources.compose.colorResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.instance


@Composable
fun SaveScreen() {
    val viewModel: SaveViewModel by ViewModelFactory.viewModel.instance()
    viewModel.unselectAll()
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.1f)
                .padding(horizontal = 20.dp)
        ) {
            TopButtons(viewModel)
        }
        Box(modifier = Modifier.weight(.9f)) {
            NumbersScreen(viewModel)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SavedNumberManagerPopup { viewModel.delete() }
    }
    Loading(viewModel)
}

@Composable
fun Loading(
    viewModel: SaveViewModel
) {
    AnimatedVisibility(
        visible = viewModel.loading.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(MR.colors.yellow_progress_stroke)
            )
        }
    }
}

@Composable
fun TopButtons(
    viewModel: SaveViewModel
) {
    AnimatedVisibility(
        visible = viewModel.selectedCount.value != 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                BackButton(viewModel::unselectAll)
            }
            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                StatisticsButton(viewModel::showStatistics)
            }
        }
    }
}

@Composable
fun BackButton(
    unselect: () -> Unit
) {
    Box(
        modifier = Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            unselect()
        }
    ) {
        Text(
            text = "취소",
            style = Body1(),
            color = colorResource(MR.colors.black),
        )
    }
}

@Composable
fun StatisticsButton(
    statistics: () -> Unit
) {
    Box(
        modifier = Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            statistics()
        }
    ) {
        Text(
            text = "통계",
            style = Body1(),
            color = colorResource(MR.colors.black),
        )
    }
}

@Composable
fun NumbersScreen(viewModel: SaveViewModel) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 60.dp),
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
                    SaveScreenTitle()
                }
                Box(Modifier.weight(.55f)) {
                    SavedLotteryNumberList(viewModel)
                }
                Box(Modifier.weight(.3f)) {
                    OptionButtons(viewModel)
                }
            }
        }
    }
}

@Composable
fun SaveScreenTitle() {
    Text(
        text = "내가 저장한 번호",
        style = Body1(),
        color = colorResource(MR.colors.black),
    )
}

@Composable
fun SavedLotteryNumberList(viewModel: SaveViewModel) {
    LazyColumn {
        items(viewModel.savedNumberListState) {
            SingleSavedLotteryNumber(
                viewModel,
                viewModel::numberSelected,
                it
            )
        }
    }
}

@Composable
fun SingleSavedLotteryNumber(
    viewModel: SaveViewModel,
    select: (SavedLotteryNumber) -> Unit,
    lotteryNumber: SavedLotteryNumber
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                select(lotteryNumber)
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        lotteryNumber.numberList.forEach {
            SavedNumberSelector(it, lotteryNumber.id, viewModel)
        }
    }
}

@Composable
fun SavedNumberSelector(
    number: Int,
    id: String,
    viewModel: SaveViewModel
) {
    with(viewModel.selectionMapState[id] ?: false) {
        if (this@with) {
            SelectedSavedNumberCard(number)
        } else {
            SavedNumberCard(number)
        }
    }
}

@Composable
fun SavedNumberCard(
    number: Int,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .size(width = 37.dp, height = 35.dp),
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(
            1.dp,
            colorResource(
                MR.colors.grey_card_border
            )
        ),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(7.dp))
                .background(
                    colorResource(
                        MR.colors.transparent
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = Body1(),
                color = Color.Black,
            )
        }
    }
}

@Composable
fun SelectedSavedNumberCard(
    number: Int,
) {
    val angleOffset = 4f
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = -angleOffset,
        targetValue = angleOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 400,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .size(width = 37.dp, height = 35.dp)
            .rotate(
                if (SavedNumberManagerPopupState.animation.value) {
                    angle
                } else {
                    0f
                }
            ),
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(
            1.dp,
            colorResource(
                MR.colors.transparent
            )
        ),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(7.dp))
                .background(
                    colorResource(
                        MR.colors.yellow_number_card_background
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = Body1(),
                color = Color.Black,
            )
        }
    }
}

@Composable
fun OptionButtons(viewModel: SaveViewModel) {
    Column {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            DeleteSelectedButton(viewModel::askDelete)
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            DeleteAllButton(viewModel::askDeleteAll)
        }
    }
}

@Composable
fun DeleteSelectedButton(
    deleteSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 37.dp, end = 37.dp, bottom = 5.dp)
            .fillMaxWidth()
            .height(37.dp)
            .clickable {
                deleteSelected()
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
                text = "선택된 번호 삭제",
                style = Body2(),
                color = colorResource(MR.colors.white),
            )
        }
    }
}

@Composable
fun DeleteAllButton(
    deleteAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 37.dp, end = 37.dp, top = 5.dp)
            .fillMaxWidth()
            .height(37.dp)
            .clickable {
                deleteAll()
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
                text = "모두 삭제",
                style = Body2(),
                color = colorResource(MR.colors.white),
            )
        }
    }
}