package jp.zliandroid.mymusicplayer.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import jp.zliandroid.mymusicplayer.App
import jp.zliandroid.mymusicplayer.di.module.ActivityModule
import jp.zliandroid.mymusicplayer.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityModule::class
        ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(app: App)
}