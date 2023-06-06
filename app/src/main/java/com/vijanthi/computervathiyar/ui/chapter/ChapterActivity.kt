@file:Suppress("DEPRECATION")

package com.vijanthi.computervathiyar.ui.chapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vijanthi.computervathiyar.BuildConfig
import com.vijanthi.computervathiyar.data.db.dao.CourseDao
import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.databinding.ActivityChapterBinding
import com.vijanthi.computervathiyar.util.Constants
import com.vijanthi.computervathiyar.util.PrefManager
import com.vijanthi.computervathiyar.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import work.upstarts.editorjskit.models.EJBlock
import work.upstarts.editorjskit.models.EJBlockType
import work.upstarts.editorjskit.models.EJHeaderBlock
import work.upstarts.editorjskit.models.data.EJHeaderData
import javax.inject.Inject


@AndroidEntryPoint
class ChapterActivity : AppCompatActivity() {

    private val viewModel: ChapterViewModel by viewModels()
    private val binding by lazy { ActivityChapterBinding.inflate(layoutInflater) }

    @Inject
    lateinit var prefManger: PrefManager

    @Inject
    lateinit var courseDao: CourseDao

    lateinit var adsHelper: AdsHelper

    lateinit var chapterDataString: String
    private val chapterBlocks = mutableListOf<EJBlock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        val data = intent.getSerializableExtra(Constants.INTENT_CHAPTER_DATA) as Chapter?
        if (data != null) {
            adsHelper = AdsHelper(this@ChapterActivity, binding, lifecycleScope, prefManger, courseDao, data)
            doOperation(data)
        } else {
            Log.e("ChapterAct", "onCreate: Failed data not found ${intent.data}")
        }
    }

    private fun doOperation(data: Chapter) {
        lifecycle.addObserver(adsHelper)
        subscribeObserver()
        binding.apply {
            courseTitle.text = data.courseTitle
            backBt.setOnClickListener { onBackPressed() }
        }
        chapterBlocks.add(
            EJHeaderBlock(data = EJHeaderData(data.chapterTitle, 1), type = EJBlockType.HEADER)
        )
        setupWebView(data)
        adsHelper.initAd()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(data: Chapter) {
        binding.lessonWv.apply {
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            isScrollbarFadingEnabled = false
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    fetchData(data)
                }
            }
        }
        binding.lessonWv.settings.apply {
            javaScriptEnabled = true
            useWideViewPort = false
            displayZoomControls = false
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            builtInZoomControls = true
            databaseEnabled = true
            domStorageEnabled = true
            setGeolocationEnabled(true)
            saveFormData = false
            savePassword = false
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            pluginState = WebSettings.PluginState.ON
        }
        binding.lessonWv.loadUrl("file:///android_asset/editorjs/index.html")
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    }

    private fun subscribeObserver() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collect {
                when (it) {
                    is ChapterViewModel.ChapterUiState.ChapterDataReceived -> {
                        chapterDataString = it.chapterData.data
                        onChapterDataReceived()
                    }

                    is ChapterViewModel.ChapterUiState.Error -> {
                        Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onChapterDataReceived() {
        Log.d(TAG, "onChapterDataReceived() called $chapterDataString")
        binding.lessonWv.evaluateJavascript("javascript:showChapter($chapterDataString)") {}
    }

    private fun fetchData(data: Chapter) {
        viewModel.fetchChapterData(data)
    }
}