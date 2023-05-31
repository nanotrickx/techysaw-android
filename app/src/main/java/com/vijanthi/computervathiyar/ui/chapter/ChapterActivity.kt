@file:Suppress("DEPRECATION")

package com.vijanthi.computervathiyar.ui.chapter

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
import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.data.model.EJResponse
import com.vijanthi.computervathiyar.databinding.ActivityChapterBinding
import com.vijanthi.computervathiyar.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import work.upstarts.editorjskit.models.EJBlock
import work.upstarts.editorjskit.ui.EditorJsAdapter
import work.upstarts.editorjskit.ui.theme.EJStyle
import work.upstarts.gsonparser.EJDeserializer

@AndroidEntryPoint
class ChapterActivity : AppCompatActivity() {

    private val viewModel: ChapterViewModel by viewModels()
    private val binding by lazy {
        ActivityChapterBinding.inflate(layoutInflater)
    }

    private val rvAdapter: EditorJsAdapter by lazy {
        EditorJsAdapter(EJStyle.create(this.applicationContext))
    }

    private val gson: Gson by lazy {
        val blocksType = object : TypeToken<MutableList<EJBlock>>() {}.type
        GsonBuilder()
            .registerTypeAdapter(blocksType, EJDeserializer())
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val data = intent.getSerializableExtra(Constants.INTENT_CHAPTER_DATA) as Chapter?
        if (data != null) {
            subscribeObserver()
            fetchData(data)
            binding.title.text = data.chapterTitle
            binding.backBt.setOnClickListener { onBackPressed() }
            setupRecyclerView()
        } else {
            Log.e("ChapterAct", "onCreate: Failed data not found ${intent.data}")
        }
    }

    private fun setupRecyclerView() {
        binding.lessonRv.adapter = rvAdapter
        binding.lessonRv.layoutManager = LinearLayoutManager(this)
        binding.lessonRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
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