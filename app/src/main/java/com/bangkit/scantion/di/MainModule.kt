package com.bangkit.scantion.di

import android.content.Context
import com.bangkit.scantion.data.database.SkinExamsDatabase
import com.bangkit.scantion.data.preference.theme.ThemeManager
import com.bangkit.scantion.data.firebase.AuthRepository
import com.bangkit.scantion.data.firebase.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
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
    fun provideThemeModeDatastore(
        @ApplicationContext context: Context
    ) = ThemeManager(context = context)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}