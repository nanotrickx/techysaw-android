package com.vijanthi.computervathiyar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.vijanthi.computervathiyar.ui.course.CourseScreen
import com.vijanthi.computervathiyar.ui.home.HomeScreen
import com.vijanthi.computervathiyar.ui.theme.TechysawTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechysawTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberAnimatedNavController()

                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "home",
                    ) {
                        composable("home"
                        ) {
                            HomeScreen(navController)
                        }
                        composable("course/{slug}?title={title}",
                            enterTransition = { slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it/1 }) },
                            exitTransition = {  slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {it/1}) }
                        ) {
                            val slug = it.arguments?.getString("slug")!!
                            val title = it.arguments?.getString("title")!!

                            CourseScreen(slug = slug, title, navController)
                        }
                    }
                }
            }
        }
    }
}
