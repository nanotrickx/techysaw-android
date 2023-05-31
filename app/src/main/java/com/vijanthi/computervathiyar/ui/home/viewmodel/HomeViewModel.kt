package com.vijanthi.computervathiyar.ui.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vijanthi.computervathiyar.data.model.CourseList
import com.vijanthi.computervathiyar.data.model.Resource
import com.vijanthi.computervathiyar.data.repository.course.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private var _uiState by mutableStateOf(HomeUiState())
    val uiState: HomeUiState get() = _uiState

    init {
        getAllCourses()
    }

    fun getAllCourses() {
        _uiState = _uiState.copy(state = HomeUiState.State.Loading)

        viewModelScope.launch {
            courseRepository.getAllCourses().collect {
                if (it is Resource.Success) {
                    _uiState = _uiState.copy(state = HomeUiState.State.Success,totalCount = it.data.size, courseList = it.data)
                } else if (it is Resource.Error) {
                    _uiState = _uiState.copy(
                        state = HomeUiState.State.Error,
                        totalCount = 0,
                        error = it.throwable
                    )
                }
            }
        }

    }
    fun filterCourses(searchText: String) {
        _uiState = _uiState.copy(state = HomeUiState.State.Loading)

        viewModelScope.launch {
            courseRepository.filterCourse(searchText).collect {
                if (it is Resource.Success) {
                    _uiState = _uiState.copy(state = HomeUiState.State.Success,totalCount = it.data.size, courseList = it.data)
                } else if (it is Resource.Error) {
                    _uiState = _uiState.copy(
                        state = HomeUiState.State.Error,
                        totalCount = 0,
                        error = it.throwable
                    )
                }
            }
        }

    }
}

data class HomeUiState(
    val state: State = State.None,
    val courseList: CourseList = emptyList(),
    val totalCount: Int = 0,
    val error: Throwable? = null
) {
    sealed class State {
        object None : State()
        object Loading : State()
        object Success : State()
        object Empty : State()
        object Error : State()
        object LoadingMore : State()
        object LoadingMoreError : State()
    }
}

fun HomeUiState.hasMore(): Boolean {
    return this.state == HomeUiState.State.Success
            && this.totalCount > 0
            && this.courseList.size < totalCount
}
