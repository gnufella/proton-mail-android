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

package ch.protonmail.android.mailbox.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runBlockingTest
import me.proton.core.domain.entity.UserId
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests the behaviour of [ChangeConversationsStarredStatus]
 */
class ChangeConversationsStarredStatusTest {

    private val conversationsRepository = mockk<ConversationsRepository>()

    private lateinit var changeConversationsStarredStatus: ChangeConversationsStarredStatus

    @BeforeTest
    fun setUp() {
        changeConversationsStarredStatus = ChangeConversationsStarredStatus(
            conversationsRepository
        )
    }

    @Test
    fun verifyStarMethodCalledWhenWeReceiveStarAction() {
        runBlockingTest {
            // given
            val conversationIds = listOf("conversation1", "conversation2")
            val userId = UserId("id")
            coEvery { conversationsRepository.star(any(), any()) } just runs

            // when
            changeConversationsStarredStatus(
                conversationIds,
                userId,
                ChangeConversationsStarredStatus.Action.ACTION_STAR
            )

            // then
            coVerify {
                conversationsRepository.star(conversationIds, userId)
            }
        }
    }

    @Test
    fun verifyUnstarMethodCalledWhenWeReceiveUnstarAction() {
        runBlockingTest {
            // given
            val conversationIds = listOf("conversation1", "conversation2")
            val userId = UserId("id")
            coEvery { conversationsRepository.unstar(any(), any()) } just runs

            // when
            changeConversationsStarredStatus(
                conversationIds,
                userId,
                ChangeConversationsStarredStatus.Action.ACTION_UNSTAR
            )

            // then
            coVerify {
                conversationsRepository.unstar(conversationIds, userId)
            }
        }
    }
}