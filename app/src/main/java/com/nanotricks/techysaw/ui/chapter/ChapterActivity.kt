package com.nanotricks.techysaw.ui.chapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nanotricks.techysaw.R
import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import work.upstarts.editorjskit.models.EJBlock
import work.upstarts.editorjskit.ui.EditorJsAdapter
import work.upstarts.editorjskit.ui.theme.EJStyle
import work.upstarts.gsonparser.EJDeserializer

data class EJResponse(val blocks: List<EJBlock>)

@AndroidEntryPoint
class ChapterActivity : AppCompatActivity() {

    private val viewModel: ChapterViewModel by viewModels()

    private val rvAdapter: EditorJsAdapter by lazy {
        EditorJsAdapter(EJStyle.create(this.applicationContext))
    }
    lateinit var rv: RecyclerView
    lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        val data = intent.getSerializableExtra(Constants.INTENT_CHAPTER_DATA) as Chapter?
        Log.e("data recevied", "$data")
        if (data != null) {
            subscribeObserver()
            fetchData(data)
        }

//      get parcelable data from compose
        rv = findViewById<RecyclerView>(R.id.lesson_rv)
        val blocksType = object : TypeToken<MutableList<EJBlock>>() {}.type
        gson = GsonBuilder()
            .registerTypeAdapter(blocksType, EJDeserializer())
            .create()
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    private fun subscribeObserver() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collect {
                when(it) {
                    is ChapterViewModel.ChapterUiState.ChapterDataReceived -> {
                        val md = gson.fromJson(it.chapterData.data, EJResponse::class.java)
                        rvAdapter.items = md.blocks
                        rvAdapter.notifyDataSetChanged()
                    }
                    is ChapterViewModel.ChapterUiState.Error -> {
                        Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                    }
                    ChapterViewModel.ChapterUiState.Loading -> {

                    }
                    else -> {}
                }
            }
        }
    }

    private fun fetchData(data: Chapter) {
        viewModel.fetchChapterData(data)
    }
}