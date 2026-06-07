package com.paperledger.app.domain.usecase.auth

import com.paperledger.app.data.local.PaperledgerDatabase
import com.paperledger.app.data.local.PaperLedgerSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val session: PaperLedgerSession,
    private val database: PaperledgerDatabase
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            session.clearAll()
            database.clearAllTables()
        }
    }
}