package com.vijanthi.computervathiyar.ui.chapter

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.vijanthi.computervathiyar.data.db.dao.CourseDao
import com.vijanthi.computervathiyar.data.model.AdViewReport
import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.databinding.ActivityChapterBinding
import com.vijanthi.computervathiyar.util.PrefManager
import com.vijanthi.computervathiyar.util.TAG
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdsHelper(
    private val context: Activity,
    private val binding: ActivityChapterBinding,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val prefManger: PrefManager,
    private val courseDao: CourseDao,
    private val chapter: Chapter
) : DefaultLifecycleObserver {

    private var rewardedAd: RewardedAd? = null
    private val isDebug = true
    private var addUnit =
        if (isDebug) "ca-app-pub-3940256099942544/5224354917" else "ca-app-pub-8740059452952570/7674868822"

    private var disableScroll: Boolean = false

    init {
        binding.lessonWv.apply {
            setOnTouchListener { _, event -> disableScroll && event.action == MotionEvent.ACTION_MOVE }
        }
    }

    // on load show add if count is zero
    private fun setupAds() = callbackFlow {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, addUnit, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                rewardedAd = null
                trySend(false)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                rewardedAd = ad
                trySend(true)
            }
        })

        awaitClose { cancel() }
    }

    fun initAd() {
        Log.d(TAG, "init() called")
        lifecycleScope.launch {
            setupAds().collectLatest {
                Log.d(TAG, "setupAds() called isSuccess $it")
                if (it) setupAdCallback()
                val avr = prefManger.adViewReport
                Log.d(TAG, "initAd() called ${chapter.slug} $avr")
                if (avr == null) {
                    prefManger.adViewReport = AdViewReport(lastAdViewTime = System.currentTimeMillis(), 2)
                    showChapter()
                } else {
                    if (avr.adViewProvision == 0) {
                        showReadLimitReached()
                        return@collectLatest
                    }
                    val crc = courseDao.getCourseReadChapters(chapter.slug!!)
                    if (crc == null || crc.readCount <= 1 || crc.readCount % 3 == 0) {
                        avr.adViewProvision--
                        prefManger.adViewReport = avr
                    }
                    showChapter()
                }
            }
        }
    }

    private fun showReadLimitReached() {
        Log.d(TAG, "showReadLimitReached() called")
        disableScroll = true
        binding.apply {
            lessonRv.apply {
                isNestedScrollingEnabled = false
                suppressLayout(true)
                visibility = View.VISIBLE
            }
            lessonWv.visibility = View.VISIBLE
            loaderContainer.visibility = View.GONE
            adLimitExceedContainer.visibility = View.VISIBLE
        }
    }

    private fun setupAdCallback() {
        Log.d(TAG, "showAd() called")
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d(TAG, "Ad failed to show.")
                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
                rewardedAd = null
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                // Called when ad is dismissed.
            }
        }
    }

    private fun showChapter() {
        binding.apply {
            adLimitExceedContainer.visibility = View.GONE
            loaderContainer.visibility = View.GONE
            lessonRv.apply {
                visibility = View.VISIBLE
                isNestedScrollingEnabled = true
                suppressLayout(false)
            }
            lessonWv.visibility = View.VISIBLE
        }
        disableScroll = false
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate() called with: owner = $owner")
        binding.watchAdBt.setOnClickListener {
            if (rewardedAd == null) {
                Toast.makeText(context, "Ad not yet ready", Toast.LENGTH_SHORT).show()
                initAd()
                return@setOnClickListener
            }
            rewardedAd?.show(context) { rewardItem ->
                // Handle the reward.
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d("TAG", "User earned the reward. $rewardAmount $rewardType")
                prefManger.adViewReport =
                    AdViewReport(lastAdViewTime = System.currentTimeMillis(), 2)
                Toast.makeText(context, "Chapter read provisioned", Toast.LENGTH_LONG).show()
                showChapter()
            }
        }
    }

}