package com.example.mycreditscore.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.example.mycreditscore.BuildConfig
import com.example.mycreditscore.R
import com.example.mycreditscore.databinding.FragmentHomeBinding
import com.example.mycreditscore.model.CreditScoreResponse
import com.example.mycreditscore.model.repo.data.Response
import com.example.mycreditscore.util.getDonutProgress
import com.example.mycreditscore.util.isOnline
import com.example.mycreditscore.util.responseDataToMap
import com.example.mycreditscore.viewModel.CreditScoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val creditScoreViewModel: CreditScoreViewModel by viewModels()
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private var creditScoreData: CreditScoreResponse? = null
    private var currentScore: Int? = null
    private var maxScore: Int? = null
    private var creditDataMap: Map<String, Any?>? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        mSwipeRefreshLayout = fragmentHomeBinding?.swipeToRefresh
        return fragmentHomeBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleProgressIndicatorVisibility(true)
        if (isOnline(requireContext())) {
            toggleNetworkErrorMessage(false)
            autoRetrieveCreditScoreData()
        } else {
            toggleNetworkErrorMessage(true)
            toggleProgressIndicatorVisibility(false)
            toggleCreditScoreDisplay(false)
        }
        setUpListeners()
    }

    private fun setUpListeners() {
        fragmentHomeBinding?.btnShowMoreDetails?.setOnClickListener {
            creditDataMap?.let { creditData ->
                val bundle = bundleOf("creditData" to creditData)
                findNavController().navigate(R.id.creditReportFragment, bundle)
            }
        }

        mSwipeRefreshLayout?.setOnRefreshListener {
            if (isOnline(requireContext()))
                manualRetrieveCreditScoreData()
            else
                mSwipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun updateCreditValues() {
        currentScore = creditScoreData?.creditReportInfo?.score
        maxScore = creditScoreData?.creditReportInfo?.maxScoreValue
    }

    private fun errorPopup() {
        MaterialDialog(requireContext()).show {
            title(text = getString(R.string.pop_up_title))
            message(text = getString(R.string.pop_up_message))
            positiveButton(text = getString(R.string.retry)) {
                manualRetrieveCreditScoreData()
            }
            negativeButton(text = getString(R.string.close)) {
                requireActivity().finish()
                exitProcess(0)
            }
        }
    }

    private fun updateProgressBar() {
        if (currentScore == null || maxScore == null) {
            errorPopup()
            return
        }

        currentScore?.let { _score ->
            maxScore?.let { _maxScore ->
                fragmentHomeBinding?.pbDonut?.let { progressBar ->
                    startProgressAnimation(progressBar, getDonutProgress(_score, _maxScore))
                }

                fragmentHomeBinding?.root?.findViewById<TextView>(R.id.tvScoreValue)?.apply {
                    startCountAnimation(this, _score)
                }

                fragmentHomeBinding?.root?.findViewById<TextView>(R.id.tvMaxValue)?.apply {
                    this.text = getString(R.string.out_of_text, _maxScore.toString())
                }

            }
        }

        /*
        fragmentHomeBinding?.pbDonut?.progressDrawable?.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
        val colors = intArrayOf(10928333, 6950317)
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, colors
        )
       println("Hello -> ${fragmentHomeBinding?.pbDonut?.progressBackgroundTintList}")
       */

    }

    private fun startProgressAnimation(progressBar: ProgressBar, donutProgressScore: Int) {
        progressBar.progress = 0
        val objectAnimator =
            ObjectAnimator.ofInt(progressBar, "progress", donutProgressScore)
        objectAnimator.duration = BuildConfig.MAX_ANIMATION_DURATION
        objectAnimator.addUpdateListener { animator ->
            progressBar.progress = animator.animatedValue as Int
        }
        objectAnimator.start()
    }

    private fun startCountAnimation(textView: TextView, creditScore: Int) {
        val animator = ValueAnimator.ofInt(0, creditScore)
        animator.duration = BuildConfig.MAX_ANIMATION_DURATION
        animator.addUpdateListener { animation ->
            textView.text = animation.animatedValue.toString()
        }
        animator.start()
    }

    private fun autoRetrieveCreditScoreData() {
        if (isOnline(requireContext())) {
            toggleNetworkErrorMessage(false)
            lifecycleScope.launchWhenStarted {
                creditScoreViewModel.state.collect { creditData ->
                    toggleProgressIndicatorVisibility(false)
                    when (creditData) {
                        is Response.Error -> {
                            errorPopup()
                            creditData.exception.message?.let { message ->
                                Log.e("ResponseError: ", message)
                            }
                        }
                        is Response.Success -> {
                            toggleCreditScoreDisplay(true)
                            creditScoreData = creditData.data
                            updateCreditValues()
                            updateProgressBar()
                            creditScoreData?.let {
                                creditDataMap = responseDataToMap(it)
                            }
                        }
                        // just in case TODO()
                        else -> {
                            toggleProgressIndicatorVisibility(false)
                        }
                    }
                }
            }
        }
    }

    private fun manualRetrieveCreditScoreData() {
        toggleProgressIndicatorVisibility(true)
        autoRetrieveCreditScoreData()
    }

    private fun toggleProgressIndicatorVisibility(show: Boolean) {
        if (show)
            fragmentHomeBinding?.pbNetworkCall?.visibility = View.VISIBLE
        else
            fragmentHomeBinding?.pbNetworkCall?.visibility = View.GONE
        mSwipeRefreshLayout?.isRefreshing = show
    }

    private fun toggleCreditScoreDisplay(show: Boolean) {
        if (show)
            fragmentHomeBinding?.donutContainer?.visibility = View.VISIBLE
        else
            fragmentHomeBinding?.donutContainer?.visibility = View.GONE
    }

    private fun toggleNetworkErrorMessage(show: Boolean) {
        if (show)
            fragmentHomeBinding?.tvProgress?.visibility = View.VISIBLE
        else
            fragmentHomeBinding?.tvProgress?.visibility = View.GONE
        mSwipeRefreshLayout?.isRefreshing = show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding = null
    }
}