package com.paperledger.app.presentation.ui.features.ach_relationships

data class ACHRelationshipState(
    val isLoading : Boolean = false,
    val nickname: String = "",
    val ownerName: String = "",
    val accountType: String = "",
    val accountNumber: String = "",
    val routingNumber: String = ""
)
