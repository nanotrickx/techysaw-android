package com.nanotricks.techysaw.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nanotricks.techysaw.R
import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.ui.common.RandomColors
import com.nanotricks.techysaw.ui.home.components.CourseSearch
import com.nanotricks.techysaw.ui.home.viewmodel.HomeUiState
import com.nanotricks.techysaw.ui.home.viewmodel.HomeViewModel
import com.nanotricks.techysaw.ui.theme.gradientMiddle
import com.nanotricks.techysaw.ui.theme.gradientStart
import com.nanotricks.techysaw.ui.theme.primaryColor
import com.nanotricks.techysaw.ui.theme.primaryColor2
import com.nanotricks.techysaw.util.ScreenSize


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val searchCourse = remember { mutableStateOf("") }
    Scaffold(content = {
        Surface(
            modifier = Modifier.padding(it),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.1F)
        ) {
            Column() {
                HomeSearchSection() {
                    searchCourse.value = it
                    viewModel.filterCourses(searchCourse.value.lowercase())
                }
                HomeBody(viewModel, navController)
            }
        }
    })
}

@Preview(showBackground = true)
@Composable
fun HomeSearchSection(onSearchChange: (String) -> Unit = {}) {
    val ss = ScreenSize()
    val listColors = listOf(gradientStart, gradientMiddle ,primaryColor)
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(ss.heightOf(by = 4).dp)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listColors))) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 24.dp, end = 18.dp)
            ) {
                Column {
                    Text(
                        text = "Hello,",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = "Good Morning",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(40.dp),  //avoid the oval shape
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primaryColor2,
                        containerColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }

            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp)
            ) {
                CourseSearch(onSearchChange)
            }
        }

    }
}


@Composable
fun HomeBody(viewModel: HomeViewModel, navController: NavHostController) {
    val searchNotFound by rememberLottieComposition(LottieCompositionSpec.Asset("animations/search_notfound.json"))

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text("Categories", style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
        Text("See All", style = MaterialTheme.typography.displaySmall, color = gradientMiddle)
    }
    Spacer(modifier = Modifier.height(8.dp))
    when (viewModel.uiState.state) {
        HomeUiState.State.None,
        HomeUiState.State.Empty -> Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                "No stories at this time. Please try later.",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        HomeUiState.State.Error -> Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                viewModel.uiState.error?.localizedMessage ?: "Unknown error. Please try later.",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        HomeUiState.State.Loading -> {
            Spacer(modifier = Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
            }
        }

        HomeUiState.State.LoadingMore -> {}
        HomeUiState.State.LoadingMoreError -> {}
        HomeUiState.State.Success -> {
            if (viewModel.uiState.courseList.isEmpty()) {
                LottieAnimation(composition = searchNotFound,)
            } else {
                CourseList(uiState = viewModel.uiState, onClick = {
                    navController.navigate("course/${it.slug}?title=${it.title}") {
                        this.launchSingleTop = true
                    }
                })
            }
        }
    }

}

@Composable
private fun CourseList(
    uiState: HomeUiState,
    onClick: (Course) -> Unit
) {
    val ss = ScreenSize()
    val listState = rememberLazyGridState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(2),
        ) {

            itemsIndexed(uiState.courseList) { index, item ->
                CourseItem(
                    index + 1,
                    item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .height(ss.heightOf(by = 4).dp)
                        .clickable { onClick(item) }
                )
            }

            // Loading more indicator
            if (uiState.state == HomeUiState.State.LoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp),
                            strokeWidth = 1.dp
                        )
                    }
                }
            }

            if (uiState.state == HomeUiState.State.LoadingMoreError) {

                item {
                    Text(
                        uiState.error?.localizedMessage
                            ?: "Unable to fetch latest stories at this time. Please try later.",
                        modifier = Modifier
                            .align(if (uiState.courseList.isEmpty()) Alignment.Center else Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

    }
}

val randomColors = RandomColors()

@Composable
private fun CourseItem(
    index: Int,
    course: Course,
    modifier: Modifier = Modifier
) {
    println("Random colors ${randomColors.courseColors}")
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(randomColors.courseColors[index]).copy(alpha = 0.15f),
        ),
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = course.title, style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${course.chapter.size} Courses",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(24.dp))
            val catImage = when(course.slug) {
                "python" -> R.drawable.ic_pythong
                "css" -> R.drawable.ic_css
                else -> R.drawable.ic_lesson_count
            }
            Image(
                painter = painterResource(id = catImage),
                contentDescription = "sample image",
                Modifier.fillMaxSize().background(Color.Transparent)
            )
        }

    }
}

data class NavigationMenu(
    val title: String,
    val icon: ImageVector
)
val menus = listOf(
    NavigationMenu("Featured", icon = Icons.Sharp.Star),
    NavigationMenu("My Course", Icons.Sharp.Notifications),
    NavigationMenu("Saved", Icons.Default.Favorite),
    NavigationMenu("Setting", Icons.Default.Settings),
)
@Composable
fun BottomBar() {

    val selectedIndex = remember { mutableStateOf(menus.first().title) }
    fun changeNavigation(selectedMenu: String) {
        selectedIndex.value = selectedMenu
//        navigate
    }
    NavigationBar(containerColor = Color.White, tonalElevation = 20.dp) {
        menus.map {
            val isSelected = selectedIndex.value == it.title
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray.copy(alpha = 0.8f),
                    unselectedTextColor = Color.Gray.copy(alpha = 0.8f),
                    indicatorColor = Color.White
                ),
                label = {
                    Text(text = it.title, style = MaterialTheme.typography.labelSmall)
                },
                selected = isSelected,
                onClick = { changeNavigation(it.title) },
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) })
        }
    }


//    BottomNavigation(elevation = 10.dp) {
//
//        BottomNavigationItem(icon = {
//            Icon(imageVector = Icons.Default.Home,"")
//        },
//            label = { Text(text = "Home") },
//            selected = (selectedIndex.value == 0),
//            onClick = {
//                selectedIndex.value = 0
//            })
//
//        BottomNavigationItem(icon = {
//            Icon(imageVector = Icons.Default.Favorite,"")
//        },
//            label = { Text(text = "Favorite") },
//            selected = (selectedIndex.value == 1),
//            onClick = {
//                selectedIndex.value = 1
//            })
//
//        BottomNavigationItem(icon = {
//            Icon(imageVector = Icons.Default.Person, "")
//        },
//            label = { Text(text = "Profile") },
//            selected = (selectedIndex.value == 2),
//            onClick = {
//                selectedIndex.value = 2
//            })
//    }
}
