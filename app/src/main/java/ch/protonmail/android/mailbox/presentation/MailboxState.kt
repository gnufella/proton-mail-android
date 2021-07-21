/*
 * Copyright (c) 2020 Proton Technologies AG
 *
 * This file is part of ProtonMail.
 *
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail. If not, see https://www.gnu.org/licenses/.
 */

package ch.protonmail.android.mailbox.presentation

import ch.protonmail.android.mailbox.presentation.model.MailboxUiItem
import me.proton.core.util.kotlin.EMPTY_STRING

sealed class MailboxState {
    object Loading : MailboxState()
    data class Error(
        val error: String = EMPTY_STRING,
        val throwable: Throwable?
    ) : MailboxState()

    data class Data(
        val items: List<MailboxUiItem> = emptyList(),
        val noMoreItems: Boolean = false
    ) : MailboxState()
}