package com.example.rommservicetestapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val counterServiceManager: CounterServiceManager,
    private val dao: CounterDao
) : ViewModel() {

    fun startService() {
        counterServiceManager.startService()
    }

    fun stopService() {
        counterServiceManager.stopService()
    }
}