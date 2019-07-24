package jp.zliandroid.mymusicplayer.di.module

import dagger.Module
import dagger.Provides
import jp.zliandroid.mymusicplayer.di.scope.ActivityScoped
import jp.zliandroid.mymusicplayer.tracklist.TrackListActivity

@Module
class AlbumIdProvideModule {

    @ActivityScoped
    @Provides
    fun provideAlbumId(activity: TrackListActivity): Long = activity.albumId
}