package jp.zliandroid.mymusicplayer.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {
    @Provides
    fun provideContext() = context
}