package com.ps.omarmattr.qalby.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.ps.omarmattr.qalby.databinding.FragmentQiblaBinding
import com.ps.omarmattr.qalby.model.SLocation
import com.ps.omarmattr.qalby.other.PREFERENCES_LOCATION
import com.ps.omarmattr.qalby.other.QIBLA_LATITUDE
import com.ps.omarmattr.qalby.other.QIBLA_LONGITUDE
import com.ps.omarmattr.qalby.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QiblaFragment : Fragment(), SensorEventListener {

    private val mBinding by lazy {
        FragmentQiblaBinding.inflate(layoutInflater)
    }

    var currentDegree: Float = 0f
    var currentNeedleDegree: Float = 0f

    lateinit var sensorManager: SensorManager
    lateinit var acc: Sensor
    lateinit var mag: Sensor
    lateinit var userLocation: Location

    lateinit var needleAnimation: RotateAnimation
    private var accArray = FloatArray(3)
    private var magArray = FloatArray(3)
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        needleAnimation = RotateAnimation(
            currentNeedleDegree,
            0f,
            Animation.RELATIVE_TO_SELF,
            .5f,
            Animation.RELATIVE_TO_SELF,
            .5f
        )

        userLocation = Location("Destination Location")
        preferencesManager.sharedPreferences.getString(
            PREFERENCES_LOCATION,
            null
        )?.let {
            val location = Gson().fromJson(it, SLocation::class.java)
            userLocation.latitude = location.lat
            userLocation.longitude = location.lng

        }


        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null &&
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null
        ) {
            acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        }


    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        when (event!!.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> magArray = event.values
            Sensor.TYPE_ACCELEROMETER -> accArray = event.values
        }
        SensorManager.getRotationMatrix(rotationMatrix, null, accArray, magArray)
        SensorManager.getOrientation(rotationMatrix, orientation)
        val value = Math.toDegrees(orientation[0].toDouble()).toFloat() * -1
        var head = value

        val destLocation = Location("Destination Location")
        destLocation.latitude = QIBLA_LATITUDE
        destLocation.longitude = QIBLA_LONGITUDE

        var bearTo = userLocation.bearingTo(destLocation)

        val geoField = GeomagneticField(
            userLocation.latitude.toFloat(),
            userLocation.longitude.toFloat(),
            userLocation.altitude.toFloat(),
            System.currentTimeMillis()
        )

        head -= geoField.declination

        if (bearTo < 0) {
            bearTo += 360
        }

        var direction = bearTo - head

        if (direction < 0) {
            direction += 360
        }

       // mBinding.tvHeading.text = "Heading : $value + degrees"

        Log.d("TAG", "Needle Degree : $currentNeedleDegree, Direction : $direction")

        needleAnimation = RotateAnimation(
            currentNeedleDegree,
            direction,
            Animation.RELATIVE_TO_SELF,
            .5f,
            Animation.RELATIVE_TO_SELF,
            .5f
        )
        needleAnimation.fillAfter = true
        needleAnimation.duration = 200
        mBinding.ivQiblaDirection.startAnimation(needleAnimation)
        currentNeedleDegree = direction
        currentDegree = -value


}

override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
}


}