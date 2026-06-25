package com.kqlite.demo.contacts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.utils.fullName
import com.kqlite.demo.contacts.viewmodel.ContactUiState
import com.kqlite.demo.contacts.viewmodel.ContactsViewModel
import kotlin.time.Clock

@Composable
fun TrashScreen(
    contactsViewModel: ContactsViewModel,
    onCancel: () -> Unit
) {
    val uiState by contactsViewModel.trashUiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Deleted Contacts")
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) {
        TrashListContent(
            uiState = uiState,
            onRestore = { contact ->
                contactsViewModel.restoreContact(contact.id) { id ->
                    println("Contact id - $id, '${contact.fullName()}' restored.")
                }
            },
            modifier = Modifier.padding(it).fillMaxSize()
        )
    }

}

@Composable
fun TrashListContent(
    uiState: ContactUiState,
    onRestore: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ContactUiState.Success -> {
            TrashList(
                contacts = uiState.contacts,
                onRestore = onRestore,
                modifier = modifier
            )
        }

        ContactUiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading deleted contacts...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun TrashList(
    contacts: List<Contact>,
    onRestore: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Trash is empty",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        val faceIcons = remember { StaticIcons().staticFaces() }
        var contactToRestore by remember { mutableStateOf<Contact?>(null) }

        contactToRestore?.let { contact ->
            AlertDialog(
                onDismissRequest = { contactToRestore = null },
                title = { Text(text = "Restore Contact") },
                text = { Text(text = "Are you sure you want to restore '${contact.fullName()}' ?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onRestore(contact)
                            contactToRestore = null
                        }
                    ) {
                        Text("Restore")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { contactToRestore = null }) {
                        Text("Cancel")
                    }
                }
            )
        }

        LazyColumn(
            modifier = modifier.background(Color.White),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
        ) {
            items(contacts, key = { it.id }) { contact ->
                ContactItem(
                    faceIcons = faceIcons,
                    contact = contact,
                    onClick = { contactToRestore = contact }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTrashList() {
    MaterialTheme {
        TrashList(
            contacts = listOf(
                Contact(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    phone = listOf("+911234567890", "+919876543210"),
                    birthDate = null,
                    email = "john.doe@example.com",
                    image = null,
                    type = com.kqlite.demo.contacts.model.ContactType.Friend,
                    deleted = true
                ),
                Contact(
                    id = 2,
                    firstName = "Jane",
                    lastName = "Moe",
                    phone = listOf("+911234567890", "+919876543210"),
                    birthDate = Clock.System.now(),
                    email = "jane.moe@example.com",
                    image = Icons.Filled.Face2.name.encodeToByteArray(),
                    type = com.kqlite.demo.contacts.model.ContactType.Family,
                    deleted = true
                )
            ),
            onRestore = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
