package org.skyfaced.todi.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import org.skyfaced.todi.R
import org.skyfaced.todi.database.TodiDatabaseImpl
import org.skyfaced.todi.settings.TodiLocale
import org.skyfaced.todi.settings.TodiSettingsImpl
import org.skyfaced.todi.settings.TodiTheme
import org.skyfaced.todi.ui.screen.details.DetailsRepositoryImpl
import org.skyfaced.todi.ui.screen.details.DetailsScreen
import org.skyfaced.todi.ui.screen.details.DetailsViewModel
import org.skyfaced.todi.ui.screen.details.Mode
import org.skyfaced.todi.ui.screen.home.HomeRepositoryImpl
import org.skyfaced.todi.ui.screen.home.HomeScreen
import org.skyfaced.todi.ui.screen.home.HomeViewModel
import org.skyfaced.todi.ui.screen.settings.SettingsScreen
import org.skyfaced.todi.ui.screen.settings.SettingsViewModel
import org.skyfaced.todi.ui.theme.TodiTheme
import org.skyfaced.todi.ui.util.ExtendedFloatingActionButton
import org.skyfaced.todi.util.LocalTodiDatabase
import org.skyfaced.todi.util.LocalTodiNavigation
import org.skyfaced.todi.util.LocalTodiSettings
import org.skyfaced.todi.util.collectAsStateWithLifecycle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodiApp() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val settings = remember { TodiSettingsImpl(context.applicationContext) }
    val database = remember { TodiDatabaseImpl(context.applicationContext).database }

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
        LocalTodiNavigation provides navHostController,
        LocalTodiSettings provides settings,
        LocalTodiDatabase provides database
    ) {
        val locale = settings.locale.observe.collectAsStateWithLifecycle(TodiLocale.English).value
        setLocale(locale)

        val navBackStackEntry = navHostController.currentBackStackEntryAsState().value

        val theme = settings.theme.observe.collectAsStateWithLifecycle(TodiTheme.System).value
        val darkTheme = when (theme) {
            TodiTheme.Light -> false
            TodiTheme.Dark -> true
            else -> isSystemInDarkTheme()
        }
        val dynamicColor = settings.dynamicColor.observe.collectAsStateWithLifecycle(true).value
        val amoled = settings.amoled.observe.collectAsStateWithLifecycle(false).value

        TodiTheme(
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

            Scaffold(
                modifier = Modifier
                    .systemBarsPadding()
                    .navigationBarsPadding(),
                topBar = {
                    TodiTopBar(
                        navBackStackEntry = navBackStackEntry,
                        locale = locale,
                        navigateUp = navHostController::navigateUp,
                        openSettings = {
                            navHostController.navigate(Screens.Settings.route)
                        }
                    )
                },
                floatingActionButton = {
                    TodiFloatingActionButton(
                        navBackStackEntry = navBackStackEntry,
                        onClick = {
                            navHostController.navigate(Screens.Details.argRoute(Mode.Create))
                        }
                    )
                },
                content = { innerPadding ->
                    TodiNavHost(
                        modifier = Modifier
                            .padding(innerPadding)
                            .imePadding()
                    )
                }
            )
        }
    }
}

@Composable
private fun TodiFloatingActionButton(
    navBackStackEntry: NavBackStackEntry?,
    onClick: () -> Unit,
) {
    val enabled = navBackStackEntry?.destination?.route == Screens.Home.route
    val duration = 500

    AnimatedVisibility(
        visible = enabled,
        enter = slideInHorizontally(tween(duration)) { it } + fadeIn(tween(duration)),
        exit = slideOutHorizontally(tween(duration)) { it } + fadeOut(tween(duration)),
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_add_task),
                    stringResource(R.string.cd_add_note)
                )
            },
            text = { Text(stringResource(R.string.lbl_add_note)) },
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            enabled = enabled,
            shape = MaterialTheme.shapes.extraLarge
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TodiTopBar(
    navBackStackEntry: NavBackStackEntry?,
    locale: TodiLocale,
    navigateUp: () -> Unit,
    openSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        navigationIcon = {
            val enabled = navBackStackEntry?.destination?.route == Screens.Settings.route ||
                    navBackStackEntry?.destination?.route == Screens.Details.route
            val alpha by animateFloatAsState(
                targetValue = if (enabled) 1f else 0f
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

                    enter with exit
                }
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

                        (enter with exit).using(SizeTransform(clip = true))
                    }
                ) { targetValue ->
                    val title = when (targetValue?.destination?.route) {
                        Screens.Home.route -> stringResource(R.string.lbl_app_name).uppercase()
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
                targetValue = if (navBackStackEntry?.destination?.route == Screens.Home.route) 1f else 0f
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
private fun TodiNavHost(
    modifier: Modifier = Modifier,
) {
    val navHostController = LocalTodiNavigation.current
    val settings = LocalTodiSettings.current
    val database = LocalTodiDatabase.current

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
                factory = object : AbstractSavedStateViewModelFactory(navBackStackEntry,
                    navBackStackEntry.arguments) {
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
@Suppress("DEPRECATION")
fun setLocale(
    context: Context,
    configuration: Configuration,
    locale: TodiLocale,
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
private fun setLocale(locale: TodiLocale) {
    val javaLocale = Locale(locale.tag)
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    configuration.setLocale(javaLocale)
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
}