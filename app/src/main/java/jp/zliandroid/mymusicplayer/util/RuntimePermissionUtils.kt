package jp.zliandroid.mymusicplayer.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
 class RuntimePermissionUtils {

    companion object {
        fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true
            }
            for (permission in permissions) {
                if (context.checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        fun checkGrantResults(vararg grantResults: Int): Boolean {
            if (grantResults.size == 0) throw IllegalArgumentException("grantResults is empty")
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
    }
}