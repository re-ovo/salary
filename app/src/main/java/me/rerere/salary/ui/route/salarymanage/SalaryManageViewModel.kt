package me.rerere.salary.ui.route.salarymanage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.model.Salary
import javax.inject.Inject

@HiltViewModel
class SalaryManageViewModel @Inject constructor(
    private val salaryAPI: SalaryAPI
) : ViewModel(){
    fun addSalary(
        salary: Salary,
        result: (Boolean) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = salaryAPI.addSalary(salary)
                result(response.code == 0)
            } catch (e: Exception){
                e.printStackTrace()
                result(false)
            }
        }
    }
}