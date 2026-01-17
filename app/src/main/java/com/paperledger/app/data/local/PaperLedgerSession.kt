package com.paperledger.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.paperLedger by preferencesDataStore(name = "paper_ledger")

@Singleton
class PaperLedgerSession @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val SESSION_TOKEN = stringPreferencesKey("session_token")
    }

    suspend fun storeUserId(id: String) {
        context.paperLedger.edit {
            it[PreferencesKeys.SESSION_TOKEN] = id
        }
    }
}
