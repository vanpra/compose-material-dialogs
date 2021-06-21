package com.vanpra.composematerialdialogs.test.utils

import com.karumi.shot.ScreenshotTest
import org.junit.runner.Description
import org.junit.runner.manipulation.Filter

class ScreenshotTestFilter : Filter() {
    override fun shouldRun(description: Description): Boolean =
        ScreenshotTest::class.java.isAssignableFrom(description.testClass)

    override fun describe(): String =
        "all tests implementing com.karumi.shot.ScreenshotTest interface"
}

class NotScreenshotTestFilter : Filter() {
    override fun shouldRun(description: Description): Boolean =
        !ScreenshotTest::class.java.isAssignableFrom(description.testClass)

    override fun describe(): String =
        "all tests not implementing com.karumi.shot.ScreenshotTest interface"
}
