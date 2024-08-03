package com.urdubolo.com.pk.Util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.urdubolo.com.pk.R

class UtilAnimation(private val context: Context) {

    private var dialog: Dialog = Dialog(context)

    fun startLoadingAnimation() {
        if (!dialog.isShowing) {
            dialog = Dialog(context).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
                show()
            }
        }
    }

    fun endLoadingAnimation() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}
