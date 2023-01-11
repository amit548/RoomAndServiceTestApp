package com.example.rommservicetestapp

import javax.inject.Inject

class CounterManager @Inject constructor(
    dao: CounterDao
) {

    val counter = dao.getCounter(10000)
}