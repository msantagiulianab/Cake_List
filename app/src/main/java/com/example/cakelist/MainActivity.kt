package com.example.cakelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.example.cakelist.ui.cakes.CakesScreen
import com.example.cakelist.ui.theme.CakeListTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

const val destinationCakesList: String = "destination_cakes_list"
const val destinationCakeDetails: String = "destination_cake_details"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CakeListTheme {
                CakesApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CakesApp() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = destinationCakesList,
        builder = {
            composable(
                route = destinationCakesList,
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -300 },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -300 },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                }
            ) {
                CakesScreen { navigationCakeId ->
                    navController.navigate("$destinationCakeDetails/$navigationCakeId")
                }
            }
        }
    )
}
