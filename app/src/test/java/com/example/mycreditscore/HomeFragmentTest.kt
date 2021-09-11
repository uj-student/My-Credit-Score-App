package com.example.mycreditscore

import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mycreditscore.view.HomeFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun testHomeFragment() {
        val scenario = launchFragment<HomeFragment>()
        scenario.recreate()
    }
}