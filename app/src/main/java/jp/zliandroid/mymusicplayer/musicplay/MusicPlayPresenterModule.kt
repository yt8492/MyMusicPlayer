//package jp.zliandroid.mymusicplayer.musicplay
//
//import com.github.salomonbrys.kodein.Kodein
//import com.github.salomonbrys.kodein.bind
//import com.github.salomonbrys.kodein.instance
//
//fun musicPlayPresenterModule(view: MusicPlayContract.View) = Kodein.Module {
//    bind<MusicPlayContract.View>() with instance(view)
//}