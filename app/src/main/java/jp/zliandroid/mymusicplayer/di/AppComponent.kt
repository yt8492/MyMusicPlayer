package jp.zliandroid.mymusicplayer.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository_Factory
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class])
interface AppComponent {

}