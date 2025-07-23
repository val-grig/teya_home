package com.grigoryev.teya_home.core.app.domain

import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.data.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConnectionUseCase @Inject constructor(
    private val connectionRepository: ConnectionRepository
) {
    fun invoke(): Flow<ConnectionEvent> = connectionRepository.getConnectionEvents()
}