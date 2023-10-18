package com.content.lottoandluck.presenter.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.presenter.viewmodel.StatisticsViewModel
import com.content.lottoandluck.presenter.viewmodel.sharedstate.StatisticsState
import com.content.lottoandluck.utils.Body2
import com.content.lottoandluck.utils.Caption
import com.content.lottoandluck.utils.Caption2
import com.content.lottoandluck.utils.ColorUtil
import dev.icerock.moko.resources.compose.colorResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.instance

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsScreen() {
    val viewModel: StatisticsViewModel by ViewModelFactory.viewModel.instance()
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Pager(pagerState, viewModel)
        PageMoveButtons(pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(
    pagerState: PagerState,
    viewModel: StatisticsViewModel
) {
    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect {
            viewModel.clearSelectedNumber()
        }
    }
    HorizontalPager(
        pageCount = StatisticsState.pageCount.value,
        state = pagerState
    ) {
        SinglePage(
            it,
            viewModel,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageMoveButtons(
    pagerState: PagerState
) {
    AnimatedVisibility(
        modifier = Modifier.padding(bottom = 33.dp),
        visible = (StatisticsState.numbers.count() > 1),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrevPage(coroutineScope, pagerState, pagerState.currentPage)
            NextPage(coroutineScope, pagerState, pagerState.currentPage)
        }
    }
}

@Composable
fun SinglePage(
    index: Int,
    viewModel: StatisticsViewModel,
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.2f),
            contentAlignment = Alignment.Center
        ) {
            ShowMoreButton(viewModel::showMore)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f),
            contentAlignment = Alignment.Center
        ) {
            MainStatisticsScreen(
                index,
                viewModel
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.2f),
            contentAlignment = Alignment.Center
        ) {
            ExitButton(viewModel::exit)
        }
    }
}

@Composable
fun MainStatisticsScreen(
    index: Int,
    viewModel: StatisticsViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            StatisticsNumberList(
                viewModel,
                StatisticsState.numbers[index]
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Detail(viewModel)
        Spacer(modifier = Modifier.height(10.dp))
        MainStatisticsResult(viewModel)
    }
}

@Composable
fun StatisticsNumberList(
    viewModel: StatisticsViewModel,
    numberList: List<Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        with(numberList) {
            forEach { value ->
                StatisticsNumberCards(
                    viewModel::onNumberClicked,
                    number = value
                )
            }
        }
    }
}

@Composable
fun StatisticsNumberCards(
    numberClicked: (Int) -> Unit,
    number: Int
) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .size(40.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { numberClicked(number) },
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
fun MainStatisticsResult(
    viewModel: StatisticsViewModel
) {
    AnimatedVisibility(
        visible = !viewModel.showDetail.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Text(text = viewModel.totalPopPercentage.value, style = Caption())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrevPage(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    index: Int
) {
    Card(
        modifier = Modifier
            .padding(start = 30.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                coroutineScope.launch { pagerState.animateScrollToPage(index - 1) }
            }
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "<",
                style = Caption(),
                color = colorResource(MR.colors.dark_gray_background)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NextPage(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    index: Int
) {
    Card(
        modifier = Modifier
            .padding(end = 30.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                coroutineScope.launch { pagerState.animateScrollToPage(index + 1) }
            }
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        )
        {
            Text(
                text = ">",
                style = Caption(),
                color = colorResource(MR.colors.dark_gray_background)
            )
        }
    }
}

@Composable
fun ShowMoreButton(
    showMore: () -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { showMore() },
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, colorResource(MR.colors.gray_border))
    ) {
        Box(
            modifier = Modifier.size(width = 110.dp, height = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "더보기",
                style = Body2(),
                color = colorResource(MR.colors.black)
            )
        }
    }
}

@Composable
fun Detail(
    viewModel: StatisticsViewModel
) {
    AnimatedVisibility(
        visible = viewModel.showDetail.value,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = Modifier.background(colorResource(MR.colors.yellow_background))
                .size(width = 300.dp, height = 400.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(.1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "선택 숫자: ${viewModel.selectedNumber.value}",
                        style = Caption2(),
                        color = colorResource(MR.colors.gray)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth().weight(.9f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.totalPopPercentage.value,
                            style = Caption2(),
                            color = colorResource(MR.colors.gray)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = viewModel.totalBonusPopPercentage.value,
                            style = Caption2(),
                            color = colorResource(MR.colors.gray)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = viewModel.lastPopState.value,
                            style = Caption2(),
                            color = colorResource(MR.colors.gray)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = viewModel.skipRoundPattern.value,
                            style = Caption2(),
                            color = colorResource(MR.colors.gray)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExitButton(
    exit: () -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { exit() },
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, colorResource(MR.colors.gray_border))
    ) {
        Box(
            modifier = Modifier.size(width = 110.dp, height = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "나가기",
                style = Body2(),
                color = colorResource(MR.colors.black)
            )
        }
    }
}