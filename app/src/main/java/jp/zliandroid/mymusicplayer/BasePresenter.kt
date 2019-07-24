package jp.zliandroid.mymusicplayer

interface BasePresenter<T> {
    fun takeView(view: T)
    fun start()
}