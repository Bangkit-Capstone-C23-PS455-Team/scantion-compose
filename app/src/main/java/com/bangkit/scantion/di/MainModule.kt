package com.bangkit.scantion.di

import android.content.Context
import com.bangkit.scantion.data.preference.login.LoginDataStoreRepository
import com.bangkit.scantion.data.preference.theme.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = LoginDataStoreRepository(context = context)

    @Provides
    @Singleton
    fun provideThemeModeDatastore(
        @ApplicationContext context: Context
    ) = ThemeManager(context = context)
}