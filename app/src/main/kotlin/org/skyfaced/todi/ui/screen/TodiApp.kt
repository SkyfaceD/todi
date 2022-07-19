package org.skyfaced.todi.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import logcat.logcat
import org.skyfaced.todi.R
import org.skyfaced.todi.ui.screen.details.Mode
import org.skyfaced.todi.ui.screen.navigation.Screens
import org.skyfaced.todi.ui.screen.navigation.TodiNavHost
import org.skyfaced.todi.ui.theme.TodiTheme
import org.skyfaced.todi.ui.util.LocalTodiNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodiApp() {
    val navHostController = rememberNavController()

    CompositionLocalProvider(
        LocalTodiNavigation provides navHostController,
    ) {
        TodiTheme {
            Scaffold(
                modifier = Modifier
                    .systemBarsPadding()
                    .navigationBarsPadding(),
                topBar = { TodiTopBar(navHostController = navHostController) }
            ) { innerPadding ->
                TodiNavHost(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TodiTopBar(
    navHostController: NavHostController,
) {
    val navBackStackEntry = navHostController.currentBackStackEntryAsState().value

    CenterAlignedTopAppBar(
        title = {
            AnimatedContent(
                targetState = navBackStackEntry,
                transitionSpec = {
                    val duration = 750
                    val direction =
                        if (targetState?.destination?.route == Screens.Home.route) 1 else -1

                    val enter = slideInVertically(tween(duration)) { direction * it } + fadeIn(
                        tween(duration)
                    )
                    val exit = slideOutVertically(tween(duration)) { direction * -it } + fadeOut(
                        tween(duration)
                    )

                    (enter with exit).using(SizeTransform(clip = true))
                }
            ) { targetValue ->
                val title = when (targetValue?.destination?.route) {
                    Screens.Home.route -> stringResource(R.string.lbl_home)
                    Screens.Details.route -> {
                        val mode =
                            Mode.valueOf(targetValue.arguments?.getString("mode") ?: Mode.View.name)
                        logcat { "Val ${targetValue.arguments?.getString("mode")}" }
                        stringResource(
                            if (mode == Mode.View) R.string.lbl_view
                            else R.string.lbl_edit
                        )
                    }
                    Screens.Settings.route -> stringResource(R.string.lbl_settings)
                    else -> ""
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    text = title,
                    textAlign = TextAlign.Center
                )
            }
        },
        actions = {
            val alpha by animateFloatAsState(
                targetValue = if (navBackStackEntry?.destination?.route == Screens.Home.route) 1f else 0f
            )

            IconButton(
                modifier = Modifier.alpha(alpha),
                onClick = { navHostController.navigate(Screens.Settings.route) },
                enabled = navBackStackEntry?.destination?.route == Screens.Home.route
            ) {
                Icon(
                    painterResource(R.drawable.ic_settings),
                    stringResource(R.string.cd_settings)
                )
            }
        }
    )
}