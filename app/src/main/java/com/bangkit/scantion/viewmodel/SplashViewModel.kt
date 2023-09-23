package com.bangkit.scantion.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.firebase.AuthRepository
import com.bangkit.scantion.data.preference.theme.ThemeManager
import com.bangkit.scantion.navigation.Graph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    repository: AuthRepository,
    private val themeManager: ThemeManager
) : ViewModel() {
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Graph.AUTHENTICATION)
    val startDestination: State<String> = _startDestination

    private val _initTheme = mutableStateOf(false)
    val initTheme: State<Boolean> = _initTheme

    private val _darkTheme = mutableStateOf(false)
    val darkTheme: State<Boolean> = _darkTheme

    init {
        if (repository.currentUser != null) {
            _startDestination.value = Graph.HOME
        } else {
            _startDestination.value = Graph.AUTHENTICATION
        }

        viewModelScope.launch(Dispatchers.IO) {
            themeManager.getInitTheme().collect {
                _initTheme.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            themeManager.getDarkMode().collect {
                _darkTheme.value = it
            }
        }
        viewModelScope.launch {
            delay(500)
            _isLoading.value = false
        }
    }
}