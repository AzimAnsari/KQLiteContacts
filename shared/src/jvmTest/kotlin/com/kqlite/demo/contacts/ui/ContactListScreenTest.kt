package com.kqlite.demo.contacts.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import com.kqlite.demo.contacts.viewmodel.ContactUiState
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ContactListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingText() {
        composeTestRule.setContent {
            ContactListContent(
                uiState = ContactUiState.Loading,
                onClick = {},
            )
        }

        composeTestRule.onNodeWithText("Loading contacts...").assertIsDisplayed()
    }

    @Test
    fun successState_emptyList_showsNoContactsText() {
        composeTestRule.setContent {
            ContactListContent(
                uiState = ContactUiState.Success(emptyList()),
                onClick = {},
            )
        }

        composeTestRule.onNodeWithText("No contacts").assertIsDisplayed()
    }

    @Test
    fun successState_withContacts_showsContacts() {
        val contacts = listOf(
            Contact(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                phone = listOf("1234567890"),
                birthDate = null,
                email = "john@example.com",
                image = null,
                type = ContactType.Friend
            ),
            Contact(
                id = 2,
                firstName = "Jane",
                lastName = "Doe",
                phone = listOf("0987654321"),
                birthDate = null,
                email = "jane@example.com",
                image = null,
                type = ContactType.Family
            )
        )

        composeTestRule.setContent {
            ContactListContent(
                uiState = ContactUiState.Success(contacts),
                onClick = {},
            )
        }

        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jane Doe").assertIsDisplayed()
    }

    @Test
    fun clickingContact_triggersOnClick() {
        val contact = Contact(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            phone = listOf("1234567890"),
            birthDate = null,
            email = "john@example.com",
            image = null,
            type = ContactType.Friend
        )
        val onClick: (Contact) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            ContactListContent(
                uiState = ContactUiState.Success(listOf(contact)),
                onClick = onClick,
            )
        }

        composeTestRule.onNodeWithText("John Doe").performClick()

        verify { onClick(contact) }
    }
}
