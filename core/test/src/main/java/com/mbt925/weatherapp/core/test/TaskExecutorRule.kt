@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mbt925.weatherapp.core.test

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TaskExecutorRule(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    fun runTest(
        timeout: Duration = 1.seconds,
        block: suspend TestScope.() -> Unit,
    ) = runTest(dispatchTimeoutMs = timeout.inWholeMilliseconds) {
        block()
        advanceUntilIdle()
        coroutineContext.cancelChildren()
    }

}

fun TestScope.testLaunch(
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = UnconfinedTestDispatcher(testScheduler),
    block = block,
)
