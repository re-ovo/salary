package me.rerere.salary.ui.route.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.model.User
import me.rerere.salary.util.DataState
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val salaryAPI: SalaryAPI
): ViewModel() {
    val userProfile: MutableStateFlow<DataState<User>> = MutableStateFlow(DataState.Empty)

    fun load(){
        viewModelScope.launch {
            userProfile.value = DataState.Loading
            try {
                val response = salaryAPI.getSelf()
                println(response)
                userProfile.value = DataState.Success(response.body!!)
            } catch (e: Exception){
                e.printStackTrace()
                userProfile.value = DataState.Error(e.javaClass.name)
            }
        }
    }

    init {
        load()
    }
}