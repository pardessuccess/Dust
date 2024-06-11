package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.finestatus.FineStatusDto
import com.schoolkeepa.dust.data.model.weather.WeatherDto
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class GetFineStatus @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(locationCode: String): Resource<FineStatusDto> {
        return dustRepository.getFineStatus(locationCode = locationCode)
    }
}