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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface ContactUiState {
    data object Loading : ContactUiState
    data class Success(val contacts: List<Contact>) : ContactUiState
}

class ContactsViewModel(private val database: ContactsDatabase) : ViewModel() {

    init {
        database.open()
    }

    val uiState: StateFlow<ContactUiState> =
        selectActiveContacts()
            .asCallbackFlow()
            .mapToList {
                println("mapToList: $it")
                TblContact.mapper(it)
            }
            .map { ContactUiState.Success(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactUiState.Loading)

    fun selectActiveContacts(): KQLiteCursor {
        val c = TblContact
            .select()
            .where { it.deleted NOT_EQ true }
            .execute()
        println("selectActiveContacts: $c")
        return c
    }

    fun insertContact(contact: Contact): Int {
        return TblContact
            .insert().use {
                it.bind { TblContact.binder(this, contact) }
                    .executeReturning(TblContact.id)
            }
    }

    fun updateContact(contact: Contact) {
        TblContact
            .update { it.binder(this, contact) }
            .where { it.id EQ contact.id }
            .execute()
    }

    fun deleteContact(id: Int) {
        TblContact
            .update { it.deleted.bind(true) }
            .where { it.id EQ id }
            .execute()
    }

    override fun onCleared() {
        database.close()
    }
}
