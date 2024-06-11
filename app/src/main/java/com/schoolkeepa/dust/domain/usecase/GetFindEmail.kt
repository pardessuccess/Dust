package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.email.FindEmailDto
import com.schoolkeepa.dust.data.model.finestatus.FineStatusDto
import com.schoolkeepa.dust.data.model.weather.WeatherDto
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class GetFindEmail @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(name: String, schoolName: String): Resource<FindEmailDto> {
        return dustRepository.getFindEmail(name, schoolName)
    }
}