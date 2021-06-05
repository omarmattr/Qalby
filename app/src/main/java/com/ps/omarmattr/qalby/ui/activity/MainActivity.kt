package com.ps.omarmattr.qalby.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var navHostFragment: Fragment? = null
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_nav_host_home)

        val navController = navHostFragment!!.findNavController()

        NavigationUI.setupWithNavController(
            mBinding.bottomNavigation,
            navController
        )



/*
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment,null,getNavOptions())
                }
                else->{
                    navController.navigate(item.itemId,null,null)
                }
            }
            true
        }*/
/*
        navHostFragment!!.findNavController()
            .addOnDestinationChangedListener { _: NavController?, destination: NavDestination, arguments: Bundle? ->
                when (destination.id) {
                    R.id.splashFragment->{
                        window.apply {
                            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                    }
                    R.id.homeFragment, R.id.orderFragment -> {
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
            }*/
    }
}