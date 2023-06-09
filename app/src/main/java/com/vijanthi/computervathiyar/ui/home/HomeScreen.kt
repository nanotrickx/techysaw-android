package com.vijanthi.computervathiyar.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vijanthi.computervathiyar.R
import com.vijanthi.computervathiyar.data.model.Course
import com.vijanthi.computervathiyar.ui.common.RandomColors
import com.vijanthi.computervathiyar.ui.home.components.CourseSearch
import com.vijanthi.computervathiyar.ui.home.viewmodel.HomeUiState
import com.vijanthi.computervathiyar.ui.home.viewmodel.HomeViewModel
import com.vijanthi.computervathiyar.ui.theme.gradientMiddle
import com.vijanthi.computervathiyar.ui.theme.gradientStart
import com.vijanthi.computervathiyar.ui.theme.primaryColor
import com.vijanthi.computervathiyar.ui.theme.primaryColor2
import com.vijanthi.computervathiyar.util.ScreenSize
import com.vijanthi.computervathiyar.util.isNetworkAvailable


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
            Column {
                HomeSearchSection {
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
    val listColors = listOf(gradientStart, gradientMiddle, primaryColor)
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }

    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(ss.heightOf(by = 4).dp)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listColors))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 24.dp, end = 18.dp)
            ) {
                Column {
                    Text(buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryColor2,
                                fontSize = 22.sp
                            )
                        ) {
                            append("H")
                        }
                        withStyle(style = SpanStyle(color = Color.White, fontSize = 22.sp)) {
                            append("ello, ")
                        }
                    })
                    Text(
                        text = user?.displayName ?: "World",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 32.sp
                    )
                }
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primaryColor2,
                        containerColor = Color.White
                    )
                ) {
                    if (user != null) {
                        SubcomposeAsyncImage(
                            model = user!!.photoUrl,
                            contentScale = ContentScale.Fit,
                            loading = {
                                CircularProgressIndicator()
                            }, modifier = Modifier
                                .clip(CircleShape)
                                .size(49.dp),
                            contentDescription = stringResource(R.string.person)
                        )
                    } else
                        Icon(Icons.Default.Person, contentDescription = "Notifications")
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
    val context = LocalContext.current
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

        HomeUiState.State.Error -> Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                if (context.isNetworkAvailable()) "Unknown error. Please try later." else "Internet not available.\nPlease enable internet to continue.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .clickable {
                    viewModel.getAllCourses()
                }
                .padding(horizontal = 4.dp, vertical = 2.dp)) {
                Icon(Icons.Default.Refresh, "refresh")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Refresh", style = MaterialTheme.typography.displaySmall)
            }
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
                LottieAnimation(composition = searchNotFound)
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Text(
                        "Categories",
                        style = MaterialTheme.typography.displayMedium,
                        fontSize = 22.sp
                    )
                    Text(
                        "Total - ${viewModel.uiState.courseList.size}",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
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
//            val catImage = when (course.slug) {
//                "python" -> R.drawable.ic_pythong
//                "css" -> R.drawable.ic_css
//                else -> R.drawable.ic_lesson_count
//            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(course.getCourseImage())
                    .fallback(R.mipmap.ic_launcher)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .crossfade(true)
                    .build(),
                contentDescription = course.slug,
                contentScale = ContentScale.Fit
            )
//            Image(
//                painter = painterResource(id = catImage),
//                contentDescription = "sample image",
//                Modifier
//                    .fillMaxSize()
//                    .background(Color.Transparent)
//            )
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
}
