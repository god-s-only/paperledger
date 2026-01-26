package com.paperledger.app.presentation.ui.features.ach_relationships

sealed class ACHRelationshipEvent {
    data class OnNickNameChange(val nickname: String): ACHRelationshipEvent()
    data class OnOwnerNameChange(val ownerName: String): ACHRelationshipEvent()
    data class OnAccountTypeChange(val accountType: String): ACHRelationshipEvent()
    data class OnAccountNumberChange(val accountNumber: String): ACHRelationshipEvent()
    data class OnRoutingNumberChange(val routingNumber: String): ACHRelationshipEvent()
    object OnSubmit: ACHRelationshipEvent()

}