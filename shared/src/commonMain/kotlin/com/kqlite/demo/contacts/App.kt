package com.kqlite.demo.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kqlite.demo.contacts.ui.ContactListScreen
import com.kqlite.demo.contacts.utils.fullName
import com.kqlite.demo.contacts.utils.testContacts
import kqlitecontacts.shared.generated.resources.Res
import kqlitecontacts.shared.generated.resources.baseline_add_24
import org.jetbrains.compose.resources.painterResource

@Preview
@Composable
fun App() {
    var presses by remember { mutableIntStateOf(0) }

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
            FloatingActionButton(onClick = { presses++ }) {
                Image(painterResource(Res.drawable.baseline_add_24), "Add Contact")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ContactListScreen(testContacts()) {
                println("Clicked on ${it.fullName()}")
            }
        }
    }
}