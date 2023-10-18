package com.content.lottoandluck.presenter.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.presenter.screen.sharedcomposable.SavedNumberManagerPopup
import com.content.lottoandluck.presenter.viewmodel.RandomViewModel
import com.content.lottoandluck.utils.Body1
import com.content.lottoandluck.utils.Body2
import com.content.lottoandluck.utils.Caption
import com.content.lottoandluck.utils.ColorUtil
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource
import org.kodein.di.instance

@Composable
fun RandomScreen() {
    val viewModel: RandomViewModel by ViewModelFactory.viewModel.instance()
    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                viewModel.closeExpand()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize().weight(.8f),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.weight(.2f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        GenerateCount(viewModel)
                    }
                    Box(
                        modifier = Modifier.weight(.2f),

                        contentAlignment = Alignment.Center
                    ) {
                        GeneratedNumberAndReset(viewModel::reset)
                    }
                    Box(
                        modifier = Modifier.weight(.6f),
                        contentAlignment = Alignment.Center
                    ) {
                        GeneratedNumberList(viewModel = viewModel)
                    }
                }
            }
            Box(
                modifier = Modifier.weight(.2f),
                contentAlignment = Alignment.TopCenter
            ) {
                ActivatedButton(viewModel)
            }
        }
        SavedNumberManagerPopup { viewModel.save() }
        GenerateCountDropDown(viewModel)
    }
}

@Composable
fun GenerateCount(
    viewModel: RandomViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "생성 횟수",
            style = Caption(),
            color = colorResource(MR.colors.black)
        )
        GenerateCountSelectMenu(viewModel)
    }
}

@Composable
fun GenerateCountSelectMenu(
    viewModel: RandomViewModel
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .size(50.dp, 20.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable { viewModel.dropDownExpand() },
    ) {
        Text(
            text = "${viewModel.generateCount.value}회",
            style = Body2(),
            modifier = Modifier.padding(start = 10.dp)
        )
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun GenerateCountDropDown(viewModel: RandomViewModel) {
    AnimatedVisibility(
        visible = viewModel.dropDownExpanded.value,
        enter = expandHorizontally(expandFrom = Alignment.CenterHorizontally),
        exit = shrinkOut(shrinkTowards = Alignment.Center)
    ) {
        Card(
            modifier = Modifier.width(100.dp).wrapContentHeight(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(.5.dp, colorResource(MR.colors.gray_border))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 1..5) {
                    CountItem(
                        viewModel::countSelected,
                        i
                    )
                }
            }
        }
    }
}

@Composable
fun CountItem(
    selectCount: (Int) -> Unit,
    i: Int
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .clickable {
                selectCount(i)
            },
        elevation = 0.dp
    ) {
        Text(
            text = "${i}회",
            style = Caption(),
            color = colorResource(MR.colors.black)
        )
    }
}

@Composable
fun GeneratedNumberAndReset(reset: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .background(colorResource(MR.colors.white))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(MR.images.icon_reset),
                contentDescription = "reset icon",
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    reset()
                }
            )
        }
        Text(
            text = "생성된 번호",
            style = Body1(),
            color = colorResource(MR.colors.black)
        )
    }
}

@Composable
fun GeneratedNumberList(viewModel: RandomViewModel) {
    LazyColumn {
        items(viewModel.generatedListState) {
            NumberList(it)
            Spacer(modifier = Modifier.height(7.dp))
        }
    }
}

@Composable
fun NumberList(numberList: List<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        with(numberList) {
            forEach { value ->
                NumberCards(number = value)
            }
        }
    }
}

@Composable
fun NumberCards(number: Int) {
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
fun ActivatedButton(viewModel: RandomViewModel) {
    if (!viewModel.isGenerated.value) {
        Box(
            modifier = Modifier.padding(vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            GenerateButton(viewModel::generateRandomNumber)
        }
    } else {
        Box(
            modifier = Modifier.padding(vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            SaveButton(viewModel::askSave)
        }
    }
}

@Composable
fun GenerateButton(
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .size(width = 110.dp, height = 40.dp)
                .background(colorResource(MR.colors.blue_button)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "생성",
                style = Body1(),
                color = colorResource(MR.colors.white)
            )
        }
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 110.dp, height = 40.dp)
                .background(colorResource(MR.colors.blue_button)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "저장",
                style = Body1(),
                color = colorResource(MR.colors.white)
            )
        }
    }
}