package com.grigoryev.teya_home.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {
    
    protected val testDispatcher = StandardTestDispatcher()
    
    @Before
    open fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    
    @After
    open fun tearDown() {
        Dispatchers.resetMain()
    }
} 