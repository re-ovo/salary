package me.rerere.salary.ui.route.user

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.salary.model.User
import me.rerere.salary.ui.component.AssistChip
import me.rerere.salary.ui.component.Md3TopBar
import me.rerere.salary.ui.route.destinations.SalaryScreenDestination
import me.rerere.salary.ui.util.RootGraph
import me.rerere.salary.ui.util.plus
import me.rerere.salary.util.DataState

@RootGraph
@Composable
@Destination
fun UserManageScreen(
    navigator: DestinationsNavigator,
    viewModel: UserManageViewModel = hiltViewModel()
) {
    var addingUser by remember { mutableStateOf(false) }
    var filterContent by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("用户管理")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.popBackStack()
                    }) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { addingUser = !addingUser }) {
                        Icon(Icons.Rounded.Add, null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            AnimatedVisibility(addingUser) {
                AddUserCard(viewModel) {
                    addingUser = false
                }
            }
            OutlinedTextField(
                value = filterContent,
                onValueChange = { text ->
                    filterContent = text
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text("输入用户名字进行搜索")
                }
            )
            Body(viewModel, filterContent, navigator)
        }
    }
}


@Composable
private fun AddUserCard(viewModel: UserManageViewModel, close: () -> Unit) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("添加用户")
            var eid by remember {
                mutableStateOf("")
            }
            var name by remember {
                mutableStateOf("")
            }
            var rank by remember {
                mutableStateOf("")
            }
            var department by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }
            var admin by remember {
                mutableStateOf(false)
            }
            OutlinedTextField(
                value = eid,
                onValueChange = { eid = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("工号")
                }
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("姓名")
                }
            )
            OutlinedTextField(
                value = rank,
                onValueChange = { rank = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("职级")
                }
            )
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("部门")
                }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("密码")
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = admin,
                    onCheckedChange = {
                        admin = it
                    }
                )
                Text("管理员", modifier = Modifier.weight(1f))

                OutlinedButton(onClick = {
                    close()
                }) {
                    Text("取消")
                }

                Button(onClick = {
                    viewModel.addUser(
                        eid = eid.toIntOrNull() ?: 0,
                        name = name,
                        rank = rank,
                        department = department,
                        role = if (admin) 1 else 0,
                        password = password
                    ) {
                        close()
                        if (it) {
                            Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("添加")
                }
            }
        }
    }
}

@Composable
private fun Body(
    viewModel: UserManageViewModel,
    filter: String,
    navigator: DestinationsNavigator
) {
    val userList by viewModel.users.collectAsState()
    when (userList) {
        is DataState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = WindowInsets.navigationBars.asPaddingValues() + PaddingValues(
                    horizontal = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                userList.readSafely()
                    ?.filter { it.name.contains(filter) }
                    ?.let {
                        items(it) { user ->
                            UserCard(user, viewModel, navigator)
                        }
                    }
            }
        }
        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else -> {}
    }
}

@Composable
private fun UserCard(user: User, viewModel: UserManageViewModel, navigator: DestinationsNavigator) {
    val context = LocalContext.current
    var editingUser by remember { mutableStateOf(false) }
    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${user.name} (${user.eid})",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip {
                    Text(
                        text = "权限: ${user.roleName}"
                    )
                }

                AssistChip {
                    Text(
                        text = "职位: ${user.rank}"
                    )
                }

                AssistChip {
                    Text(
                        text = "部门: ${user.department}"
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { editingUser = true }) {
                    Text("编辑用户")
                }

                var deleteDialog by remember {
                    mutableStateOf(false)
                }
                if(deleteDialog) {
                    AlertDialog(
                        onDismissRequest = { deleteDialog = false },
                        title = {
                            Text("删除用户")
                        },
                        text = {
                            Text("是否删除该用户?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.delete(user.eid) {
                                        deleteDialog = false
                                        if (it) {
                                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }
                            ) {
                                Text("确定")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { deleteDialog = false }) {
                                Text("取消")
                            }
                        }
                    )
                }
                TextButton(onClick = {
                    deleteDialog = true
                }) {
                    Text("删除用户")
                }

                TextButton(onClick = {
                    navigator.navigate(SalaryScreenDestination(user.eid))
                }) {
                    Text("查看用户工资数据")
                }
            }

            AnimatedVisibility(editingUser) {
                Column {
                    Text("编辑用户信息")
                    var rank by remember { mutableStateOf(user.rank) }
                    var department by remember { mutableStateOf(user.department) }
                    var name by remember { mutableStateOf(user.name) }
                    var password by remember { mutableStateOf("") }
                    var admin by remember { mutableStateOf(user.role == 1) }
                    OutlinedTextField(
                        label = { Text("姓名") },
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text("职位") },
                        value = rank,
                        onValueChange = {
                            rank = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text("部门") },
                        value = department,
                        onValueChange = {
                            department = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text("密码") },
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("管理员:")
                        Checkbox(
                            checked = admin,
                            onCheckedChange = {
                                admin = it
                            }
                        )

                        OutlinedButton(onClick = {
                            editingUser = false
                        }) {
                            Text("取消")
                        }

                        Button(onClick = {
                            viewModel.updateUser(
                                user.eid,
                                name,
                                rank,
                                department,
                                password,
                                if (admin) 1 else 0
                            ) {
                                if (it) {
                                    Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show()
                                }
                                editingUser = false
                            }
                        }) {
                            Text("保存")
                        }
                    }
                }
            }
        }
    }
}