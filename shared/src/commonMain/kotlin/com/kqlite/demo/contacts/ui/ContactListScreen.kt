package com.kqlite.demo.contacts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face2
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.viewmodel.ContactUiState
import com.kqlite.demo.contacts.viewmodel.ContactsViewModel
import kotlin.time.Clock

@Composable
fun ContactListScreen(
    contactsViewModel: ContactsViewModel,
    onClick: (Contact) -> Unit
) {
    val uiState by contactsViewModel.uiState.collectAsStateWithLifecycle()
    ContactListContent(
        uiState = uiState,
        onClick = onClick,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ContactListContent(
    uiState: ContactUiState,
    onClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ContactUiState.Success -> {
            ContactList(
                contacts = uiState.contacts,
                onClick = onClick,
                modifier = modifier
            )
        }

        ContactUiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading contacts...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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
        val faceIcons = remember { StaticIcons().staticFaces() }
        LazyColumn(
            modifier = modifier.background(Color.White),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
        ) {
            items(contacts, key = { it.id }) { contact ->
                ContactItem(
                    faceIcons = faceIcons,
                    contact = contact,
                    onClick = { onClick(contact) })
            }
        }
    }
}

@Preview
@Composable
fun PreviewContactList() {
    MaterialTheme {
        ContactList(
            contacts = listOf(
                Contact(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    phone = listOf("+911234567890", "+919876543210"),
                    birthDate = null,
                    email = "john.doe@example.com",
                    image = null,
                    type = com.kqlite.demo.contacts.model.ContactType.Friend
                ),
                Contact(
                    id = 2,
                    firstName = "Jane",
                    lastName = "Moe",
                    phone = listOf("+911234567890", "+919876543210"),
                    birthDate = Clock.System.now(),
                    email = "jane.moe@example.com",
                    image = Icons.Filled.Face2.name.encodeToByteArray(),
                    type = com.kqlite.demo.contacts.model.ContactType.Family
                )
            ),
            onClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}