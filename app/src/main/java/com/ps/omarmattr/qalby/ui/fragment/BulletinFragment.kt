package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.databinding.FragmentBulletinBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class BulletinFragment : Fragment() {

private val mBinding by lazy{
    FragmentBulletinBinding.inflate(layoutInflater)
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )= mBinding.root


}