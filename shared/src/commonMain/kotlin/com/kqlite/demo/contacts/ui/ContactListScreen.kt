package com.kqlite.demo.contacts.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.utils.testContacts
import com.kqlite.demo.contacts.viewmodel.ContactUiState
import com.kqlite.demo.contacts.viewmodel.ContactsViewModel

@Composable
fun ContactListScreen(
    contactsViewModel: ContactsViewModel,
    onClick: (Contact) -> Unit
) {
    val uiState by contactsViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ContactUiState.Success -> {
            ContactList(
                contacts = state.contacts,
                onClick = onClick,
                modifier = Modifier.fillMaxSize()
            )
        }

        ContactUiState.Loading -> {
            // Loading state can be handled here
        }
    }
}

@Composable
fun ContactList(
    contacts: List<Contact>,
    onClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No contacts",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(contacts, key = { it.id }) { contact ->
                ContactItem(contact = contact, onClick = { onClick(contact) })
            }
        }
    }
}

@Preview
@Composable
fun PreviewContactList() {
    val contacts = testContacts()

    MaterialTheme {
        ContactList(
            contacts = contacts,
            onClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun PreviewEmptyContactList() {
    MaterialTheme {
        ContactList(
            contacts = emptyList(),
            onClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
