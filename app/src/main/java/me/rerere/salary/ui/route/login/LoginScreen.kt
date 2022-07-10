package me.rerere.salary.ui.route.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.rerere.salary.ui.component.AppBarStyle
import me.rerere.salary.ui.component.Md3TopBar
import me.rerere.salary.ui.route.destinations.IndexScreenDestination
import me.rerere.salary.ui.route.destinations.LoginScreenDestination
import me.rerere.salary.ui.util.RootGraph

@RootGraph
@Composable
@Destination
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("登录你的账号")
                },
                appBarStyle = AppBarStyle.Medium
            )
        }
    ) {
        val scope = rememberCoroutineScope()
        var showDialog by remember {
            mutableStateOf(false)
        }
        var message by remember {
            mutableStateOf("")
        }
        if(showDialog){
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text("登录")
                },
                text = {
                    Text(message)
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("确定")
                    }
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var eid by remember {
                mutableStateOf("1")
            }
            var password by remember {
                mutableStateOf("admin")
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.75f),
                value = eid,
                onValueChange = {
                    eid = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text("员工ID")
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.75f),
                value = password,
                onValueChange = {
                    password = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                label = {
                    Text("密码")
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(0.75f),
                onClick = {
                    message = "登录中"
                    showDialog = true

                    viewModel.login(
                        eid = eid.toIntOrNull() ?: 1,
                        password = password
                    ) {
                        when (it) {
                            0 -> {
                                message = "登录成功"
                                scope.launch {
                                    delay(500)
                                    showDialog = false
                                    navigator.navigate(IndexScreenDestination) {
                                        popUpTo(LoginScreenDestination.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            1 -> {
                                message = "不存在此用户，请检查你的员工ID"
                            }
                            2 -> {
                                message = "密码错误"
                            }
                        }
                    }
                }
            ) {
                Text("登录")
            }
        }
    }
}