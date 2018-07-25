package jp.zliandroid.mymusicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import jp.zliandroid.mymusicplayer.fragments.PlayerFragment

class ControlReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val control = it.getIntExtra("control", ERROR_CODE)
            if (control == PlayerFragment.MUSIC_START){
                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
            } else if (control == PlayerFragment.MUSIC_STOP){
                Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show()
            } else if (control == PlayerFragment.MUSIC_BACK){
                Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
            } else if (control == PlayerFragment.MUSIC_NEXT){
                Toast.makeText(context, "next", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val ERROR_CODE = -1
    }
}