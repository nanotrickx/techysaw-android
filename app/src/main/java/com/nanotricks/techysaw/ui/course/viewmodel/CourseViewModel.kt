package com.nanotricks.techysaw.ui.course.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.data.model.Resource
import com.nanotricks.techysaw.data.repository.course.CourseRepository
import com.nanotricks.techysaw.ui.home.viewmodel.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {
    private var _uiState by mutableStateOf(CourseUiState())
    val uiState: CourseUiState get() = _uiState

    fun fetchCourse(slug: String) {
        Log.d("CourseViewModel", "fetchCourse() called with: slug = $slug")
        _uiState = _uiState.copy(state = CourseUiState.State.Loading)
        viewModelScope.launch {
            courseRepository.getCourseBySlug(slug).collect {
                when(it) {
                    is Resource.Error -> {
                        _uiState = _uiState.copy(state = CourseUiState.State.Error, error = it.throwable)
                    }
                    Resource.None -> {}
                    is Resource.Success -> {
                        _uiState = _uiState.copy(state = CourseUiState.State.Success, course = it.data)
                    }
                }
            }
        }
    }
}

data class CourseUiState(
    val state: State = State.None,
    val course: Course? = null,
    val error: Throwable? = null
) {
    sealed class State {
        object None : State()
        object Loading : State()
        object Success : State()
        object Empty : State()
        object Error : State()
    }
}
