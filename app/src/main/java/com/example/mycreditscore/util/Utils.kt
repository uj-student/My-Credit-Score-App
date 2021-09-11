package com.example.mycreditscore.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.mycreditscore.model.CoachingSummary
import com.example.mycreditscore.model.CreditReportInfo
import com.example.mycreditscore.model.CreditScoreResponse

fun getDonutProgress(score: Int, possibleMax: Int): Int {
    return if (score < 1 || possibleMax < 1) 0 else if (score > possibleMax) 100 else (score * 100) / possibleMax
}

fun responseDataToMap(creditScoreResponse: CreditScoreResponse): Map<String, Any?> {
    val creditDataMap = mutableMapOf<String, Any?>()

    for (creditData in CreditScoreResponse::class.java.declaredFields) {
        creditData.isAccessible = true
        if (creditData.get(creditScoreResponse) == null)
            continue
        if (creditData.type == CreditReportInfo::class.java) {
            val creditReportInfo = creditScoreResponse.creditReportInfo
            for (property in CreditReportInfo::class.java.declaredFields) {
                property.isAccessible = true
                property.get(creditReportInfo)?.let { creditItem ->
                    if (shouldAddProperty(creditItem))
                        creditDataMap[property.name] = creditItem
                }
            }
            continue
        }
        if (creditData.type == CoachingSummary::class.java) {
            val coachingSummary = creditScoreResponse.coachingSummary
            for (property in CoachingSummary::class.java.declaredFields) {
                property.isAccessible = true
                property.get(coachingSummary)?.let { creditItem ->
                    if (shouldAddProperty(creditItem))
                        creditDataMap[property.name] = creditItem
                }
            }
            continue
        }
        creditDataMap[creditData.name] = creditData.get(creditScoreResponse)
    }
    return creditDataMap
}

fun isPropertyNotZero(input: Int): Boolean {
    return input > 0
}

fun shouldAddProperty(input: Any): Boolean {
    if (input is Int && isPropertyNotZero(input))
        return true
    else if (input is String)
        return true
    else if (input is Boolean)
        return true
    return false
}

fun addCreditItemToMap(creditDataMap: MutableMap<String, Any?>): MutableMap<String, Any?> {
    return creditDataMap
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return networkCapabilities != null && networkCapabilities.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_INTERNET
    ) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}


