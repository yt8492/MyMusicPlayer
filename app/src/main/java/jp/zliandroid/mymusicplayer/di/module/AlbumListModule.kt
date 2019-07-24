package jp.zliandroid.mymusicplayer.di.module

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.zliandroid.mymusicplayer.albumlist.AlbumListContract
import jp.zliandroid.mymusicplayer.albumlist.AlbumListFragment
import jp.zliandroid.mymusicplayer.albumlist.AlbumListPresenter
import jp.zliandroid.mymusicplayer.di.scope.ActivityScoped
import jp.zliandroid.mymusicplayer.di.scope.FragmentScoped

@Module
abstract class AlbumListModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun albumListFragmnet(): AlbumListFragment

    @ActivityScoped
    @Binds
    abstract fun bindAlbumListPresenter(albumListPresenter: AlbumListPresenter): AlbumListContract.Presenter
}