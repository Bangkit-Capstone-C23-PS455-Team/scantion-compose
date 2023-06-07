package com.bangkit.scantion.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.scantion.data.preference.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _initTheme = MutableStateFlow(false)
    val initTheme: StateFlow<Boolean> = _initTheme

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme

    init {
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
    }


    fun setInitTheme(isInitMode: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            themeManager.setInitTheme(isInitMode)
        }
    }

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            themeManager.saveDarkTheme(isDarkMode)
            Log.d("settingViewModel", "setDarkMode: $isDarkMode")
        }
    }
}