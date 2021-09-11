package com.example.mycreditscore.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycreditscore.model.CreditScoreResponse
import com.example.mycreditscore.model.repo.data.GetCreditScoreData
import com.example.mycreditscore.model.repo.data.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditScoreViewModel @Inject constructor(private val dataSource: GetCreditScoreData) : ViewModel() {

    private var dataState: MutableStateFlow<Response<CreditScoreResponse>?> = MutableStateFlow(null)
    val state: StateFlow<Response<CreditScoreResponse>?> = dataState

    init {
        getReportScore()
    }

    fun getReportScore() {
        viewModelScope.launch {
            dataState.value = dataSource.getCreditData()
        }
    }
}