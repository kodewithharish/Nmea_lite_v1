package com.accord.nmea.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.accord.nmea.R

object LibUIUtils {

    /**
     * Ask the user if they want to enable GPS, and if so, show them system settings
     */
    fun promptEnableGps(context: Context, activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage(context.getString(R.string.enable_gps_message))
            .setPositiveButton(
                context.getString(R.string.enable_gps_positive_button)
            ) { dialog: DialogInterface?, which: Int ->
                val intent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                activity.startActivity(intent)
            }
            .setNegativeButton(
                context.getString(R.string.enable_gps_negative_button)
            ) { dialog: DialogInterface?, which: Int -> }
            .show()
    }


    /**
     * Shows the dialog to explain why location permissions are needed
     *
     * NOTE - this dialog can't be managed under the old dialog framework as the method
     * ActivityCompat.shouldShowRequestPermissionRationale() always returns false.
     */
    fun showLocationPermissionDialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
            .setTitle(R.string.title_location_permission)
            .setMessage(R.string.text_location_permission)
            .setCancelable(false)
            .setPositiveButton(
                R.string.ok
            ) { dialog: DialogInterface?, which: Int ->
                // Request permissions from the user
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PermissionUtils.LOCATION_PERMISSION_REQUEST
                )
            }
            .setNegativeButton(
                R.string.exit
            ) { dialog: DialogInterface?, which: Int ->
                // Exit app
                activity.finish()
            }
        builder.create().show()
    }



    /**
     * Converts screen dimension units from dp to pixels, based on algorithm defined in
     * http://developer.android.com/guide/practices/screens_support.html#dips-pels
     *
     * @param dp value in dp
     * @return value in pixels
     */
    @JvmStatic
    fun dpToPixels(context: Context, dp: Float): Int {
        // Get the screen's density scale
        val scale = context.resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * Returns true if this device supports the Sensor.TYPE_ROTATION_VECTOR sensor, false if it
     * doesn't
     *
     * @return true if this device supports the Sensor.TYPE_ROTATION_VECTOR sensor, false if it
     * doesn't
     */
    fun isRotationVectorSensorSupported(context: Context): Boolean {
        val sensorManager = context
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null
    }

}