package jp.zliandroid.mymusicplayer.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import jp.zliandroid.mymusicplayer.di.scope.ActivityScoped
import jp.zliandroid.mymusicplayer.di.scope.FragmentScoped
import jp.zliandroid.mymusicplayer.tracklist.TrackListActivity
import jp.zliandroid.mymusicplayer.tracklist.TrackListContract
import jp.zliandroid.mymusicplayer.tracklist.TrackListFragment
import jp.zliandroid.mymusicplayer.tracklist.TrackListPresenter
import javax.inject.Named

@Module
abstract class TrackListModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun trackListFragment(): TrackListFragment

    @ActivityScoped
    @Binds
    abstract fun bindTrackListPresenter(trackListPresenter: TrackListPresenter): TrackListContract.Presenter
}