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

import ch.protonmail.android.core.Constants
import ch.protonmail.android.mailbox.domain.model.ConversationsActionResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import me.proton.core.domain.entity.UserId
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test the behavior of [ChangeConversationsReadStatus]
 */
class ChangeConversationsReadStatusTest {

    private val conversationsRepository = mockk<ConversationsRepository>()

    private lateinit var changeConversationsReadStatus: ChangeConversationsReadStatus

    @BeforeTest
    fun setUp() {
        changeConversationsReadStatus = ChangeConversationsReadStatus(
            conversationsRepository
        )
    }

    @Test
    fun verifyMarkReadIsCalledWhenReadActionIsReceived() {
        runBlockingTest {
            // given
            val conversation1 = "conversation1"
            val conversation2 = "conversation2"
            val conversationIds = listOf(conversation1, conversation2)
            val userId = UserId("id")
            coEvery {
                conversationsRepository.markRead(conversationIds, userId)
            } returns ConversationsActionResult.Success

            // when
            changeConversationsReadStatus(
                conversationIds,
                ChangeConversationsReadStatus.Action.ACTION_MARK_READ,
                userId,
                Constants.MessageLocationType.ARCHIVE,
                Constants.MessageLocationType.ARCHIVE.messageLocationTypeValue.toString()
            )

            // then
            coVerify {
                conversationsRepository.markRead(conversationIds, userId)
            }
        }
    }

    @Test
    fun verifyMarkUnreadIsCalledWhenUnreadActionIsReceived() {
        runBlockingTest {
            // given
            val conversation1 = "conversation1"
            val conversation2 = "conversation2"
            val conversationIds = listOf(conversation1, conversation2)
            coEvery {
                conversationsRepository.markUnread(conversationIds, any(), any(), any())
            } returns ConversationsActionResult.Success

            // when
            changeConversationsReadStatus(
                conversationIds,
                ChangeConversationsReadStatus.Action.ACTION_MARK_UNREAD,
                UserId("id"),
                Constants.MessageLocationType.ARCHIVE,
                Constants.MessageLocationType.ARCHIVE.messageLocationTypeValue.toString()
            )

            // then
            coVerify {
                conversationsRepository.markUnread(conversationIds, any(), any(), any())
            }
        }
    }

    @Test
    fun verifyUseCaseReturnsSuccessResultWhenRepositoryReturnsSuccessResult() {
        runBlockingTest {
            // given
            val conversation1 = "conversation1"
            val conversation2 = "conversation2"
            val conversationIds = listOf(conversation1, conversation2)
            val userId = UserId("id")
            val expectedResult = ConversationsActionResult.Success
            coEvery {
                conversationsRepository.markRead(conversationIds, userId)
            } returns ConversationsActionResult.Success

            // when
            val result = changeConversationsReadStatus(
                conversationIds,
                ChangeConversationsReadStatus.Action.ACTION_MARK_READ,
                userId,
                Constants.MessageLocationType.ARCHIVE,
                Constants.MessageLocationType.ARCHIVE.messageLocationTypeValue.toString()
            )

            // then
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun verifyUseCaseReturnsErrorResultWhenRepositoryReturnsErrorResult() {
        runBlockingTest {
            // given
            val conversation1 = "conversation1"
            val conversation2 = "conversation2"
            val conversationIds = listOf(conversation1, conversation2)
            val expectedResult = ConversationsActionResult.Error
            coEvery {
                conversationsRepository.markUnread(conversationIds, any(), any(), any())
            } returns ConversationsActionResult.Error

            // when
            val result = changeConversationsReadStatus(
                conversationIds,
                ChangeConversationsReadStatus.Action.ACTION_MARK_UNREAD,
                UserId("id"),
                Constants.MessageLocationType.ARCHIVE,
                Constants.MessageLocationType.ARCHIVE.messageLocationTypeValue.toString()
            )

            // then
            assertEquals(expectedResult, result)
        }
    }
}