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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kqlite.demo.contacts.db.ContactsDatabase
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
                        println("Clicked on ${it.fullName()}")
                    }
                }
            }
        }
        composable("add_contact") {
            AddContactScreen(
                onSave = {
                    val id = contactsViewModel.insertContact(it)
                    println("Inserted Contact ID: $id")
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}