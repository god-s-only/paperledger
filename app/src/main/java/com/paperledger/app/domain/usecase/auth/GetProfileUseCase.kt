package com.paperledger.app.domain.usecase.auth

import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.domain.models.profile.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val api: AlpacaApiService,
    private val getUserIdUseCase: GetUserIdUseCase
) {
    suspend operator fun invoke(): Result<ProfileModel> {
        return withContext(Dispatchers.IO) {
            try {
                val accountId = getUserIdUseCase() ?: return@withContext Result.failure(
                    AppError.HttpError(401, "No account ID found")
                )
                val response = api.getAccount(accountId)
                if (response.isSuccessful) {
                    val body = response.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    val profile = ProfileModel(
                        id = body.id,
                        accountNumber = body.accountNumber,
                        status = body.status,
                        currency = body.currency,
                        accountType = body.accountType,
                        createdAt = body.createdAt.substringBefore("T"),
                        lastEquity = body.lastEquity,
                        givenName = body.identity.givenName,
                        familyName = body.identity.familyName,
                        dateOfBirth = body.identity.dateOfBirth,
                        countryOfBirth = body.identity.countryOfBirth,
                        countryOfCitizenship = body.identity.countryOfCitizenship,
                        taxIdType = body.identity.taxIdType,
                        fundingSource = body.identity.fundingSource,
                        emailAddress = body.contact.emailAddress,
                        phoneNumber = body.contact.phoneNumber,
                        streetAddress = body.contact.streetAddress,
                        city = body.contact.city,
                        state = body.contact.state,
                        postalCode = body.contact.postalCode,
                        trustedContactGivenName = body.trustedContact.givenName,
                        trustedContactFamilyName = body.trustedContact.familyName,
                        trustedContactEmail = body.trustedContact.emailAddress
                    )
                    Result.success(profile)
                } else {
                    Result.failure(AppError.HttpError(response.code(), response.message()))
                }
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }
}