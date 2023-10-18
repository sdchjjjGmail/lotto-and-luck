package com.content.lottoandluck.presenter.screen.sharedcomposable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.presenter.viewmodel.sharedstate.ToastMessageState
import com.content.lottoandluck.utils.Body3
import dev.icerock.moko.resources.compose.colorResource

@Composable
fun SavedToastMessage() {
    AnimatedVisibility(
        visible = ToastMessageState.show.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke((0.5).dp, colorResource(MR.colors.black))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(colorResource(MR.colors.white))
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        ToastMessageState.hide()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "저장 되었어요!", style = Body3(), color = colorResource(MR.colors.black))
            }
        }
    }
}