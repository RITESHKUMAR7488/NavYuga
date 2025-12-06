package com.example.navyuga.arthYuga.adminModule.di

import android.content.Context
import com.example.navyuga.arthYuga.adminModule.repositories.AdminRepository
import com.example.navyuga.arthYuga.adminModule.repositories.AdminRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdminModule {

    @Provides
    @Singleton
    fun provideAdminRepository(
        firestore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): AdminRepository {
        return AdminRepositoryImpl(firestore, context)
    }
}