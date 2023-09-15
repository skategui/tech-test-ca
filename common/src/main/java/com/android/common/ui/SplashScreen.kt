package com.android.common.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.common.R
import com.android.common.ui.screens.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreditAgricoleSplashAnimation(modifier: Modifier, navController: NavController) {


    val targetValue = 160f

    val width = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val height = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    val scaleFactor = remember {
        Animatable(1.5f)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true, block = {
        runAnimation(targetValue, coroutineScope, scaleFactor)

    })

    if (scaleFactor.value >= targetValue) {
        navController.navigate(Screens.BanksScreen.route)
    }

    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFF006F4E))
    ) {

        if (scaleFactor.value < targetValue) {
            Image(
                painter =
                painterResource(id = R.drawable.credit_agricole_white),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .offset {
                        IntOffset(
                            width
                                .div(2)
                                .toInt()
                                .minus(100),
                            height
                                .div(2)
                                .toInt()
                        )
                    }
                    .scale(scaleFactor.value)
            )
        }
    }


}

private fun runAnimation(
    targetValue: Float,
    coroutineScope: CoroutineScope,
    scaleFactor: Animatable<Float, AnimationVector1D>
) {
    coroutineScope.launch {
        repeat(2) {
            scaleFactor.animateTo(
                1f,
                tween(easing = FastOutSlowInEasing, durationMillis = 1000.div(it.plus(1)))
            )
            scaleFactor.animateTo(
                1.5f,
                tween(easing = FastOutSlowInEasing, durationMillis = 1500.div(it.plus(1)))
            )
        }
        scaleFactor.animateTo(targetValue, tween(easing = FastOutSlowInEasing))
        delay(1000)
        scaleFactor.animateTo(1f)
        runAnimation(targetValue, coroutineScope, scaleFactor)
    }
}