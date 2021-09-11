package com.example.mycreditscore

import com.example.mycreditscore.model.CoachingSummary
import com.example.mycreditscore.model.CreditReportInfo
import com.example.mycreditscore.model.CreditScoreResponse

object MockResponse {
    fun getMockedResponse(): CreditScoreResponse {
        return CreditScoreResponse(
            accountIDVStatus = "PASS",
            creditReportInfo = CreditReportInfo(
                score = 514,
                scoreBand = 4,
                clientRef = "CS-SED-655426-708782",
                status = "MATCH",
                maxScoreValue = 700,
                minScoreValue = 0,
                monthsSinceLastDefaulted = -1,
                hasEverDefaulted = false,
                monthsSinceLastDelinquent = 1,
                hasEverBeenDelinquent = true,
                percentageCreditUsed = 44,
                percentageCreditUsedDirectionFlag = 1,
                changedScore = 0,
                currentShortTermDebt = 13758,
                currentShortTermNonPromotionalDebt = 13758,
                currentShortTermCreditLimit = 30600,
                currentShortTermCreditUtilisation = 44,
                changeInShortTermDebt = 549,
                currentLongTermDebt = 24682,
                currentLongTermNonPromotionalDebt = 24682,
                currentLongTermCreditLimit = null,
                currentLongTermCreditUtilisation = null,
                changeInLongTermDebt = -327,
                numPositiveScoreFactors = 9,
                numNegativeScoreFactors = 0,
                equifaxScoreBand = 4,
                equifaxScoreBandDescription = "Excellent",
                daysUntilNextReport = 9
            ),
            dashboardStatus = "PASS",
            personaType = "INEXPERIENCED",
            coachingSummary = CoachingSummary(
                activeTodo = false,
                activeChat = true,
                numberOfTodoItems = 0,
                numberOfCompletedTodoItems = 0,
                selected = true
            ),
            augmentedCreditScore = null
        )
    }
}
