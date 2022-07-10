package me.rerere.salary.ui.route.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.model.User
import me.rerere.salary.util.DataState
import javax.inject.Inject

@HiltViewModel
class UserManageViewModel @Inject constructor(
    private val salaryAPI: SalaryAPI
): ViewModel(){
    val users: MutableStateFlow<DataState<List<User>>> = MutableStateFlow(DataState.Empty)

    fun addUser(
        eid: Int,
        name: String,
        rank: String,
        department: String,
        role: Int,
        password: String,
        finish: (Boolean) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = salaryAPI.addUser(
                    eid,
                    name,
                    rank,
                    department,
                    role,
                    password
                )
                finish(response.code == 0)
                loadAllUsers()
            }catch (e: Exception){
                finish(false)
            }
        }
    }

    fun delete(eid: Int, finish: (Boolean) -> Unit){
        viewModelScope.launch {
            try {
                val response = salaryAPI.deleteUser(eid)
                finish(response.code == 0)
                loadAllUsers()
            }catch (e: Exception){
                e.printStackTrace()
                finish(false)
            }
        }
    }

    fun updateUser(
        eid: Int,
        name: String,
        rank: String,
        department: String,
        password: String,
        role: Int,
        finish: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = salaryAPI.updateUser(
                    eid = eid,
                    name = name,
                    rank = rank,
                    department = department,
                    password = password,
                    role = role
                )
                finish(result.code == 0)
                loadAllUsers()
            }catch (e: Exception){
                e.printStackTrace()
                finish(false)
            }
        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            users.value = DataState.Loading
            try {
                val userList = salaryAPI.getUsers()
                users.value = DataState.Success(userList.body!!)
            } catch (e: Exception){
                e.printStackTrace()
                users.value = DataState.Error(e.javaClass.simpleName)
            }
        }
    }

    init {
        loadAllUsers()
    }
}