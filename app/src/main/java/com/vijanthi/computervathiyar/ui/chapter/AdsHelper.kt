package com.vijanthi.computervathiyar.ui.chapter

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.vijanthi.computervathiyar.data.model.AdViewReport
import com.vijanthi.computervathiyar.databinding.ActivityChapterBinding
import com.vijanthi.computervathiyar.util.Constants
import com.vijanthi.computervathiyar.util.PrefManager
import com.vijanthi.computervathiyar.util.TAG
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class AdsHelper(
    private val context: Activity,
    private val binding: ActivityChapterBinding,
    private val viewModel: ChapterViewModel,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val prefManger: PrefManager
) : DefaultLifecycleObserver {

    private var isAdViewed: Boolean = false
    private var rewardedAd: RewardedAd? = null
    private val isDebug = true
    private var addUnit = if (isDebug) "ca-app-pub-3940256099942544/5224354917" else "ca-app-pub-8740059452952570/7674868822"
    // on load show add if count is zero
    private fun setupAds() = callbackFlow {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context,addUnit, adRequest, object : RewardedAdLoadCallback() {
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
    fun init() {
        Log.d(TAG, "init() called")
        lifecycleScope.launch {
            setupAds().collectLatest {
                Log.d(TAG, "setupAds() called isSuccess $it")
                if (it) {
                    val avr = prefManger.adViewReport
                    if (avr == null || avr.adViewProvision == 0) {
                        showAd()
                    } else {
                        avr.adViewProvision--
                        prefManger.adViewReport = avr
                        showChapter()
                    }
                }
            }
        }
    }

    fun showAd() {
        Log.d(TAG, "showAd() called")
        rewardedAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
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

        rewardedAd?.show(context) { rewardItem ->
            // Handle the reward.
            val rewardAmount = rewardItem.amount
            val rewardType = rewardItem.type
            Log.d("TAG", "User earned the reward. $rewardAmount $rewardType")
            prefManger.adViewReport = AdViewReport(lastAdViewTime = System.currentTimeMillis(), 2)
            Toast.makeText(context, "Chapter provision enabled", Toast.LENGTH_LONG).show()
            showChapter()
        }
    }

    private fun showChapter() {
        binding.loaderContainer.visibility = View.GONE
        binding.lessonRv.visibility = View.VISIBLE
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate() called with: owner = $owner")
    }
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }
}