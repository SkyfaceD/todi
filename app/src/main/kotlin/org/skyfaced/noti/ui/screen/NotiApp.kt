package org.skyfaced.noti.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import logcat.logcat
import org.skyfaced.noti.BuildConfig
import org.skyfaced.noti.R
import org.skyfaced.noti.database.NotiDatabaseImpl
import org.skyfaced.noti.settings.NotiLocale
import org.skyfaced.noti.settings.NotiSettingsImpl
import org.skyfaced.noti.settings.NotiTheme
import org.skyfaced.noti.ui.screen.details.DetailsRepositoryImpl
import org.skyfaced.noti.ui.screen.details.DetailsScreen
import org.skyfaced.noti.ui.screen.details.DetailsViewModel
import org.skyfaced.noti.ui.screen.home.HomeRepositoryImpl
import org.skyfaced.noti.ui.screen.home.HomeScreen
import org.skyfaced.noti.ui.screen.home.HomeViewModel
import org.skyfaced.noti.ui.screen.settings.SettingsScreen
import org.skyfaced.noti.ui.screen.settings.SettingsViewModel
import org.skyfaced.noti.ui.theme.NotiTheme
import org.skyfaced.noti.ui.util.ExtendedFloatingActionButton
import org.skyfaced.noti.util.LocalNotiDatabase
import org.skyfaced.noti.util.LocalNotiExtendedFloatingActionButton
import org.skyfaced.noti.util.LocalNotiNavigation
import org.skyfaced.noti.util.LocalNotiNotifications
import org.skyfaced.noti.util.LocalNotiSettings
import org.skyfaced.noti.util.Mode
import org.skyfaced.noti.util.fab.NotiExtendedFloatingActionButton
import org.skyfaced.noti.util.fab.NotiExtendedFloatingActionButtonImpl
import org.skyfaced.noti.util.notifcations.NotiNotificationsImpl
import java.util.*
import kotlin.math.abs

@Composable
fun NotiApp() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val settings = remember { NotiSettingsImpl(context.applicationContext) }
    val database = remember { NotiDatabaseImpl(context.applicationContext).database }
    val snackbarHostState = remember { SnackbarHostState() }
    val notifications = remember { NotiNotificationsImpl(context, snackbarHostState) }
    val expanded = remember { mutableStateOf(true) }
    val fab = remember { NotiExtendedFloatingActionButtonImpl(expanded) }

    val systemUiController = rememberSystemUiController()
    val navHostController = rememberNavController()

    DisposableEffect(lifecycleOwner) {
        val listener = NavController.OnDestinationChangedListener { _, dest, args ->
            logcat { "Navigation: $dest $args" }
        }
        navHostController.addOnDestinationChangedListener(listener)

        onDispose {
            navHostController.removeOnDestinationChangedListener(listener)
        }
    }

    CompositionLocalProvider(
        LocalNotiNavigation provides navHostController,
        LocalNotiSettings provides settings,
        LocalNotiDatabase provides database,
        LocalNotiNotifications provides notifications,
        LocalNotiExtendedFloatingActionButton provides fab
    ) {
        val locale = settings.locale.observe.collectAsStateWithLifecycle(NotiLocale.English).value
        setLocale(locale)

        val navBackStackEntry = navHostController.currentBackStackEntryAsState().value

        @Suppress("MoveVariableDeclarationIntoWhen")
        val theme = settings.theme.observe.collectAsStateWithLifecycle(NotiTheme.System).value
        val darkTheme = when (theme) {
            NotiTheme.Light -> false
            NotiTheme.Dark -> true
            else -> isSystemInDarkTheme()
        }
        val dynamicColor = settings.dynamicColor.observe.collectAsStateWithLifecycle(true).value
        val amoled = settings.amoled.observe.collectAsStateWithLifecycle(false).value

        NotiTheme(
            darkTheme = darkTheme,
            dynamicColor = dynamicColor,
            amoled = amoled
        ) {
            val backgroundColor = MaterialTheme.colorScheme.background

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = backgroundColor,
                    darkIcons = !darkTheme
                )
                systemUiController.setNavigationBarColor(
                    color = backgroundColor,
                    darkIcons = !darkTheme,
                )
            }

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        var y = consumed.y
                        if (abs(y) < 1e-3) y = 0f

                        if (-y < 0 && !fab.expanded.value) fab.expand()
                        else if (-y > 0 && fab.expanded.value) fab.shrink()

                        return super.onPostScroll(consumed, available, source)
                    }
                }
            }

            Scaffold(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .systemBarsPadding()
                    .navigationBarsPadding(),
                topBar = {
                    NotiTopBar(
                        navBackStackEntry = navBackStackEntry,
                        locale = locale,
                        navigateUp = navHostController::navigateUp,
                        openSettings = {
                            notifications.hideSnackbar()
                            navHostController.navigate(Screens.Settings.route)
                        }
                    )
                },
                floatingActionButton = {
                    NotiFloatingActionButton(
                        fab = fab,
                        navBackStackEntry = navBackStackEntry,
                        onClick = {
                            notifications.hideSnackbar()
                            navHostController.navigate(Screens.Details.argRoute(Mode.Create))
                        }
                    )
                },
                content = { innerPadding ->
                    NotiNavHost(
                        modifier = Modifier
                            .padding(innerPadding)
                            .imePadding()
                    )
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = notifications.snackbarHostState,
                        snackbar = {
                            Snackbar(
                                snackbarData = it,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun NotiFloatingActionButton(
    fab: NotiExtendedFloatingActionButton,
    navBackStackEntry: NavBackStackEntry?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabled = navBackStackEntry?.destination?.route == Screens.Home.route
    val duration = 500

    AnimatedVisibility(
        visible = enabled,
        enter = slideInHorizontally(tween(duration)) { it } + fadeIn(tween(duration)),
        exit = slideOutHorizontally(tween(duration)) { it } + fadeOut(tween(duration)),
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_add_task),
                    stringResource(R.string.cd_add_note)
                )
            },
            text = { Text(stringResource(R.string.lbl_add_note)) },
            enabled = enabled,
            expanded = fab.expanded.value,
            shape = MaterialTheme.shapes.extraLarge
        )
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotiTopBar(
    navBackStackEntry: NavBackStackEntry?,
    locale: NotiLocale,
    navigateUp: () -> Unit,
    openSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        navigationIcon = {
            val enabled = navBackStackEntry?.destination?.route == Screens.Settings.route ||
                    navBackStackEntry?.destination?.route == Screens.Details.route
            val alpha by animateFloatAsState(
                targetValue = if (enabled) 1f else 0f,
                label = "alphaAnimation"
            )

            IconButton(
                modifier = Modifier.alpha(alpha),
                onClick = navigateUp,
                enabled = enabled
            ) {
                Icon(
                    painterResource(R.drawable.ic_back),
                    stringResource(R.string.cd_back)
                )
            }
        },
        title = {
            val duration = 500

            AnimatedContent(
                targetState = locale,
                transitionSpec = {
                    val enter = scaleIn(tween(duration)) + fadeIn(tween(duration))
                    val exit = scaleOut(tween(duration)) + fadeOut(tween(duration))

                    enter togetherWith exit
                },
                label = "languageAnimation"
            ) {
                AnimatedContent(
                    targetState = navBackStackEntry,
                    transitionSpec = {
                        val direction =
                            if (targetState?.destination?.route == Screens.Home.route) 1 else -1

                        val enter = slideInVertically(tween(duration)) { direction * it } + fadeIn(
                            tween(duration)
                        )
                        val exit =
                            slideOutVertically(tween(duration)) { direction * -it } + fadeOut(
                                tween(duration)
                            )

                        (enter togetherWith exit).using(SizeTransform(clip = true))
                    },
                    label = "toolbarAnimation"
                ) { targetValue ->
                    val title = when (targetValue?.destination?.route) {
                        Screens.Home.route -> BuildConfig.APP_NAME
                        Screens.Details.route -> {
                            val mode =
                                Mode.valueOf(targetValue.arguments?.getString("mode").orEmpty())
                            stringResource(
                                when (mode) {
                                    Mode.Edit -> R.string.lbl_editing
                                    Mode.Create -> R.string.lbl_creating
                                }
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
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        actions = {
            val alpha by animateFloatAsState(
                targetValue = if (navBackStackEntry?.destination?.route == Screens.Home.route) 1f else 0f,
                label = "alphaAnimation"
            )

            IconButton(
                modifier = Modifier.alpha(alpha),
                onClick = openSettings,
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

@Composable
private fun NotiNavHost(
    modifier: Modifier = Modifier,
) {
    val navHostController = LocalNotiNavigation.current
    val settings = LocalNotiSettings.current
    val database = LocalNotiDatabase.current

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            val viewModel = viewModel<HomeViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (!modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }

                        val repository = HomeRepositoryImpl(database.noteDao)
                        return HomeViewModel(settings, repository) as T
                    }
                }
            )

            HomeScreen(viewModel = viewModel)
        }

        composable(
            route = Screens.Details.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("id") { type = NavType.LongType; defaultValue = 0L },
            )
        ) { navBackStackEntry ->
            val viewModel = viewModel<DetailsViewModel>(
                factory = object : AbstractSavedStateViewModelFactory(
                    navBackStackEntry,
                    navBackStackEntry.arguments
                ) {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(
                        key: String,
                        modelClass: Class<T>,
                        handle: SavedStateHandle,
                    ): T {
                        if (!modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }

                        val repository = DetailsRepositoryImpl(database.noteDao)
                        return DetailsViewModel(repository, handle) as T
                    }
                }
            )

            DetailsScreen(viewModel = viewModel)
        }

        composable(Screens.Settings.route) {
            val viewModel = viewModel<SettingsViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (!modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }

                        return SettingsViewModel(settings) as T
                    }
                }
            )

            SettingsScreen(viewModel = viewModel)
        }
    }
}

/**
 * Experimental function
 */
@Suppress("DEPRECATION", "unused")
fun setLocale(
    context: Context,
    configuration: Configuration,
    locale: NotiLocale,
) {
    val javaLocale = Locale(locale.tag)
    configuration.setLocale(javaLocale)
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
}

/**
 * Experimental function
 */
@SuppressLint("ComposableNaming")
@Suppress("DEPRECATION")
@Composable
@ReadOnlyComposable
private fun setLocale(locale: NotiLocale) {
    val javaLocale = Locale(locale.tag)
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    configuration.setLocale(javaLocale)
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
}