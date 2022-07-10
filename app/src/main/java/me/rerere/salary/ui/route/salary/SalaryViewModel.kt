package me.rerere.salary.ui.route.salary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.model.Salary
import me.rerere.salary.util.DataState
import javax.inject.Inject

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val salaryAPI: SalaryAPI
) : ViewModel() {
    val salaryHistory: MutableStateFlow<DataState<List<Salary>>> = MutableStateFlow(DataState.Empty)

    fun getSalaryData(eid: Int) {
        viewModelScope.launch {
            salaryHistory.value = DataState.Loading
            try {
                salaryHistory.value = DataState.Success(salaryAPI.getSalary(eid).body!!)
            } catch (e: Exception) {
                salaryHistory.value = DataState.Error(e.javaClass.name)
            }
        }
    }
}