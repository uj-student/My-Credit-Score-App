package com.example.mycreditscore

import com.example.mycreditscore.model.CreditScoreResponse
import com.example.mycreditscore.model.repo.data.GetCreditScoreData
import com.example.mycreditscore.model.repo.data.Response
import com.example.mycreditscore.testUtils.BaseUnitTest
import com.example.mycreditscore.util.getDonutProgress
import com.example.mycreditscore.util.isPropertyNotZero
import com.example.mycreditscore.util.responseDataToMap
import com.example.mycreditscore.util.shouldAddProperty
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class AppUnitTests : BaseUnitTest() {

    private val creditDataRequestMock: GetCreditScoreData = mock()
    private val creditDataResponseMock: CreditScoreResponse = mock()
    private val creditMapMock: Map<String, Any?> = mock()
    private val viewModelState: StateFlow<Response<CreditScoreResponse>?> = mock()
    private val exceptionMock: Exception = mock()
    private val sampleCreditResponse = MockResponse.getMockedResponse()

    /*
      unit tests below - mainly Util.kt
     */
    @Test
    fun shouldGetPositiveRating() {
        assertEquals(getDonutProgress(score = 100, possibleMax = 500), 20)
    }

    @Test
    fun shouldGetZeroRating() {
        assertEquals(getDonutProgress(score = -100, possibleMax = 500), 0)
    }

    @Test
    fun shouldGetHundredRating() {
        assertEquals(getDonutProgress(score = 100, possibleMax = 50), 100)
    }

    @Test
    fun shouldGetPropertyIsZero() {
        assertFalse(isPropertyNotZero(-4))
        assertFalse(isPropertyNotZero(0))
        assertFalse(isPropertyNotZero(0.0.toInt()))
        assertFalse(isPropertyNotZero(-1))
        assertFalse(isPropertyNotZero(-90000))
    }

    @Test
    fun shouldGetPropertyNotZero() {
        assertTrue(isPropertyNotZero(1))
        assertTrue(isPropertyNotZero(2))
        assertTrue(isPropertyNotZero(100))
        assertTrue(isPropertyNotZero(30600))
        assertTrue(isPropertyNotZero("50".toInt()))
    }

    @Test
    fun shouldNotAddProperty() {
        assertFalse(shouldAddProperty(0))
        assertFalse(shouldAddProperty(10.78))
        assertFalse(shouldAddProperty(listOf<Any>()))
        assertFalse(shouldAddProperty((11 * 4).toFloat()))
        assertFalse(shouldAddProperty(creditDataResponseMock))
    }

    @Test
    fun shouldAddProperty() {
        assertTrue(shouldAddProperty(1))
        assertTrue(shouldAddProperty(10.78.toInt()))
        assertTrue(shouldAddProperty("Good Credit"))
        assertTrue(shouldAddProperty(true))
        assertTrue(shouldAddProperty(false))
        assertTrue(shouldAddProperty(false.toString()))
    }

    @Test
    fun shouldGetEmptyMap() {
        val creditScoreResponseMock: CreditScoreResponse = mock()
        val result = responseDataToMap(creditScoreResponseMock)
        assertTrue(result.isEmpty())
    }

    @Test
    fun shouldGetPopulatedMap() {
        val result = responseDataToMap(sampleCreditResponse)
        assertTrue(result.isNotEmpty())
        assertEquals(result.size, 27)
    }

    @Test
    fun shouldGetMapFromResponse() {
        assertNotSame(responseDataToMap(sampleCreditResponse), creditMapMock)
    }

    /*
      GetData / network request tests below
     */
    @Test
    fun shouldCallSuccessDataOnce() = runBlockingTest {
        val mockCreditData = mockSuccessfulGetData(creditDataRequestMock, creditDataResponseMock)
        mockCreditData.getCreditData()
        verify(mockCreditData, times(1)).getCreditData()
    }

    @Test
    fun shouldCallErrorDataOnce() = runBlockingTest {
        val mockCreditData = mockErrorGetData(creditDataRequestMock, exceptionMock)
        mockCreditData.getCreditData()
        verify(mockCreditData, times(1)).getCreditData()
    }

    @Test
    fun shouldGetSuccessResponse() = runBlockingTest {
        val mockCreditData = mockSuccessfulGetData(creditDataRequestMock, creditDataResponseMock)
        val response = mockCreditData.getCreditData()
        assert(response is Response.Success)
    }

    @Test
    fun shouldGetErrorResponse() = runBlockingTest {
        val mockCreditData = mockErrorGetData(creditDataRequestMock, exceptionMock)
        val response = mockCreditData.getCreditData()
        assert(response is Response.Error)
    }

    /*
      viewModel tests below
     */
    @Test
    fun shouldGetSuccessInViewModel() = runBlockingTest {
        val viewModelMock = mockViewModelSuccess(creditDataRequestMock, creditDataResponseMock)
        assertEquals(viewModelMock.state.value, Response.Success(creditDataResponseMock))
    }

    @Test
    fun shouldGetErrorInViewModel() = runBlockingTest {
        val viewModelMock = mockViewModelError(creditDataRequestMock, exceptionMock)
        assertEquals(viewModelMock.state.value, Response.Error(exceptionMock))
    }

    //
    @Test
    fun shouldValidateResponseData() {

    }

}
