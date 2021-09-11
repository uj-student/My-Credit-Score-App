package com.example.mycreditscore.testUtils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mycreditscore.model.CreditScoreResponse
import com.example.mycreditscore.model.repo.data.GetCreditScoreData
import com.example.mycreditscore.model.repo.data.Response
import com.example.mycreditscore.viewModel.CreditScoreViewModel
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseUnitTest {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule =
        InstantTaskExecutorRule() //Allows execution of LiveData to happen instantly

    suspend fun mockSuccessfulGetData(
        creditDataRequestMock: GetCreditScoreData,
        creditScoreResponse: CreditScoreResponse
    ): GetCreditScoreData {
        whenever(creditDataRequestMock.getCreditData()).thenReturn(
            Response.Success(creditScoreResponse)
        )
        return creditDataRequestMock
    }

    suspend fun mockErrorGetData(
        creditDataRequestMock: GetCreditScoreData,
        exceptionMock: Exception
    ): GetCreditScoreData {
        whenever(creditDataRequestMock.getCreditData()).thenReturn(
            Response.Error(exceptionMock)
        )
        return creditDataRequestMock
    }

    suspend fun mockViewModelSuccess(
        creditDataRequestMock: GetCreditScoreData,
        creditDataResponseMock: CreditScoreResponse
    ): CreditScoreViewModel {
        whenever(creditDataRequestMock.getCreditData()).thenReturn(
            Response.Success(creditDataResponseMock)
        )

        return CreditScoreViewModel(creditDataRequestMock)
    }

    suspend fun mockViewModelError(
        creditDataRequestMock: GetCreditScoreData,
        exceptionMock: Exception
    ): CreditScoreViewModel {
        whenever(creditDataRequestMock.getCreditData()).thenReturn(
            Response.Error(exceptionMock)
        )

        return CreditScoreViewModel(creditDataRequestMock)
    }
}