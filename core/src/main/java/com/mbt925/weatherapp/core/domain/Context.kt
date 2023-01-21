package com.mbt925.weatherapp.core.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


interface ContextState<State> {
    val state: StateFlow<State>
    fun reduce(reducer: (State) -> State)
}

interface ContextExecutor<State> {
    suspend fun execute(useCase: UseCase<State>)
}

interface Context<State>: ContextState<State>, ContextExecutor<State> {
    override suspend fun execute(useCase: UseCase<State>) = useCase(this)

    companion object {
        operator fun <State> invoke(
            initialState: State,
        ): Context<State> = ContextImpl(
            initialState = initialState,
        )
    }
}

internal class ContextImpl<State>(
    initialState: State,
) : Context<State> {

    override val state: MutableStateFlow<State> = MutableStateFlow(initialState)

    override fun reduce(reducer: (State) -> State) {
        state.update { state -> reducer(state) }
    }
}
