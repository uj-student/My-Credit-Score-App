package com.example.mycreditscore

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import java.lang.reflect.InvocationTargetException

object BaseUtil {
    private const val RES_ID = "${BuildConfig.APPLICATION_ID}:id/"
    const val LONG_WAIT = 2000L
    const val MEDIUM_WAIT = 500L

    @Throws(InterruptedException::class)
    fun waitForScreenToLoad(milliseconds: Long) {
        Thread.sleep(milliseconds)
    }

    @Throws(UiObjectNotFoundException::class)
    fun confirmTextMatches(resource: String, expectedText: String?) {
        var fieldName = "$RES_ID$resource"
        fieldName = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .findObject(UiSelector().resourceId(fieldName)).text
        Assert.assertEquals(expectedText, fieldName)
    }

    fun isTextDisplayed(text: String) {
        Espresso.onView(ViewMatchers.withText(text)).check(matches(isDisplayed()))
    }

    @Throws(UiObjectNotFoundException::class)
    fun clickButton() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .findObject(UiSelector().resourceId("${RES_ID}btnShowMoreDetails"))
            .clickAndWaitForNewWindow()
    }

    fun scrollScreen() {
        try {
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .findObject(UiSelector().scrollable(true))
                .swipeUp(10)
        } catch (ex: UiObjectNotFoundException) {
            ex.message?.let {
                Log.d("Unable to scroll: ", it)
            }
        }
    }

    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class,
        NoSuchMethodException::class,
        InvocationTargetException::class
    )
    fun setMobileDataEnabled(context: Context, enabled: Boolean) {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectManagerClass = Class.forName(connectManager.javaClass.name)
        val connectivityManagerField = connectManagerClass.getDeclaredField("mService")
        connectivityManagerField.isAccessible = true
        val connectivityManager = connectivityManagerField.get(connectManager)
        val connectivityManagerClass = Class.forName(connectivityManager.javaClass.name)
        val setMobileDataEnabledMethod =
            connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", java.lang.Boolean.TYPE)
        setMobileDataEnabledMethod.isAccessible = true
        setMobileDataEnabledMethod.invoke(connectivityManager, enabled)
    }

}