package com.example.mycreditscore.model

import java.io.Serializable

data class CreditScoreResponse(
    val accountIDVStatus: String? = null,
    val creditReportInfo: CreditReportInfo? = null,
    val dashboardStatus: String? = null,
    val personaType: String? = null,
    val coachingSummary: CoachingSummary? = null,
    val augmentedCreditScore: String? = null
): Serializable

data class CreditReportInfo(
    val score: Int?,
    val scoreBand: Int?,
    val clientRef: String?,
    val status: String?,
    val maxScoreValue: Int?,
    val minScoreValue: Int?,
    val monthsSinceLastDefaulted: Int?,
    val hasEverDefaulted: Boolean?,
    val monthsSinceLastDelinquent: Int?,
    val hasEverBeenDelinquent: Boolean?,
    val percentageCreditUsed: Int?,
    val percentageCreditUsedDirectionFlag: Int?,
    val changedScore: Int?,
    val currentShortTermDebt: Int?,
    val currentShortTermNonPromotionalDebt: Int?,
    val currentShortTermCreditLimit: Int?,
    val currentShortTermCreditUtilisation: Int?,
    val changeInShortTermDebt: Int?,
    val currentLongTermDebt: Int?,
    val currentLongTermNonPromotionalDebt: Int?,
    val currentLongTermCreditLimit: Any?,
    val currentLongTermCreditUtilisation: Any?,
    val changeInLongTermDebt: Int?,
    val numPositiveScoreFactors: Int?,
    val numNegativeScoreFactors: Int?,
    val equifaxScoreBand: Int?,
    val equifaxScoreBandDescription: String?,
    val daysUntilNextReport: Int?
): Serializable

data class CoachingSummary(
    val activeTodo: Boolean?,
    val activeChat: Boolean?,
    val numberOfTodoItems: Int?,
    val numberOfCompletedTodoItems: Int?,
    val selected: Boolean?
): Serializable