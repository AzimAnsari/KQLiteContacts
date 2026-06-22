package com.kqlite.demo.contacts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.demo.contacts.model.ContactType
import com.kqlite.demo.contacts.utils.toInstant
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun AddContactScreen(
    onSave: (Contact) -> Unit,
    onCancel: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phones by remember { mutableStateOf(listOf("")) }
    var selectedType by remember { mutableStateOf(ContactType.Other) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Validation Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Contact") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val errors = mutableListOf<String>()
                            if (firstName.isBlank()) {
                                errors.add("First name cannot be empty.")
                            }

                            val filledPhones = phones.filter { it.isNotBlank() }
                            if (filledPhones.isEmpty()) {
                                errors.add("At least one phone number is required.")
                            } else if (filledPhones.any { it.length < 10 }) {
                                errors.add("Each phone number must be at least 10 characters long.")
                            }

                            if (email.isNotBlank()) {
                                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
                                if (!emailRegex.matches(email)) {
                                    errors.add("Please enter a valid email address.")
                                }
                            }

                            var parsedBirthDate: Instant? = null
                            if (birthDate.isNotBlank()) {
                                val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
                                if (!dateRegex.matches(birthDate)) {
                                    errors.add("Birth date must be in YYYY-MM-DD format.")
                                } else {
                                    try {
                                        parsedBirthDate = birthDate.toInstant()
                                    } catch (_: Exception) {
                                        errors.add("Invalid birth date.")
                                    }
                                }
                            }

                            if (errors.isNotEmpty()) {
                                errorMessage = errors.joinToString("\n")
                                showErrorDialog = true
                            } else {
                                onSave(
                                    Contact(
                                        id = -1,
                                        firstName = firstName,
                                        lastName = lastName,
                                        phone = filledPhones,
                                        email = email.takeIf { it.isNotBlank() },
                                        birthDate = parsedBirthDate,
                                        createdTime = Clock.System.now(),
                                        image = null,
                                        type = selectedType
                                    )
                                )
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (firstName.isBlank()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Text(
                        text = firstName.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            phones.forEachIndexed { index, phone ->
                OutlinedTextField(
                    value = phone,
                    onValueChange = { newValue ->
                        val newList = phones.toMutableList()
                        newList[index] = newValue
                        phones = newList
                    },
                    label = { Text("Phone ${index + 1}") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    trailingIcon = {
                        Row {
                            if (phones.size > 1) {
                                IconButton(onClick = {
                                    val newList = phones.toMutableList()
                                    newList.removeAt(index)
                                    phones = newList
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Phone")
                                }
                            }
                            if (index == phones.size - 1) {
                                IconButton(onClick = { phones = phones + "" }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Phone")
                                }
                            }
                        }
                    },
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true
            )

            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Birth Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                singleLine = true
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Contact Type",
                    style = MaterialTheme.typography.titleMedium
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ContactType.entries.forEach { type ->
                        val icon = when (type) {
                            ContactType.Family -> Icons.Default.Group
                            ContactType.Friend -> Icons.Default.Favorite
                            ContactType.Work -> Icons.Default.Work
                            ContactType.Other -> Icons.Default.Info
                        }
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.name) },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
