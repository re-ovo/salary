package me.rerere.salary.ui.route.index

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.salary.ui.component.AppBarStyle
import me.rerere.salary.ui.component.AssistChip
import me.rerere.salary.ui.component.Md3TopBar
import me.rerere.salary.ui.route.destinations.*
import me.rerere.salary.ui.route.login.LoginViewModel
import me.rerere.salary.ui.util.RootGraph
import me.rerere.salary.util.DataState

@RootGraph(start = true)
@Destination
@Composable
fun IndexScreen(navigator: DestinationsNavigator, viewModel: IndexViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("工资管理系统")
                },
                actions = {
                    var expandMenu by remember { mutableStateOf(false) }
                    IconButton(onClick = { expandMenu = !expandMenu }) {
                        Icon(Icons.Rounded.MoreVert, null)
                        DropdownMenu(
                            expanded = expandMenu,
                            onDismissRequest = { expandMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("注销登录") },
                                onClick = {
                                    expandMenu = false
                                    navigator.navigate(LoginScreenDestination) {
                                        popUpTo(IndexScreenDestination.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                },
                appBarStyle = AppBarStyle.Large
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileCard(viewModel)
                val userProfile by viewModel.userProfile.collectAsState()
                Category("员工区") {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            navigator.navigate(
                                SalaryScreenDestination(
                                    userProfile.readSafely()?.eid ?: 0
                                )
                            )
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Icon(Icons.Rounded.Person, null)

                            Text(text = "我的工资数据", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }

                Category("管理区") {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            if (userProfile.readSafely()?.role == 1) {
                                navigator.navigate(StatsScreenDestination)
                            } else {
                                Toast.makeText(context, "你没有权限这样做！", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Icon(Icons.Rounded.Build, null)

                            Text(text = "报表", style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            if (userProfile.readSafely()?.role == 1) {
                                navigator.navigate(UserManageScreenDestination)
                            } else {
                                Toast.makeText(context, "你没有权限这样做！", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Icon(Icons.Rounded.Person, null)

                            Text(text = "用户管理", style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            if (userProfile.readSafely()?.role == 1) {
                                navigator.navigate(SalaryManageScreenDestination)
                            } else {
                                Toast.makeText(context, "你没有权限这样做！", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Icon(Icons.Rounded.MailOutline, null)

                            Text(text = "工资管理", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(indexViewModel: IndexViewModel) {
    val user by indexViewModel.userProfile.collectAsState()
    ElevatedCard(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        when (user) {
            is DataState.Success -> {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "欢迎你 ${user.read().name} (${user.read().eid})",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip {
                            Text(
                                text = "权限: ${user.read().roleName}"
                            )
                        }

                        AssistChip {
                            Text(
                                text = "职位: ${user.read().rank}"
                            )
                        }

                        AssistChip {
                            Text(
                                text = "部门: ${user.read().department}"
                            )
                        }
                    }
                }
            }
            is DataState.Loading -> {
                CircularProgressIndicator()
            }
            is DataState.Error -> {
                Text("错误!")
            }
            else -> {}
        }
    }
}

@Composable
private fun Category(
    name: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Column {
            content()
        }
    }
}