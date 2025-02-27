package org.koreader.launcher.dialog

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import org.koreader.launcher.device.LightsInterface

class LightDialog {

    companion object {
        private const val LIGHT_DIALOG_CLOSED = -1
        private const val LIGHT_DIALOG_OPENED = 0
        private const val LIGHT_DIALOG_CANCEL = 1
        private const val LIGHT_DIALOG_OK = 2
    }

    var state = LIGHT_DIALOG_CLOSED

    fun show(
        activity: Activity,
        controller: LightsInterface,
        title: String,
        dim: String,
        warmth: String,
        okButton: String,
        cancelButton: String
    ) {
        val hasWarmth = controller.hasWarmth()
        state = LIGHT_DIALOG_OPENED
        activity.runOnUiThread {
            val titleText = TextView(activity)
            val dimText = TextView(activity)
            val dimSeekBar = SeekBar(activity)
            titleText.text = title
            titleText.gravity = Gravity.CENTER_HORIZONTAL
            titleText.height = 30
            titleText.textSize = 18f
            dimText.text = dim
            dimText.gravity = Gravity.CENTER_HORIZONTAL
            dimText.textSize = 18f
            dimSeekBar.max = controller.getMaxBrightness()
            dimSeekBar.progress = controller.getBrightness(activity)
            dimSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    controller.setBrightness(activity, p1)
                }
            })
            val linearLayout = LinearLayout(activity)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.addView(dimText)
            linearLayout.addView(dimSeekBar)
            if (hasWarmth) {
                val warmthText = TextView(activity)
                val warmthSeekBar = SeekBar(activity)
                warmthText.text = warmth
                warmthText.gravity = Gravity.CENTER_HORIZONTAL
                warmthText.textSize = 18f
                warmthSeekBar.max = controller.getMaxWarmth()
                warmthSeekBar.progress = controller.getWarmth(activity)
                warmthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        controller.setWarmth(activity, p1)
                    }
                })
                linearLayout.addView(warmthText)
                linearLayout.addView(warmthSeekBar)
            }

            val builder = AlertDialog.Builder(activity)
            builder.setCustomTitle(titleText)
                .setCancelable(false)
                .setPositiveButton(okButton) {
                        _, _ -> state = LIGHT_DIALOG_OK
                }
                .setNegativeButton(cancelButton) {
                        _, _ -> state = LIGHT_DIALOG_CANCEL
                }

            val dialog: AlertDialog = builder.create()
            dialog.setView(linearLayout)
            dialog.show()
        }
    }
}
