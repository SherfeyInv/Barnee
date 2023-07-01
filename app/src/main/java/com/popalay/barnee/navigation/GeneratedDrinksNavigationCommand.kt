/*
 * Copyright (c) 2023 Denys Nykyforov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.popalay.barnee.navigation

import androidx.navigation.NavBackStackEntry
import com.popalay.barnee.data.repository.DrinksRequest
import com.popalay.barnee.domain.navigation.GeneratedDrinksDestination
import com.popalay.barnee.domain.navigation.RouteProvider
import com.popalay.barnee.domain.parameterizeddrinklist.ParameterizedDrinkListInput

object GeneratedDrinksNavigationCommand : NavigationCommand<ParameterizedDrinkListInput>,
    RouteProvider by GeneratedDrinksDestination {

    override fun parseInput(backStackEntry: NavBackStackEntry): ParameterizedDrinkListInput =
        ParameterizedDrinkListInput(
            request = DrinksRequest.Generated,
            emptyStateMessage = "No have no generated drinks yet.\nLet's make some! 🍸",
            title = "Your Bartender",
        )
}