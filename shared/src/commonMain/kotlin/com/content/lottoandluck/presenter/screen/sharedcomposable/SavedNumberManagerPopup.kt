package com.content.lottoandluck.presenter.screen.sharedcomposable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.presenter.viewmodel.sharedstate.SavedNumberManagerPopupState
import com.content.lottoandluck.utils.Caption
import dev.icerock.moko.resources.compose.colorResource

@Composable
fun SavedNumberManagerPopup(
    onClickYes: () -> Unit
) {
    AnimatedVisibility(
        visible = SavedNumberManagerPopupState.showPopup.value,
        enter = expandVertically(expandFrom = Alignment.CenterVertically),
        exit = fadeOut(),
    ) {
        Card(
            modifier = Modifier.size(width = 225.dp, height = 100.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 10.dp,
        ) {
            Column {
                Box(
                    modifier = Modifier.weight(.45f)
                        .fillMaxSize()
                        .background(colorResource(MR.colors.blue_popup_button)),
                    contentAlignment = Alignment.Center
                ) {
                    PopupMessage()
                }
                Box(modifier = Modifier.weight(.55f)) {
                    PopupOptions(onClickYes)
                }
            }
        }
    }
}

@Composable
fun PopupMessage() {
    Text(
        text = "번호를 ${SavedNumberManagerPopupState.popupAct.value} 할까요?",
        style = Caption(),
        color = colorResource(MR.colors.white_popup_button_text)
    )
}

@Composable
fun PopupOptions(
    onClickYes: () -> Unit
) {
    Row {
        Box(
            modifier = Modifier
                .weight(.5f)
                .fillMaxSize()
                .background(colorResource(MR.colors.white_popup_button_background))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    SavedNumberManagerPopupState.hidePopup()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "뒤로",
                style = Caption(),
                color = colorResource(MR.colors.line_decoration)
            )
        }
        Divider(
            modifier = Modifier.fillMaxHeight().width(0.5.dp),
            color = colorResource(MR.colors.line_decoration),
        )
        Box(
            modifier = Modifier
                .weight(.5f)
                .fillMaxSize()
                .background(colorResource(MR.colors.white_popup_button_background))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    onClickYes()
                    SavedNumberManagerPopupState.hidePopup()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "네",
                style = Caption(),
                color = colorResource(MR.colors.line_decoration)
            )
        }
    }
}