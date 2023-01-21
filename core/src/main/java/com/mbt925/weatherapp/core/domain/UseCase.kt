package com.mbt925.weatherapp.core.domain

fun interface UseCase<State> : suspend (ContextState<State>) -> Unit

fun <State> useCase(body: suspend ContextState<State>.() -> Unit) = UseCase { model -> body(model) }
