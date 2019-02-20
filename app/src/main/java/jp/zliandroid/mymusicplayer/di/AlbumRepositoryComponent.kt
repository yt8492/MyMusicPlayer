package jp.zliandroid.mymusicplayer.di

import dagger.Component
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface AlbumRepositoryComponent {
    val albumRepository: AlbumRepository
}