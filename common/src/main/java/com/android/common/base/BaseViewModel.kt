package com.android.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface UserInteraction
interface UiState
interface UiSingleEvent

abstract class BaseViewModel<Event : UserInteraction, State : UiState, SingleEvent : UiSingleEvent>(
    initialState: State
) : ViewModel() {

    // Get Current State
    protected val currentState: State
        get() = uiState.value

    // UiState is current state of views.
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    // UiEvent is the user actions.
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    private val event = _event.asSharedFlow()

    // UiEffect is the side effects like error messages which we want to show only once.
    private val _singleEvent: Channel<SingleEvent> = Channel()
    val singleEvent = _singleEvent.receiveAsFlow()


    /**
     * emit new event
     */
    fun setEvent(event: Event) {
        val newEvent = event
        viewModelScope.launch { _event.emit(newEvent) }
    }


    /**
     * emit new state for the UI
     */
    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     *  emit new single event
     */
    protected fun setSingleEvent(builder: () -> SingleEvent) {
        val effectValue = builder()
        viewModelScope.launch { _singleEvent.send(effectValue) }
    }

    init {
        subscribeUserInteractions()
    }


    /**
     * Start listening to Event
     */
    private fun subscribeUserInteractions() {
        viewModelScope.launch {
            event.collect {
                subscribeUserInteraction(it)
            }
        }
    }

    /**
     * Handle each user interaction
     */
    open fun subscribeUserInteraction(event: Event) {}
}