package me.rerere.salary.ui.route.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.model.Stats
import me.rerere.salary.util.DataState
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val salaryAPI: SalaryAPI
) : ViewModel(){
    val stats : MutableStateFlow<DataState<Stats>> = MutableStateFlow(DataState.Empty)

    fun load(){
        viewModelScope.launch {
            stats.value = DataState.Loading
            try {
                val response = salaryAPI.getStats()
                stats.value = DataState.Success(response.body!!)
            } catch (e: Exception){
                e.printStackTrace()
                stats.value = DataState.Error(e.javaClass.name)
            }
        }
    }

    init {
        load()
    }
}