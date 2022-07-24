package org.skyfaced.noti.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

private val NotiSmallestRoundedCornerShape = RoundedCornerShape(4.dp)
private val NotiSmallRoundedCornerShape = RoundedCornerShape(8.dp)
private val NotiDefaultRoundedCornerShape = RoundedCornerShape(12.dp)
private val NotiBigRoundedCornerShape = RoundedCornerShape(16.dp)
private val NotiBiggestRoundedCornerShape = RoundedCornerShape(32.dp)

val NotiShapes = Shapes(
    extraSmall = NotiSmallestRoundedCornerShape,
    small = NotiSmallRoundedCornerShape,
    medium = NotiDefaultRoundedCornerShape,
    large = NotiBigRoundedCornerShape,
    extraLarge = NotiBiggestRoundedCornerShape
)