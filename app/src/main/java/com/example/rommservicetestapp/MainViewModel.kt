package com.example.rommservicetestapp

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val counterServiceManager: CounterServiceManager,
    private val dao: CounterDao
) : ViewModel() {

    fun startService() {
        counterServiceManager.startService()
        countInit.start()
    }

    fun stopService() {
        counterServiceManager.stopService()
        countInit.cancel()
    }

    private val countInit = object : CountDownTimer(10000 * 60 * 60, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            viewModelScope.launch {
                dao.insertCounter(Counter(10000, millisUntilFinished))
            }
        }

        override fun onFinish() {
            viewModelScope.launch {
                dao.insertCounter(Counter(10000, 0))
            }
        }
    }
}