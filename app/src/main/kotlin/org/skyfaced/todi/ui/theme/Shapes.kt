package org.skyfaced.todi.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val TodiSmallestRoundedCornerShape = RoundedCornerShape(4.dp)
val TodiSmallRoundedCornerShape = RoundedCornerShape(8.dp)
val TodiDefaultRoundedCornerShape = RoundedCornerShape(12.dp)
val TodiBigRoundedCornerShape = RoundedCornerShape(16.dp)
val TodiBiggestRoundedCornerShape = RoundedCornerShape(32.dp)

val TodiShapes = Shapes(
    extraSmall = TodiSmallestRoundedCornerShape,
    small = TodiSmallRoundedCornerShape,
    medium = TodiDefaultRoundedCornerShape,
    large = TodiBigRoundedCornerShape,
    extraLarge = TodiBiggestRoundedCornerShape
)