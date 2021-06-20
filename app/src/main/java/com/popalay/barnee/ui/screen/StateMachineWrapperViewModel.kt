package com.popalay.barnee.ui.screen

import androidx.lifecycle.ViewModel
import com.popalay.barnee.domain.Action
import com.popalay.barnee.domain.SideEffect
import com.popalay.barnee.domain.State
import com.popalay.barnee.domain.StateMachine
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class StateMachineWrapperViewModel<S : State, A : Action, SE : SideEffect>(
    private val stateMachine: StateMachine<S, A, *, SE>
) : ViewModel() {
    val stateFlow: StateFlow<S> = stateMachine.stateFlow
    val sideEffectFlow: SharedFlow<SE> = stateMachine.sideEffectFlow

    fun processAction(action: A) {
        stateMachine.process(action)
    }

    override fun onCleared() {
        super.onCleared()
        stateMachine.clear()
    }
}
