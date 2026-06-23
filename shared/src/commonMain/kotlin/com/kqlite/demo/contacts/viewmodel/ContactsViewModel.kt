package com.kqlite.demo.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kqlite.demo.contacts.delight.DelightDatabaseHelper
import com.kqlite.demo.contacts.model.Contact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface ContactUiState {
    data object Loading : ContactUiState
    data class Success(val contacts: List<Contact>) : ContactUiState
}

class ContactsViewModel(private val helper: DelightDatabaseHelper) : ViewModel() {

    val uiState: StateFlow<ContactUiState> =
        helper.selectContactsAsFlow()
            .map { ContactUiState.Success(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactUiState.Loading)

    /*val uiState: StateFlow<ContactUiState> =
        selectActiveContacts()
            .asCallbackFlow()
            .mapToList {
                TblContact.mapper(it)
            }
            .map { ContactUiState.Success(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactUiState.Loading)

    fun selectActiveContacts(): KQLiteCursor {
        return TblContact
            .select()
            .where { it.deleted NOT_EQ true }
            .execute()
    }

    fun insertContact(contact: Contact): Int {
        return TblContact
            .insert()
            .bind { it.binder(this, contact) }
            .executeReturning(TblContact.id)
    }

    fun updateContact(contact: Contact) {
        TblContact
            .update(onConflict = Action.REPLACE) { it.binder(this, contact) }
            .where { it.id EQ contact.id }
            .execute()
    }

    fun deleteContact(id: Int) {
        TblContact
            .update { it.deleted.bind(true) }
            .where { it.id EQ id }
            .execute()
    }*/

    fun insertContact(contact: Contact): Long {
        return helper.insertContact(contact)
    }
}
