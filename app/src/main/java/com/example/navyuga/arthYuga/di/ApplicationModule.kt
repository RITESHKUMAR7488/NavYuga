package com.example.navyuga.arthYuga.di

import android.content.Context
import android.content.SharedPreferences
import com.example.navyuga.arthYuga.common.utilities.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("EstakePrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferenceManager(sharedPreferences: SharedPreferences): PreferenceManager {
        return PreferenceManager(sharedPreferences)
    }
}