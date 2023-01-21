package com.mbt925.weatherapp.core.data

import com.mbt925.weatherapp.core.test.TaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule

@ExperimentalTime
@ExperimentalCoroutinesApi
class DataAccessTest {

    @get:Rule
    val rule = TaskExecutorRule()

    @Test
    fun onNoDataPresent_whenGet_returnsNull() = rule.runTest {
        assertNull(DataAccess<Any> { error("") }.get())
    }

    @Test
    fun onNoDataPresent_whenGetOrFetch_fetches_andReturnsFetchedData() = rule.runTest {
        val data = "data"
        assertSame(data, DataAccess<Any> { data }.getOrFetch())
    }

    @Test
    fun onDataPresent_whenGetOrFetch_notFetch_andReturnsCachedData() = rule.runTest {
        val data = "data"
        val fetch = mockk<suspend () -> String> {
            coEvery { this@mockk() } returns data
        }
        val access = DataAccess<Any> { fetch() }
        assertSame(data, access.getOrFetch())
        assertSame(data, access.getOrFetch())
        coVerify(exactly = 1) { fetch() }
    }

    @Test
    fun getOrFetch_checksLocal() {
        checksLocal { flowOf(getOrFetch()) }
    }

    @Test
    fun getOrFetch_noCache_callsRemote() {
        noCache_callsRemote { flowOf(getOrFetch()) }
    }

    @Test
    fun getOrFetch_noCache_onRemoteSuccess_cachesRemote() {
        noCache_onRemoteSuccess_cachesRemote { flowOf(getOrFetch()) }
    }

    @Test
    fun getOrFetch_withCache_notCallRemote() {
        withCache_notCallRemote { flowOf(getOrFetch()) }
    }

    @Test
    fun getOrFetch_noCache_afterCaching_notCallRemoteTwice() {
        noCache_afterCaching_notCallRemoteTwice { flowOf(getOrFetch()) }
    }

    @Test
    fun getAndFetch_checksLocal() {
        checksLocal { getAndFetch() }
    }

    @Test
    fun getAndFetch_noCache_callsRemote() {
        noCache_callsRemote { getAndFetch() }
    }

    @Test
    fun getAndFetch_noCache_onRemoteSuccess_cachesRemote() {
        noCache_onRemoteSuccess_cachesRemote { getAndFetch() }
    }

    @Test
    fun getAndFetch_withCache_callsRemote() {
        withCache_callsRemote { getAndFetch() }
    }

    @Test
    fun getAndFetch_noCache_afterCaching_callsRemoteTwice() {
        noCache_afterCaching_callsRemoteMultipleTimes { getAndFetch() }
    }

    @Test
    fun getAndFetch_noCache_multipleFetches_callsRemoteOnce() {
        noCache_parallelCalls_callsRemoteOnce { getAndFetch() }
    }

    @Test
    fun clearAndFetch_clearsCache() {
        clearsCache { flowOf(clearAndFetch()) }
    }

    @Test
    fun clearAndFetch_checksLocal() {
        checksLocal { flowOf(clearAndFetch()) }
    }

    @Test
    fun clearAndFetch_callsRemote() {
        callsRemote { flowOf(clearAndFetch()) }
    }

    @Test
    fun clearAndFetch_onRemoteSuccess_cachesRemote() {
        noCache_onRemoteSuccess_cachesRemote { flowOf(clearAndFetch()) }
    }

    @Test
    fun clearAndFetch_afterCaching_callsRemoteTwice_clearsCacheTwice() {
        afterCaching_callsRemoteTwice_clearsCacheTwice { flowOf(clearAndFetch()) }
    }

    @Test
    fun poll_checksLocal() {
        checksLocal { poll(100).take(5) }
    }

    @Test
    fun poll_noCache_callsRemote() {
        noCache_callsRemote { poll(100).take(5) }
    }

    @Test
    fun poll_noCache_onRemoteSuccess_cachesRemote() {
        noCache_onRemoteSuccess_cachesRemote { poll(100).take(5) }
    }

    @Test
    fun poll_withCache_callsRemote() {
        withCache_callsRemote { poll(100).take(5) }
    }

    @Test
    fun poll_noCache_afterCaching_callsRemoteTwice() {
        noCache_afterCaching_callsRemoteMultipleTimes { poll(100).take(5) }
    }

    private fun <T> checksLocal(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        var getInvoked = false
        DataAccess(
            get = { getInvoked = true },
            save = { true },
            fetch = { },
        ).block().collect()
        assertTrue(getInvoked)
    }

    private fun <T> noCache_callsRemote(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        var cache: Int? = null
        var fetchInvoked = false
        DataAccess(
            get = { cache },
            save = { cache = it; true },
            fetch = { fetchInvoked = true; 0 },
        ).block().collect()
        assertTrue(fetchInvoked)
    }

    private fun <T> noCache_onRemoteSuccess_cachesRemote(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        var cache: Int? = null
        DataAccess(
            get = { cache },
            save = { cache = it; true },
            fetch = { 0 },
        ).block().collect()
        assertEquals(0, cache)
    }

    private fun <T> withCache_notCallRemote(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        DataAccess(
            get = { 0 },
            save = { false },
            fetch = { throw RuntimeException("did not expect to fetch") },
        ).block().collect()
    }

    private fun <T> noCache_afterCaching_notCallRemoteTwice(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        val putInvocations = mutableListOf<Int?>()
        var fetchInvocations = 0
        val dataAccess = DataAccess(
            get = { putInvocations.lastOrNull() },
            save = { putInvocations.add(it) },
            fetch = { fetchInvocations++; 0 },
        )
        repeat(2) { dataAccess.block().collect() }
        assertEquals(1, fetchInvocations)
    }

    private fun <T> withCache_callsRemote(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        var fetchInvoked = false
        DataAccess(
            get = { 0 },
            save = { false },
            fetch = { fetchInvoked = true },
        ).block().collect()
        assertTrue(fetchInvoked)
    }

    private fun <T> noCache_afterCaching_callsRemoteMultipleTimes(
        block: DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        val putInvocations = mutableListOf<Int?>()
        var fetchInvocations = 0
        val dataAccess = DataAccess(
            get = { putInvocations.lastOrNull() },
            save = { putInvocations.add(it) },
            fetch = { fetchInvocations++; 0 },
        )
        repeat(2) { dataAccess.block().collect() }
        assertTrue(fetchInvocations > 1)
    }

    private fun <T> noCache_parallelCalls_callsRemoteOnce(
        block: DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        val putInvocations = mutableListOf<Int?>()
        val fetchInvocations = AtomicInteger(0)
        val dataAccess = DataAccess(
            get = { putInvocations.lastOrNull() },
            save = { putInvocations.add(it) },
            fetch = {
                delay(10)
                fetchInvocations.getAndIncrement(); 0
            },
        )

        launch { dataAccess.block().collect() }
        launch { dataAccess.block().collect() }

        advanceUntilIdle()
        assertEquals(1, fetchInvocations.get())
    }

    private fun <T> clearsCache(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        var clearInvoked = false
        DataAccess(
            get = { },
            save = { clearInvoked = (clearInvoked || it == null); true },
            fetch = { },
        ).block().collect()
        assertTrue(clearInvoked)
    }

    private fun <T> callsRemote(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        var fetchInvoked = false
        DataAccess(
            get = { },
            save = { false },
            fetch = { fetchInvoked = true },
        ).block().collect()
        assertTrue(fetchInvoked)
    }

    private fun <T> afterCaching_callsRemoteTwice_clearsCacheTwice(
        block: suspend DataAccess<*>.() -> Flow<T>,
    ) = rule.runTest {
        val putInvocations = mutableListOf<Int?>()
        var fetchInvocations = 0
        val dataAccess = DataAccess(
            get = { putInvocations.lastOrNull() },
            save = { putInvocations.add(it) },
            fetch = { fetchInvocations++; 0 },
        )
        repeat(2) { dataAccess.block().collect() }
        assertEquals(2, fetchInvocations)
        assertEquals(2, putInvocations.count { it == null })
    }

}
