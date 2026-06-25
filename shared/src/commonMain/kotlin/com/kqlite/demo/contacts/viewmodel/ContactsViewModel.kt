package com.kqlite.demo.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kqlite.cursor.KQLiteCursor
import com.kqlite.cursor.asCallbackFlow
import com.kqlite.cursor.mapToList
import com.kqlite.demo.contacts.db.ContactsDatabase
import com.kqlite.demo.contacts.db.TblContact
import com.kqlite.demo.contacts.model.Contact
import com.kqlite.statement.insert
import com.kqlite.statement.select
import com.kqlite.statement.update
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ContactUiState> =
        _searchQuery.flatMapLatest { query ->
            selectActiveContacts(query)
                .asCallbackFlow()
                .mapToList(ioDispatcher) { TblContact.mapper(it) }
                .map { ContactUiState.Success(it) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactUiState.Loading)

    private fun selectActiveContacts(query: String): KQLiteCursor =
        TblContact
            .select()
            .where {
                if (query.isNotBlank()) {
                    val likeQuery = "%$query%"
                    (it.deleted NOT_EQ true) AND (
                            (it.firstName LIKE likeQuery) OR
                                    (it.lastName LIKE likeQuery) OR
                                    (it.email LIKE likeQuery) OR
                                    (it.phone LIKE likeQuery)
                            )
                } else {
                    it.deleted NOT_EQ true
                }
            }
            .execute()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }


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

    fun updateContact(
        contact: Contact,
        onResult: (Int) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            TblContact
                .update { it.binder(this, contact) }
                .where { it.id EQ contact.id }
                .execute()

            val changes = database.sqliteChanges()
            withContext(Dispatchers.Main) {
                onResult(changes)
            }
        }
    }

    fun deleteContact(id: Int, onResult: (Int) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            TblContact
                .update { it.deleted.bind(true) }
                .where { it.id EQ id }
                .execute()

            val changes = database.sqliteChanges()
            withContext(Dispatchers.Main) {
                onResult(changes)
            }
        }
    }
}
