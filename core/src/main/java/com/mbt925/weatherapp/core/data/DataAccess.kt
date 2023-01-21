package com.mbt925.weatherapp.core.data

import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive

/**
 * Handling remote and local data should be done in a unified way.
 * This interface exposes different strategies to read data from local and remote sources.
 * Data is always stored locally, and then returned
 */
interface DataAccess<T> {

    /**
     * @return local data if present or null
     */
    suspend fun get(): T?

    /**
     * @return local data if present, or otherwise fetched remote data
     */
    suspend fun getOrFetch(): T

    /**
     * @return fetched remote data, after clearing local data
     */
    suspend fun clearAndFetch(): T

    /**
     * @return A flow which upon collecting first emits local data if present,
     * then fetches remote data and emits the fetched data.
     */
    fun getAndFetch(): Flow<T>

    /**
     * @return A flow which upon collection, first emits local data if present,
     * then keeps fetching and emitting remote data as long as the collecting
     * coroutine job is active.
     */
    fun poll(interval: Long = DEFAULT_POLLING_INTERVAL): Flow<T>

    companion object {
        const val DEFAULT_POLLING_INTERVAL = 15_000L
    }
}

/**
 * Factory method to instantiate [DataAccess] with mapping between local and remote types.
 * Saving and getting locals is delegated.
 */
operator fun <Data> DataAccess.Companion.invoke(
    log: (String) -> Unit = {},
    get: suspend () -> Data?,
    save: suspend (Data?) -> Boolean,
    fetch: suspend () -> Data,
): DataAccess<Data> = DataAccessImpl(
    log = log,
    get = get,
    save = save,
    fetch = fetch,
)

/**
 * Factory method to instantiate [DataAccess].
 * Fetched data gets saved to and read from memory.
 */
operator fun <Remote> DataAccess.Companion.invoke(
    log: (String) -> Unit = {},
    fetch: suspend () -> Remote,
): DataAccess<Remote> {
    var data: Remote? = null
    return DataAccess(
        get = { data },
        save = { data = it; true },
        fetch = fetch,
        log = log,
    )
}

open class DataAccessImpl<Data>(
    val log: (String) -> Unit,
    val get: suspend () -> Data?,
    val save: suspend (Data?) -> Boolean,
    val fetch: suspend () -> Data,
) : DataAccess<Data> {

    private val isFetching = AtomicBoolean(false)

    private fun <Data> Flow<Data>.flowLogging() =
        onStart { log("Starting flow") }
            .onEach { log("Emitting value: $it") }
            .onCompletion { log("Ending flow") }

    private suspend fun FlowCollector<Data>.emitIfNotNull(): Data? {
        log("Checking local data")
        return get()
            ?.also { log("Found local data") }
            ?.also { emit(it) }
            .also { if (it == null) log("No local data found") }
    }

    private suspend fun fetchAndSave(
        onSaved: suspend () -> Unit = {},
        onNotSaved: suspend (Data) -> Unit = {},
    ) {
        if (isFetching.compareAndSet(false, true)) {
            try {
                log("Fetching remote data")
                fetch()
                    .also { log("Saving remote value: $it") }
                    .also {
                        if (save(it)) onSaved() else onNotSaved(it)
                    }
            } finally {
                isFetching.set(false)
            }
        } else {
            log("Skip fetching remote data as call is already running")
            while (isFetching.get()) {
                delay(10) // wait to ensure further calls have the updated local state
            }
        }
    }

    private suspend fun FlowCollector<Data>.fetchSaveEmit() {
        fetchAndSave(
            onSaved = { emitIfNotNull() },
            onNotSaved = { emit(it) }
        )
    }

    private suspend fun clear() {
        log("Clear local data")
        save(null)
    }

    override suspend fun get() = get.invoke()
    override suspend fun getOrFetch() = flow { emitIfNotNull() ?: fetchSaveEmit() }
        .flowLogging()
        .first()

    override suspend fun clearAndFetch() = flow { fetchSaveEmit() }
        .onStart { clear() }
        .flowLogging()
        .first()

    override fun getAndFetch() = flow { fetchSaveEmit() }
        .onStart { emitIfNotNull() }
        .flowLogging()

    override fun poll(interval: Long) = flow {
        while (currentCoroutineContext().isActive) {
            fetchSaveEmit()
            delay(interval)
        }
    }
        .onStart { emitIfNotNull() }
        .flowLogging()
}
