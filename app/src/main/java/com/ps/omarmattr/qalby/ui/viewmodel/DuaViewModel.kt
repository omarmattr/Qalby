package com.ps.omarmattr.qalby.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ps.omarmattr.qalby.repository.DuaRepository
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DuaViewModel @Inject constructor(val duaRepository: DuaRepository, application: Application) :
    AndroidViewModel(application) {

    fun getDua(id: Int) {
        duaRepository.getDua(
            id
        )
    }

    val listDuaStateFlow: StateFlow<ResultRequest<Any>> = duaRepository.listDuaStateFlow


}