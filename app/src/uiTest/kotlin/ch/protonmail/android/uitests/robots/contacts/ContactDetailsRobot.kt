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
package ch.protonmail.android.uitests.robots.contacts

import androidx.appcompat.widget.AppCompatImageButton
import ch.protonmail.android.R
import me.proton.core.test.android.instrumented.Robot

/**
 * [ContactDetailsRobot] class contains actions and verifications for Contacts functionality.
 */
open class ContactDetailsRobot : Robot {

    fun deleteContact(): ContactsRobot {
        delete()
            .confirmDeletion()
        return ContactsRobot()
    }

    fun editContact(): AddContactRobot {
        view.withId(R.id.action_contact_details_edit).click()
        return AddContactRobot()
    }

    fun navigateUp(): ContactsRobot {
        view
            .instanceOf(AppCompatImageButton::class.java)
            .withParent(view.withId(R.id.toolbar))
            .click()
        return ContactsRobot()
    }

    private fun delete(): ContactDetailsRobot {
        view.withId(R.id.action_contact_details_delete).click()
        return this
    }

    private fun confirmDeletion() {
        view.withId(android.R.id.button1).isEnabled().isCompletelyDisplayed().click()
    }

    /**
     * Contains all the validations that can be performed by [ContactDetailsRobot].
     */
    class Verify {}

    inline fun verify(block: Verify.() -> Unit) = Verify().apply(block)
}
