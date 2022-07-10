package me.rerere.salary.ui.route.login

import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.sharedPreferenceOf
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val salaryAPI: SalaryAPI
) : ViewModel(){
    fun login(eid: Int, password: String, callBack: (Int) -> Unit) {
        viewModelScope.launch {
            runCatching { salaryAPI.login(eid, password) }
                .getOrNull()
                ?.let {
                    sharedPreferenceOf("jwt").edit {
                        putString("token", it.body)
                    }
                    callBack(it.code)
                }
        }
    }
}