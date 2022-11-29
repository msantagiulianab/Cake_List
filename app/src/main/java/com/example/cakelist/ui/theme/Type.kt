package com.example.cakelist.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cakelist.R

val fontsPoppins = FontFamily(
    Font(R.font.poppins_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.poppins_regular, weight = FontWeight.Normal)
)

val fontsBebas = FontFamily(
    Font(R.font.bebas_neue_regular, weight = FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h4 = TextStyle(
        fontFamily = fontsPoppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    h5 = TextStyle(
        fontFamily = fontsPoppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    h6 = TextStyle(
        fontFamily = fontsPoppins,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    h2 = TextStyle(
        fontFamily = fontsBebas,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)