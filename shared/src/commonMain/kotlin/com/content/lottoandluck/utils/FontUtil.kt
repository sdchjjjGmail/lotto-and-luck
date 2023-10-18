package com.content.lottoandluck.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.content.lottoandluck.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun MainFontBold(): FontFamily {
    return fontFamilyResource(MR.fonts.SpoqaHanSansNeo.bold)
}

@Composable
fun MainFontRegular(): FontFamily {
    return fontFamilyResource(MR.fonts.SpoqaHanSansNeo.regular)
}

// Set of Material typography styles to start with
@Composable
fun H1(): TextStyle {
    return TextStyle(
        fontFamily = MainFontBold(),
        fontSize = 21.sp,
    )
}

@Composable
fun Body1(): TextStyle {
    return TextStyle(
        fontFamily = MainFontBold(),
        fontSize = 18.sp
    )
}

@Composable
fun Body2(): TextStyle {
    return TextStyle(
        fontFamily = MainFontBold(),
        fontSize = 16.sp,
    )
}

@Composable
fun Body3(): TextStyle {
    return TextStyle(
        fontFamily = MainFontRegular(),
        fontSize = 14.sp,
    )
}

@Composable
fun Caption(): TextStyle {
    return TextStyle(
        fontFamily = MainFontBold(),
        fontSize = 14.sp
    )
}

@Composable
fun Caption2(): TextStyle {
    return TextStyle(
        fontFamily = MainFontBold(),
        fontSize = 12.sp
    )
}
