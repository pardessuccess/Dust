package com.schoolkeepa.dust.di

import com.schoolkeepa.dust.data.repository.DustRepositoryImpl
import com.schoolkeepa.dust.data.repository.NeisRepositoryImpl
import com.schoolkeepa.dust.domain.repository.DustRepository
import com.schoolkeepa.dust.domain.repository.NeisRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun provideDustRepository(dustRepositoryImpl: DustRepositoryImpl): DustRepository

    @Binds
    @Singleton
    abstract fun provideNeisRepository(neisRepositoryImpl: NeisRepositoryImpl): NeisRepository


}