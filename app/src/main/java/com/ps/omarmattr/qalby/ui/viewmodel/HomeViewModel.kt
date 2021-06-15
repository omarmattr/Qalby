package com.ps.omarmattr.qalby.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ps.omarmattr.qalby.repository.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val socialRepository: SocialRepository
) : AndroidViewModel(application) {
    fun getSocial() =
        socialRepository.getSocial()

    fun getSocialLiveData() = socialRepository.getSocialLiveData()
    fun getSocialFacebookLiveData() = socialRepository.getSocialFacebookLiveData()
}