package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO

sealed class PlaceTradeEvent {
    data class OnPlaceTradeButtonClick(val positionRequest: PositionRequestDTO): PlaceTradeEvent()
}