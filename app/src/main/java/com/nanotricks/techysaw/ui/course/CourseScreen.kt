package com.nanotricks.techysaw.ui.course

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nanotricks.techysaw.R
import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.ui.chapter.ChapterActivity
import com.nanotricks.techysaw.ui.common.BackButton
import com.nanotricks.techysaw.ui.course.viewmodel.CourseUiState
import com.nanotricks.techysaw.ui.course.viewmodel.CourseViewModel
import com.nanotricks.techysaw.util.Constants
import com.nanotricks.techysaw.util.ScreenSize

@Composable
fun CourseScreen(
    slug: String, navController: NavHostController,
    viewModel: CourseViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton {
                    navController.popBackStack()
                }
                Text(
                    slug,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
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
        ) {
            Box() {

            }
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
            Row {
                Icon(Icons.Default.AccountBox, "")
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
        LazyColumn {
            items(course.chapter) {
                ChapterListItem(chapter = it) { selectedChapter ->
                   viewModel.updateReadStatus(course, selectedChapter)
                    val intent = Intent(context, ChapterActivity::class.java)
                    intent.putExtra(Constants.INTENT_CHAPTER_DATA,selectedChapter)
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
        shape = CircleShape.copy(CornerSize(15.dp)),
        modifier = Modifier.padding(12.dp),
        onClick = { onChapterClicked(chapter) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)) {
            Row {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.2f),
                    ),
                    shape = CircleShape.copy(CornerSize(12.dp)),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(55.dp)
                        .height(70.dp)
                        .padding(vertical = 2.dp, horizontal = 1.dp)
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = chapter.chapterTitle, style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = chapter.chapterTitle, style = MaterialTheme.typography.displaySmall)
                }
            }
        }
    }
}
