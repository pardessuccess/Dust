package com.schoolkeepa.dust.di

import com.schoolkeepa.dust.data.manager.LocalUserManagerImpl
import com.schoolkeepa.dust.domain.manager.LocalUserManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindLocalUserManager(localUserManagerImpl: LocalUserManagerImpl): LocalUserManager

}