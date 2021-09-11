package com.example.mycreditscore

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.UiObjectNotFoundException
import com.example.mycreditscore.BaseUtil.LONG_WAIT
import com.example.mycreditscore.BaseUtil.MEDIUM_WAIT
import com.example.mycreditscore.BaseUtil.clickButton
import com.example.mycreditscore.BaseUtil.confirmTextMatches
import com.example.mycreditscore.BaseUtil.isTextDisplayed
import com.example.mycreditscore.BaseUtil.scrollScreen
import com.example.mycreditscore.BaseUtil.setMobileDataEnabled
import com.example.mycreditscore.BaseUtil.waitForScreenToLoad
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.InvocationTargetException


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainUiTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    @Throws(InterruptedException::class, UiObjectNotFoundException::class)
    fun mainActivityTest() {
        waitForScreenToLoad(LONG_WAIT)
        confirmTextMatches("tvScoreText", "Your credit score is")
        confirmTextMatches("tvMaxValue", "out of 700")
        confirmTextMatches("btnShowMoreDetails", "SHOW REPORT")
        clickButton()
        waitForScreenToLoad(MEDIUM_WAIT)
        confirmTextMatches("creditScoreValue", "PASS")
        scrollScreen()
        waitForScreenToLoad(LONG_WAIT)
        scrollScreen()
        waitForScreenToLoad(LONG_WAIT)
        isTextDisplayed("514")
        isTextDisplayed("INEXPERIENCED")
    }

//    @Test
    @Throws(
        InterruptedException::class,
        UiObjectNotFoundException::class,
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class,
        NoSuchMethodException::class,
        InvocationTargetException::class,
        NoSuchFieldException::class)
    fun offlineTest() {
        mActivityTestRule.scenario.onActivity {
            setMobileDataEnabled(it.baseContext, false)
        }
        waitForScreenToLoad(10000)

    }

}
