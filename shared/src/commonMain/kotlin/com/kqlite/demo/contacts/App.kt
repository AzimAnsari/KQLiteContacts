package com.kqlite.demo.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kqlite.demo.contacts.db.ContactsDatabase
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.ui.AddContactScreen
import com.kqlite.demo.contacts.ui.ContactListScreen
import com.kqlite.demo.contacts.utils.fullName
import com.kqlite.demo.contacts.viewmodel.ContactsViewModel

@Preview
@Composable
fun App() {
    val navController = rememberNavController()
    val database = remember { ContactsDatabase() }
    val contactsViewModel: ContactsViewModel = viewModel { ContactsViewModel(database) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    NavHost(navController = navController, startDestination = "contact_list") {
        composable("contact_list") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("KQLite Contacts")
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate("add_contact") }) {
                        Image(Icons.Filled.Add, "Add Contact")
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ContactListScreen(contactsViewModel) {
                        selectedContact = it
                        navController.navigate("update_contact")
                    }
                }
            }
        }
        composable("add_contact") {
            OpenContactScreen(contactsViewModel, navController, contact = null)
        }
        composable("update_contact") {
            OpenContactScreen(contactsViewModel, navController, selectedContact)
        }
    }
}

@Composable
fun OpenContactScreen(
    contactsViewModel: ContactsViewModel,
    navController: NavController,
    contact: Contact?
) {
    AddContactScreen(
        contact = contact,
        onSave = {
            if (it.id == -1) {
                contactsViewModel.insertContact(it) { id ->
                    if (id > 0) {
                        println("Contact '${it.fullName()}' created.")
                    } else {
                        println("Error creating contact.")
                    }
                    navController.popBackStack()
                }
            } else {
                contactsViewModel.updateContact(it) { changes ->
                    if (changes > 0) {
                        println("Contact '${it.fullName()}' updated.")
                    } else {
                        println("Error updating contact.")
                    }
                    navController.popBackStack()
                }
            }
        },
        onDelete = {
            contactsViewModel.deleteContact(it.id) { id ->
                println("Contact '${it.fullName()}' deleted.")
                navController.popBackStack()
            }
        },
        onCancel = {
            navController.popBackStack()
        }
    )
}