package jp.zliandroid.mymusicplayer.di.module

import dagger.Module
import dagger.Provides
import jp.zliandroid.mymusicplayer.di.scope.ActivityScoped
import jp.zliandroid.mymusicplayer.musicplay.MusicPlayActivity

@Module
class MusicInfoProvideModule {

    @ActivityScoped
    @Provides
    fun provideAlbumId(activity: MusicPlayActivity): Long = activity.albumId

    @ActivityScoped
    @Provides
    fun provideTrackIds(activity: MusicPlayActivity): List<Long> = activity.trackIds

    @ActivityScoped
    @Provides
    fun provideTrackPosition(activity: MusicPlayActivity): Int = activity.trackPosition
}