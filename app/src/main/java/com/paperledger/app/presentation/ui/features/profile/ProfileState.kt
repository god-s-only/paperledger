package com.paperledger.app.presentation.ui.features.profile

import com.paperledger.app.domain.models.profile.ProfileModel

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: ProfileModel? = null,
    val error: String? = null
)