package me.rerere.salary.ui.route.salarymanage

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.salary.model.Salary
import me.rerere.salary.ui.component.BackIcon
import me.rerere.salary.ui.component.Md3TopBar
import me.rerere.salary.ui.util.RootGraph

@RootGraph
@Destination
@Composable
fun SalaryManageScreen(
    navigator: DestinationsNavigator,
    viewModel: SalaryManageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var eid by remember {
        mutableStateOf("")
    }
    var base by remember {
        mutableStateOf(0f)
    }
    var perf by remember {
        mutableStateOf(0f)
    }
    var welfare by remember {
        mutableStateOf(0f)
    }
    var special by remember {
        mutableStateOf(0f)
    }
    var overtime by remember {
        mutableStateOf(0f)
    }
    var subsidy by remember {
        mutableStateOf(0f)
    }
    var debit by remember {
        mutableStateOf(0f)
    }
    var description by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("工资管理")
                },
                navigationIcon = {
                    BackIcon(navigator)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    viewModel.addSalary(
                        Salary(
                            eid = eid.toInt(),
                            salary = base + perf + welfare + special + overtime + subsidy - debit,
                            time = System.currentTimeMillis(),
                            base = base.toFloat(),
                            perf = perf.toFloat(),
                            welfare = welfare.toFloat(),
                            special = special.toFloat(),
                            overtime = overtime.toFloat(),
                            subsidy = subsidy.toFloat(),
                            debit = debit.toFloat(),
                            description = description
                        )
                    ) {
                        if (it) {
                            Toast.makeText(context, "添加工资数据成功！", Toast.LENGTH_SHORT).show()
                            navigator.popBackStack()
                        } else {
                            Toast.makeText(context, "添加工资数据失败！", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text("提交")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "发放工资",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = eid,
                onValueChange = {
                    eid = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("员工编号")
                }
            )

            OutlinedTextField(
                value = base.toString(),
                onValueChange = {
                    base = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("基本工资")
                }
            )

            OutlinedTextField(
                value = perf.toString(),
                onValueChange = {
                    perf = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("绩效工资")
                }
            )

            OutlinedTextField(
                value = welfare.toString(),
                onValueChange = {
                    welfare = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("福利")
                }
            )

            OutlinedTextField(
                value = special.toString(),
                onValueChange = {
                    special = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("特殊工资")
                }
            )

            OutlinedTextField(
                value = overtime.toString(),
                onValueChange = {
                    overtime = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("加班")
                }
            )

            OutlinedTextField(
                value = subsidy.toString(),
                onValueChange = {
                    subsidy = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("补贴")
                }
            )

            OutlinedTextField(
                value = debit.toString(),
                onValueChange = {
                    debit = it.toFloatOrNull() ?: 0f
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("扣款")
                }
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("备注")
                }
            )
        }
    }
}