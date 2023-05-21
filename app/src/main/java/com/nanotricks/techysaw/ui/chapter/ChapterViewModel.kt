package com.nanotricks.techysaw.ui.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanotricks.techysaw.data.db.model.ChapterData
import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.data.model.Resource
import com.nanotricks.techysaw.data.repository.chapter.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ChapterUiState>(ChapterUiState.Loading)
    val state: StateFlow<ChapterUiState> get() = _state

    fun fetchChapterData(chapter: Chapter) {
        viewModelScope.launch {
            chapterRepository.getChapterData(chapter).collect {
                when(it) {
                    is Resource.Error -> {
                        _state.value = ChapterUiState.Error(it.throwable?.localizedMessage ?: "Error")
                    }
                    Resource.None -> {}
                    is Resource.Success -> {
                        _state.value = ChapterUiState.ChapterDataReceived(it.data)
                    }
                }
            }
        }
    }


    sealed class ChapterUiState {
        object Init: ChapterUiState()
        object Loading: ChapterUiState()
        data class Error(val error: String): ChapterUiState()
        data class ChapterDataReceived(val chapterData: ChapterData): ChapterUiState()
    }
}