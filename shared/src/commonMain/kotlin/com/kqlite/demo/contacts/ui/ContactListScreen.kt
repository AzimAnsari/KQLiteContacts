package com.kqlite.demo.contacts.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.utils.testContacts

@Composable
fun ContactListScreen(
    contacts: List<Contact>,
    onClick: (Contact) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(contacts, key = { it.id }) { contact ->
            ContactItem(contact = contact, onClick = { onClick(contact) })
        }
    }
}

@Preview
@Composable
fun PreviewContactList() {
    val contacts = testContacts()

    MaterialTheme {
        ContactListScreen(
            contacts = contacts,
            onClick = {}
        )
    }
}