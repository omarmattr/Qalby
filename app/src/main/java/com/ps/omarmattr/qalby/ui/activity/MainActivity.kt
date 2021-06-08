package com.ps.omarmattr.qalby.ui.activity

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
import com.example.easywaylocation.GetLocationDetail
import com.example.easywaylocation.Listener
import com.google.android.gms.location.LocationRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.databinding.ActivityMainBinding
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Listener {
    private var navHostFragment: Fragment? = null
    private lateinit var mBinding: ActivityMainBinding
    var easyWayLocation: EasyWayLocation? = null
    @Inject
    lateinit var viewModel:MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        requestPermissions()
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_nav_host_home)

        mBinding.bottomNavigation.itemIconTintList = null

        val navController = navHostFragment!!.findNavController()
        NavigationUI.setupWithNavController(
            mBinding.bottomNavigation,
            navController
        )



        mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.destination_more -> {
                    navController.navigate(item.itemId, null, null)
                }
                else -> {
                    navController.navigate(item.itemId, null, null)
                }
            }
            true
        }

        navHostFragment!!.findNavController()
            .addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
                when (destination.id) {
                    R.id.splashFragment -> {
                        window.apply {
                            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                        mBinding.bottomNavigation.visibility = View.GONE
                    }
                    R.id.destination_home, R.id.destination_bulletin, R.id.destination_dua, R.id.destination_more, R.id.destination_solah -> {

                        window.apply {
                            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                        mBinding.bottomNavigation.visibility = View.VISIBLE
                    }

                    else -> {
                        window.apply {
                            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                        mBinding.bottomNavigation.visibility = View.GONE
                    }
                }
            }
    }

    override fun locationOn() {
        Toast.makeText(this, "Location ON", Toast.LENGTH_SHORT).show()
    }

    override fun currentLocation(location: Location) {

        viewModel.getLocation(location.latitude,location.longitude)
    }

    override fun locationCancelled() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTING_REQUEST_CODE -> easyWayLocation!!.onActivityResult(resultCode)
        }
    }


    private fun requestPermissions() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Log.e("ooooooo", "onPermissionGranted")


                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0!!.isPermanentlyDenied) Log.e("ooooooo", "onPermissionDenied")

                    LocationRequest.create().apply {
                        interval = 10000
                        priority = LocationRequest.PRIORITY_LOW_POWER
                    }.also {
                        easyWayLocation =
                            EasyWayLocation(
                                this@MainActivity, it, true,
                                false, this@MainActivity
                            )
                    }
                    easyWayLocation!!.startLocation()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    Log.e("ooooooo", "onPermissionRationaleShouldBeShown")
                    p1!!.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(
                    applicationContext,
                    "Error occurred! ",
                    Toast.LENGTH_SHORT
                ).show()
            }.onSameThread().check()
    }



}


