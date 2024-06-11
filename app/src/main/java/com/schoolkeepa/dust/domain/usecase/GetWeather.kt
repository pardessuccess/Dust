package com.schoolkeepa.dust.domain.usecase

import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.weather.WeatherDto
import com.schoolkeepa.dust.domain.repository.DustRepository
import javax.inject.Inject

class GetWeather @Inject constructor(
    private val dustRepository: DustRepository
) {
    suspend operator fun invoke(lat: String, lng: String, date: String): Resource<WeatherDto> {
        return dustRepository.getWeather(lat, lng, date)
    }
}