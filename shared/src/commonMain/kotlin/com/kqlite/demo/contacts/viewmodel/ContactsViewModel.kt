package com.kqlite.demo.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.cursor.mapToList
import com.kqlite.demo.contacts.db.ContactsDatabase
import com.kqlite.demo.contacts.db.TblContact
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.flow.asCallbackFlow
import com.kqlite.statement.insert
import com.kqlite.statement.select
import com.kqlite.statement.update
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface ContactUiState {
    data object Loading : ContactUiState
    data class Success(val contacts: List<Contact>) : ContactUiState
}

class ContactsViewModel(
    private val database: ContactsDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    init {
        addCloseable(database.open())
    }

    val uiState: StateFlow<ContactUiState> =
        selectActiveContacts()
            .asCallbackFlow()
            .mapToList(ioDispatcher) { TblContact.mapper(it) }
            .map { ContactUiState.Success(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactUiState.Loading)

    private fun selectActiveContacts(): KQLiteCursor =
        TblContact
            .select()
            .where { it.deleted NOT_EQ true }
            .execute()


    fun insertContact(
        contact: Contact,
        onResult: (Int) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            val id = database.withTransaction {
                val insertStmt = TblContact.insert()
                insertStmt.bind { TblContact.binder(this, contact) }
                val id = insertStmt.executeReturning(TblContact.id)
                insertStmt.close()
                id
            }
            withContext(Dispatchers.Main) {
                onResult(id)
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch(ioDispatcher) {
            TblContact
                .update { it.binder(this, contact) }
                .where { it.id EQ contact.id }
                .execute()
        }
    }

    fun deleteContact(id: Int) {
        viewModelScope.launch(ioDispatcher) {
            TblContact
                .update { it.deleted.bind(true) }
                .where { it.id EQ id }
                .execute()
        }
    }
}
