package jp.zliandroid.mymusicplayer.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.zliandroid.mymusicplayer.albumlist.AlbumListActivity
import jp.zliandroid.mymusicplayer.di.scope.ActivityScoped
import jp.zliandroid.mymusicplayer.musicplay.MusicPlayActivity
import jp.zliandroid.mymusicplayer.tracklist.TrackListActivity

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AlbumListModule::class])
    abstract fun albumListActivity(): AlbumListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [TrackListModule::class, AlbumIdProvideModule::class])
    abstract fun trackListActivity(): TrackListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MusicPlayModule::class, MusicInfoProvideModule::class])
    abstract fun musicPlayActivity(): MusicPlayActivity
}