package com.mbt925.weatherapp.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


inline fun <reified T> Flow<T?>.toStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
): StateFlow<T?> = stateIn(scope, started, null)

inline fun <reified T> Flow<T>.toStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    initialValue: T,
): StateFlow<T> = stateIn(scope, started, initialValue)
