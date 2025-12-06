package com.example.navyuga.arthYuga.authModule.di

import com.example.navyuga.arthYuga.authModule.repositories.AuthRepository
import com.example.navyuga.arthYuga.authModule.repositories.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}