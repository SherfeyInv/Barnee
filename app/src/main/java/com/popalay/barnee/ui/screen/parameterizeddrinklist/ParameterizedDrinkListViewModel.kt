package com.popalay.barnee.ui.screen.parameterizeddrinklist

import com.popalay.barnee.domain.EmptySideEffect
import com.popalay.barnee.domain.parameterizeddrinklist.ParameterizedDrinkListAction
import com.popalay.barnee.domain.parameterizeddrinklist.ParameterizedDrinkListState
import com.popalay.barnee.domain.parameterizeddrinklist.ParameterizedDrinkListStateMachine
import com.popalay.barnee.ui.screen.StateMachineWrapperViewModel

class ParameterizedDrinkListViewModel(
    stateMachine: ParameterizedDrinkListStateMachine
) : StateMachineWrapperViewModel<ParameterizedDrinkListState, ParameterizedDrinkListAction, EmptySideEffect>(stateMachine)
