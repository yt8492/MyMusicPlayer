package jp.zliandroid.mymusicplayer.di.module

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.zliandroid.mymusicplayer.di.scope.ActivityScoped
import jp.zliandroid.mymusicplayer.di.scope.FragmentScoped
import jp.zliandroid.mymusicplayer.musicplay.MusicPlayContract
import jp.zliandroid.mymusicplayer.musicplay.MusicPlayFragment
import jp.zliandroid.mymusicplayer.musicplay.MusicPlayPresenter

@Module
abstract class MusicPlayModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun musicPlayFragment(): MusicPlayFragment

    @ActivityScoped
    @Binds
    abstract fun bindMusicPlayPresenter(musicPlayPresenter: MusicPlayPresenter): MusicPlayContract.Presenter
}