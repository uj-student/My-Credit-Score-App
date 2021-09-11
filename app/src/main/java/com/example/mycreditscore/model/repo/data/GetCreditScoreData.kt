package com.example.mycreditscore.model.repo.data

import com.example.mycreditscore.model.CreditScoreResponse
import com.example.mycreditscore.model.repo.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCreditScoreData @Inject constructor(private val api: NetworkService) {
    suspend fun getCreditData(): Response<CreditScoreResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                api.getCreditScore()?.let { response ->
                    Response.Success(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e)
            }
//            catch (e: IOException) {
//                e.printStackTrace()
//                Response.Error(e)
//            } catch (e: HttpException) {
//                e.printStackTrace()
//                Response.Error(e)
//            } catch (e: NoSuchElementException) {
//                e.printStackTrace()
//                Response.Error(e)
//            }
        }
    }
}

sealed class Response<out R> {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Nothing>()
}