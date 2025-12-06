package com.example.navyuga.arthYuga.mainModule.di

import com.example.navyuga.arthYuga.mainModule.repositories.ProfileRepository
import com.example.navyuga.arthYuga.mainModule.repositories.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}