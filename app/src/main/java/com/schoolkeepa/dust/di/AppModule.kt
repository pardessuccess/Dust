package com.schoolkeepa.dust.di

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.schoolkeepa.dust.BuildConfig
import com.schoolkeepa.dust.data.network.DustApi
import com.schoolkeepa.dust.data.network.NeisApi
import com.schoolkeepa.dust.data.repository.AuthRepositoryImpl
import com.schoolkeepa.dust.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DustRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NeisRetrofit


    @Provides
    @Singleton
    @DustRetrofit
    fun provideDustApi(): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            ).build()


    @Provides
    @Singleton
    @DustRetrofit
    fun provideDustApiInstance(
        @DustRetrofit retrofit: Retrofit
    ): DustApi {
        return retrofit.create(DustApi::class.java)
    }

    @Provides
    @Singleton
    @NeisRetrofit
    fun provideNeisApi(): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.NEIS_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()


    @Provides
    @Singleton
    @NeisRetrofit
    fun provideNeisApiInstance(
        @NeisRetrofit retrofit: Retrofit
    ): NeisApi {
        return retrofit.create(NeisApi::class.java)
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = firebaseAuth)
    }

}