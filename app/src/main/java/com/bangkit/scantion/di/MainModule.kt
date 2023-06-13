package com.bangkit.scantion.di

import android.content.Context
import com.bangkit.scantion.data.database.SkinExamsDao
import com.bangkit.scantion.data.database.SkinExamsDatabase
import com.bangkit.scantion.data.preference.login.LoginDataStoreRepository
import com.bangkit.scantion.data.preference.theme.ThemeManager
import com.bangkit.scantion.data.repository.AuthRepository
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
    fun provideSkinExamsDatabase(
        @ApplicationContext context: Context
    ) = SkinExamsDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideSkinExamsDao(
        database: SkinExamsDatabase
    ) = database.skinExamsDao()

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

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context
    ) = AuthRepository(context)
}