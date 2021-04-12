package com.muiezarif.kidsfitness.utils

import android.util.Log

open class  D {

    companion object {
        var IS_DEVELOPMENT_MODE = true
        val TAG = "com.ecdashboard"

        fun d(tag: String, msg: String) {
            if (IS_DEVELOPMENT_MODE)
                Log.d(tag, msg)
        }

        fun e(tag: String, msg: String) {
            if (IS_DEVELOPMENT_MODE)
                Log.e(tag, msg)
        }

        fun i(tag: String, msg: String) {
            if (IS_DEVELOPMENT_MODE) Log.i(tag, msg)
        }

        fun v(tag: String, msg: String) {
            if (IS_DEVELOPMENT_MODE)
                Log.v(tag, msg)
        }

        fun w(tag: String, msg: String) {
            if (IS_DEVELOPMENT_MODE)
                Log.w(tag, msg)
        }
    }

}