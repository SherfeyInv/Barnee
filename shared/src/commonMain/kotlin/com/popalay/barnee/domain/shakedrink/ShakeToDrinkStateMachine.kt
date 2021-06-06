package com.popalay.barnee.domain.shakedrink

import com.popalay.barnee.data.device.ShakeDetector
import com.popalay.barnee.data.model.Drink
import com.popalay.barnee.data.repository.DrinkRepository
import com.popalay.barnee.data.repository.DrinksRequest
import com.popalay.barnee.domain.Action
import com.popalay.barnee.domain.Mutation
import com.popalay.barnee.domain.Result
import com.popalay.barnee.domain.State
import com.popalay.barnee.domain.StateMachine
import com.popalay.barnee.domain.Success
import com.popalay.barnee.domain.Uninitialized
import com.popalay.barnee.domain.flatMapToResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.take

data class ShakeToDrinkState(
    val randomDrink: Result<Drink> = Uninitialized(),
    val shouldShow: Boolean = false
) : State

sealed class ShakeToDrinkAction : Action {
    object Initial : ShakeToDrinkAction()
    object DialogDismissed : ShakeToDrinkAction()
    object Retry : ShakeToDrinkAction()
}

sealed class ShakeToDrinkMutation : Mutation {
    data class RandomDrink(val data: Result<Drink>) : ShakeToDrinkMutation()
}

class ShakeToDrinkStateMachine(
    drinkRepository: DrinkRepository,
    shakeDetector: ShakeDetector
) : StateMachine<ShakeToDrinkState, ShakeToDrinkAction, ShakeToDrinkMutation, Nothing>(
    initialState = ShakeToDrinkState(),
    initialAction = ShakeToDrinkAction.Initial,
    processor = { state, _ ->
        merge(
            filterIsInstance<ShakeToDrinkAction.Initial>()
                .take(1)
                .flatMapMerge { detectShakes(shakeDetector) }
                .flatMapToResult { drinkRepository.getDrinks(DrinksRequest.Random(1)).map { it.first() } }
                .filter { !(it is Success<Drink> && state().randomDrink is Uninitialized) }
                .map { ShakeToDrinkMutation.RandomDrink(it) },
            filterIsInstance<ShakeToDrinkAction.Retry>()
                .flatMapToResult { drinkRepository.getDrinks(DrinksRequest.Random(1)).map { it.first() } }
                .map { ShakeToDrinkMutation.RandomDrink(it) },
            filterIsInstance<ShakeToDrinkAction.DialogDismissed>()
                .map { ShakeToDrinkMutation.RandomDrink(Uninitialized()) },
        )
    },
    reducer = { mutation ->
        when (mutation) {
            is ShakeToDrinkMutation.RandomDrink -> copy(
                randomDrink = mutation.data,
                shouldShow = mutation.data !is Uninitialized
            )
        }
    }
)

private fun detectShakes(shakeDetector: ShakeDetector) = callbackFlow {
    shakeDetector.start {
        try {
            offer(true)
        } catch (e: Exception) {
            // Handle exception from the channel: failure in flow or premature closing
        }
    }
    awaitClose {
        shakeDetector.stop()
    }
}