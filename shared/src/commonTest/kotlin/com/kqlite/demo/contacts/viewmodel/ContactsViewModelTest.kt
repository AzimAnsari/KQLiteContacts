package com.kqlite.demo.contacts.viewmodel

import com.kqlite.demo.contacts.db.ContactsDatabase
import com.kqlite.demo.contacts.db.ContactsDatabaseDriver
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {

    private lateinit var database: ContactsDatabase
    private lateinit var viewModel: ContactsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        database = ContactsDatabase(ContactsDatabaseDriver(":memory:"))
        viewModel = ContactsViewModel(database, testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should eventually be Success with empty list`() = runTest {
        val uiState = viewModel.uiState.first { it is ContactUiState.Success }
        assertTrue(uiState is ContactUiState.Success)
        assertTrue(uiState.contacts.isEmpty())
    }

    @Test
    fun `insertContact should add contact and update uiState`() = runTest {
        val contact = Contact(
            id = -1,
            firstName = "John",
            lastName = "Doe",
            phone = listOf("1234567890"),
            birthDate = null,
            email = "john@example.com",
            image = null,
            type = ContactType.Friend
        )

        var insertedId = -1
        viewModel.insertContact(contact) {
            insertedId = it
        }

        advanceUntilIdle()

        assertTrue(insertedId > 0)

        val uiState =
            viewModel.uiState.first { it is ContactUiState.Success && it.contacts.isNotEmpty() }
        val successState = uiState as ContactUiState.Success
        assertEquals(1, successState.contacts.size)
        assertEquals("John", successState.contacts[0].firstName)
        assertEquals(insertedId, successState.contacts[0].id)
    }

    @Test
    fun `updateContact should modify existing contact`() = runTest {
        val contact = Contact(
            id = -1,
            firstName = "John",
            lastName = "Doe",
            phone = listOf("1234567890"),
            birthDate = null,
            email = "john@example.com",
            image = null,
            type = ContactType.Friend
        )

        var insertedId = -1
        viewModel.insertContact(contact) {
            insertedId = it
        }
        advanceUntilIdle()

        val updatedContact = contact.copy(id = insertedId, firstName = "Jane")
        var changes = 0
        viewModel.updateContact(updatedContact) {
            changes = it
        }
        advanceUntilIdle()

        assertEquals(1, changes)
        val uiState =
            viewModel.uiState.first { it is ContactUiState.Success && it.contacts.any { c -> c.firstName == "Jane" } }
        val successState = uiState as ContactUiState.Success
        assertEquals("Jane", successState.contacts.find { it.id == insertedId }?.firstName)
    }

    @Test
    fun `deleteContact should mark contact as deleted and remove from uiState`() = runTest {
        val contact = Contact(
            id = -1,
            firstName = "John",
            lastName = "Doe",
            phone = listOf("1234567890"),
            birthDate = null,
            email = "john@example.com",
            image = null,
            type = ContactType.Friend
        )

        var insertedId = -1
        viewModel.insertContact(contact) {
            insertedId = it
        }
        advanceUntilIdle()

        var changes = 0
        viewModel.deleteContact(insertedId) {
            changes = it
        }
        advanceUntilIdle()

        assertEquals(1, changes)
        val uiState =
            viewModel.uiState.first { it is ContactUiState.Success && it.contacts.isEmpty() }
        val successState = uiState as ContactUiState.Success
        assertTrue(successState.contacts.none { it.id == insertedId })
    }
}
