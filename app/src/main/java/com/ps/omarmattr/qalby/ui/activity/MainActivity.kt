package com.ps.omarmattr.qalby.ui.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.databinding.ActivityMainBinding
import com.ps.omarmattr.qalby.ui.dialog.MoreDialog


class MainActivity : AppCompatActivity() {
    private var navHostFragment: Fragment? = null
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_nav_host_home)
        mBinding.bottomNavigation.itemIconTintList = null
        val navController = navHostFragment!!.findNavController()
        NavigationUI.setupWithNavController(
            mBinding.bottomNavigation,
            navController
        )



       mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.destination_more -> {
                    navController.navigate(item.itemId,null,null)
                  //  moreMenuNavigate()
                }
                else->{
                    navController.navigate(item.itemId,null,null)
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


}