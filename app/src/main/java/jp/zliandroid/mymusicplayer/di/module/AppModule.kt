package jp.zliandroid.mymusicplayer.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumDataSource
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository
import jp.zliandroid.mymusicplayer.data.tracksource.TrackDataSource
import jp.zliandroid.mymusicplayer.data.tracksource.TrackRepository
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAlbumRepository(context: Context): AlbumDataSource = AlbumRepository(context)

    @Singleton
    @Provides
    fun provideTrackRepository(context: Context): TrackDataSource = TrackRepository(context)
}