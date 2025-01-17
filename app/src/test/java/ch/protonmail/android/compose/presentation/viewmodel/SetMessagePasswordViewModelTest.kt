/*
 * Copyright (c) 2022 Proton AG
 *
 * This file is part of Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see https://www.gnu.org/licenses/.
 */

package ch.protonmail.android.compose.presentation.viewmodel

import ch.protonmail.android.compose.presentation.model.MessagePasswordUiModel
import ch.protonmail.android.compose.presentation.model.SetMessagePasswordUiModel
import ch.protonmail.android.compose.presentation.model.SetMessagePasswordUiModel.Error
import ch.protonmail.android.compose.presentation.model.SetMessagePasswordUiModel.Input
import kotlinx.coroutines.flow.first
import me.proton.core.test.kotlin.CoroutinesTest
import me.proton.core.util.kotlin.EMPTY_STRING
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test suite for [SetMessagePasswordViewModel]
 */
class SetMessagePasswordViewModelTest : CoroutinesTest {

    private val viewModel = SetMessagePasswordViewModel(dispatchers)
    private val testHint = "hint"

    @Test
    fun emptyPasswordAndEmptyRepeat() = coroutinesTest {

        // given
        val password = EMPTY_STRING
        val repeat = EMPTY_STRING
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.Empty),
            repeatInput = Input(repeat, Error.Empty),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = repeat,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun emptyPasswordAnd5CharsRepeat() = coroutinesTest {

        // given
        val password = EMPTY_STRING
        val repeat = "hello"
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.Empty),
            repeatInput = Input(repeat, Error.DoesNotMatch),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = repeat,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun shortPasswordAndEmptyRepeat() = coroutinesTest {

        // given
        val password = "123"
        val repeat = EMPTY_STRING
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.TooShort),
            repeatInput = Input(repeat, Error.Empty),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = repeat,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun shortPasswordAndNotMatchRepeat() = coroutinesTest {

        // given
        val password = "123"
        val repeat = "1234"
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.TooShort),
            repeatInput = Input(repeat, Error.DoesNotMatch),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = repeat,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun shortPasswordAndMatchRepeat() = coroutinesTest {

        // given
        val password = "123"
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.TooShort),
            repeatInput = Input(password, null),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = password,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun longPasswordAndNotMatchRepeat() = coroutinesTest {

        // given
        val password = (0..50).joinToString { it.toString() }
        val repeat = "1234"
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.TooLong),
            repeatInput = Input(repeat, Error.DoesNotMatch),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = repeat,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun longPasswordAndMatchRepeat() = coroutinesTest {

        // given
        val password = (0..50).joinToString { it.toString() }
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, Error.TooLong),
            repeatInput = Input(password, null),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = password,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun okPasswordAndNotMatchRepeat() = coroutinesTest {

        // given
        val password = "12345"
        val repeat = "1234"
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, null),
            repeatInput = Input(repeat, Error.DoesNotMatch),
            hasErrors = true,
            messagePassword = MessagePasswordUiModel.Unset
        )

        // when
        viewModel.validate(
            password = password,
            repeat = repeat,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun okPasswordAndMatchRepeat() = coroutinesTest {

        // given
        val password = "12345"
        val expected = SetMessagePasswordUiModel(
            passwordInput = Input(password, null),
            repeatInput = Input(password, null),
            hasErrors = false,
            messagePassword = MessagePasswordUiModel.Set(password, testHint)
        )

        // when
        viewModel.validate(
            password = password,
            repeat = password,
            hint = testHint
        )
        val result = viewModel.result.first()

        // then
        assertEquals(expected, result)
    }
}
