package com.content.lottoandluck.presenter.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.content.lottoandluck.MR
import com.content.lottoandluck.nativespecific.qr.QrScannerEvent
import com.content.lottoandluck.permissions.model.Permission
import com.content.lottoandluck.permissions.model.PermissionState
import com.content.lottoandluck.permissions.service.PermissionsService
import com.content.lottoandluck.presenter.viewmodel.SearchViewModel
import com.content.lottoandluck.nativespecific.qr.QrScannerStateViewModel
import com.content.lottoandluck.utils.Body1
import com.content.lottoandluck.utils.Body2
import com.content.lottoandluck.utils.Body3
import com.content.lottoandluck.utils.ColorUtil
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    permissionsService: PermissionsService,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                focusManager.clearFocus()
                viewModel.closeDropDown()
            },
        contentAlignment = Alignment.Center
    ) {
        Column {
            Box(
                Modifier.weight(.5f),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    SearchTab(viewModel, permissionsService, qrScannerStateViewModel)
                }
            }
            Box(
                Modifier.weight(.5f),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SearchFailMessage(viewModel)
                    SearchResult(viewModel)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    DrawingRoundDropDown(viewModel)
                }
            }
        }
    }
}

@Composable
fun SearchTab(
    viewModel: SearchViewModel,
    permissionsService: PermissionsService,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle()
        SearchBar(viewModel, permissionsService, qrScannerStateViewModel)
    }
}

@Composable
fun ScreenTitle() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "로또당첨번호 조회",
            style = Body1(),
            color = colorResource(MR.colors.black),
        )
    }
}

@Composable
fun SearchBar(
    viewModel: SearchViewModel,
    permissionsService: PermissionsService,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    Row(
        modifier = Modifier
            .padding(vertical = 20.dp)
    ) {
        SearchIcon()
        SearchTextField(viewModel::search)
        QrIcon(permissionsService, qrScannerStateViewModel)
    }
}

@Composable
fun SearchIcon() {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(colorResource(MR.colors.light_gray))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(MR.images.icon_search),
            contentDescription = "search icon"
        )
    }
}

@Composable
fun SearchTextField(
    search: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val textState = remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .size(width = 225.dp, height = 50.dp)
            .background(colorResource(MR.colors.light_gray)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = textState.value,
            onValueChange = {
                if (it == "" || it.toIntOrNull() != null) {
                    if (it.length <= 4) {
                        textState.value = it
                    }
                    search(textState.value)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    search(textState.value)
                }
            ),
            textStyle = Body3(),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(colorResource(MR.colors.light_gray))
        )
    }
}

@Composable
fun QrIcon(
    permissionsService: PermissionsService,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val permissionState by permissionsService.checkPermissionFlow(Permission.CAMERA)
        .collectAsState(permissionsService.checkPermission(Permission.CAMERA))

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(colorResource(MR.colors.light_gray))
            .padding(10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                coroutineScope.launch {
                    when (permissionState) {
                        PermissionState.NOT_DETERMINED -> permissionsService.providePermission(
                            Permission.CAMERA
                        )

                        PermissionState.DENIED -> permissionsService.openSettingPage(Permission.CAMERA)
                        else -> qrScannerStateViewModel.onEvent(QrScannerEvent.OpenQrScreenScanner)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(MR.images.icon_qr_code),
            contentDescription = "qr code icon"
        )
    }
}

@Composable
fun DrawingRoundDropDown(viewModel: SearchViewModel) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { viewModel.dropDownExpand() },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .wrapContentHeight()
                    .border(
                        BorderStroke(1.dp, Color.LightGray),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(3.dp)
            ) {
                Text(
                    text = viewModel.resultDisplay.value,
                    style = Body2(),
                    modifier = Modifier.padding(start = 10.dp)
                )
                Icon(
                    Icons.Filled.ArrowDropDown, "contentDescription",
                    Modifier.align(Alignment.CenterEnd)
                )
            }
            ExpandedDropdown(viewModel)
        }
    }
}

@Composable
fun ExpandedDropdown(viewModel: SearchViewModel) {
    AnimatedVisibility(
        visible = viewModel.dropDownExpanded.value,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .width(250.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
                .background(colorResource(MR.colors.white)),
        ) {
            Column() {
                viewModel.dropDownItemList.forEach { item ->
                    DropDownItem(round = item, viewModel::search)
                }
            }
        }
    }
}

@Composable
fun DropDownItem(
    round: Int,
    itemClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 10.dp, top = 3.dp, bottom = 3.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                itemClicked(round.toString())
            }
    ) {
        Text(
            text = "${round}회",
            style = Body2()
        )
    }
}

@Composable
fun SearchResult(viewModel: SearchViewModel) {
    AnimatedVisibility(
        visible = !viewModel.searchResult.value.numberList.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DrawingNumbers(viewModel.searchResult.value.numberList)
            BonusNumber(viewModel.searchResult.value.bonusNumber)
        }
    }
}

@Composable
fun DrawingNumbers(list: List<Int>) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
    ) {
        list.forEachIndexed { _, i ->
            ResultNumberCards(number = i)
        }
    }
}

@Composable
fun ResultNumberCards(number: Int) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .size(35.dp),
        shape = RoundedCornerShape(100.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(ColorUtil.getColorCodeByNumber(number))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (number <= 0) {
                    ""
                } else {
                    number.toString()
                },
                style = Body1(),
                color = colorResource(MR.colors.white),
            )
        }
    }
}

@Composable
fun BonusNumber(number: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlusSign()
        ResultNumberCards(number = number)
    }
}

@Composable
fun PlusSign() {
    Box(
        modifier = Modifier.size(width = 30.dp, height = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            fontSize = 21.sp,
            color = colorResource(MR.colors.light_gray),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SearchFailMessage(viewModel: SearchViewModel) {
    AnimatedVisibility(
        visible = viewModel.searchResult.value.round == -1,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "검색 결과가 없습니다.",
                style = Body1(),
                color = colorResource(MR.colors.black)
            )
        }
    }
}