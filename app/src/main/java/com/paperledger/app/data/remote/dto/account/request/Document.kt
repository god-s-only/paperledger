package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("content")
    val content: String,
    @SerializedName("document_sub_type")
    val documentSubType: String,
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("mime_type")
    val mimeType: String
)