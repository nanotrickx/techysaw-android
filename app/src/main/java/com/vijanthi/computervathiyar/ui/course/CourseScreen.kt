package com.vijanthi.computervathiyar.ui.course

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vijanthi.computervathiyar.R
import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.data.model.Course
import com.vijanthi.computervathiyar.ui.chapter.ChapterActivity
import com.vijanthi.computervathiyar.ui.common.BackButton
import com.vijanthi.computervathiyar.ui.course.viewmodel.CourseUiState
import com.vijanthi.computervathiyar.ui.course.viewmodel.CourseViewModel
import com.vijanthi.computervathiyar.util.Constants
import com.vijanthi.computervathiyar.util.ScreenSize

@Composable
fun CourseScreen(
    slug: String, title: String, navController: NavHostController,
    viewModel: CourseViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchCourse(slug)
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.05F))
    ) {
        Column(
            Modifier
                .background(Color.Gray.copy(alpha = 0.05F))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                /*verticalAlignment = Alignment.CenterVertically*/
            ) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    BackButton {
                        navController.popBackStack()
                    }
                }
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            when (viewModel.uiState.state) {
                CourseUiState.State.None,
                CourseUiState.State.Empty -> Box(
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

                CourseUiState.State.Error -> Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        viewModel.uiState.error?.localizedMessage
                            ?: "Unknown error. Please try later.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                CourseUiState.State.Loading -> {
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        CircularProgressIndicator()
                    }
                }

                CourseUiState.State.Success -> {
                    CourseChapter(viewModel.uiState.course, navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun CourseChapter(course: Course?, navController: NavHostController, viewModel: CourseViewModel) {
    val ss = ScreenSize()
    val context = LocalContext.current

    Column(Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
        if (course == null) {
            return@Column
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(ss.heightOf(by = 4).dp), shape = CircleShape.copy(CornerSize(22.dp))
        ,

        ) {
            Image(painterResource(id = R.drawable.ic_pythong),"", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = course.title, style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = course.description, style = MaterialTheme.typography.bodyMedium)
            }
            Row(Modifier.height(35.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lesson_count), "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${course.chapter.size}",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Gray.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.padding(bottom = 12.dp).fillMaxHeight()
        ) {
            items(course.chapter) {
                ChapterListItem(chapter = it) { selectedChapter ->
                    viewModel.updateReadStatus(course, selectedChapter)
                    val intent = Intent(context, ChapterActivity::class.java)
                    intent.putExtra(Constants.INTENT_CHAPTER_DATA, selectedChapter)
                    context.startActivity(intent)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListItem(chapter: Chapter, onChapterClicked: (Chapter) -> Unit) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = CircleShape.copy(CornerSize(12.dp)),
        modifier = Modifier.padding(top = 6.dp, start = 12.dp, end = 12.dp, bottom = 2.dp).shadow(
            ambientColor = Color.Gray.copy(alpha = 0.1f),
            elevation = 10.dp,
            clip = false,
            spotColor = Color.Gray.copy(alpha = 0.2f)
        ),
        onClick = { onChapterClicked(chapter) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
    ) {
        Column() {
            Row {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.2f),
                    ),
                    shape = CircleShape.copy(CornerSize(10.dp)),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(55.dp)
                        .height(70.dp)
                        .padding(2.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = if (chapter.isRead) R.drawable.ic_check_blue else R.drawable.ic_check_grey),
                            modifier = Modifier.size(30.dp),
                            contentDescription = ""
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)) {
                    Text(
                        text = chapter.chapterTitle,
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = chapter.chapterTitle, style = MaterialTheme.typography.displaySmall)
                }
            }
        }
    }
}
