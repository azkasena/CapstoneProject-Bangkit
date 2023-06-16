package com.vie.healthyteeth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vie.healthyteeth.model.UserModel
import com.vie.healthyteeth.model.UserPreference
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}