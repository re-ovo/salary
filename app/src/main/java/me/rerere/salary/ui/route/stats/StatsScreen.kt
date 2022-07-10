package me.rerere.salary.ui.route.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.rerere.salary.model.Salary
import me.rerere.salary.ui.component.BackIcon
import me.rerere.salary.ui.component.Md3TopBar
import me.rerere.salary.ui.util.RootGraph
import me.rerere.salary.ui.util.plus
import me.rerere.salary.util.DataState
import me.rerere.salary.util.formatMonth
import java.util.*

@RootGraph
@Destination
@Composable
fun StatsScreen(
    navigator: DestinationsNavigator,
    viewModel: StatsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("数据统计")
                },
                navigationIcon = {
                    BackIcon(navigator)
                }
            )
        }
    ) { innerPadding ->
        val stats by viewModel.stats.collectAsState()
        when (stats) {
            is DataState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues() + PaddingValues(16.dp) + innerPadding
                ) {
                    item {
                        Text(text = "基础数据", style = MaterialTheme.typography.titleLarge)
                    }

                    item {
                        Card {
                            Column(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "本月已发放工资 ${stats.read().salaryMonth} 元",
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text(
                                    text = "当前有员工 ${stats.read().users} 人",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                    }

                    item {
                        Text(text = "本月发放记录", style = MaterialTheme.typography.titleLarge)
                    }

                    items(stats.read().records) {
                        SalaryCard(it)
                    }
                }
            }
            is DataState.Loading -> {
                CircularProgressIndicator()
            }
            is DataState.Error -> {
                Text("加载错误")
            }
            else -> {}
        }
    }
}

@Composable
private fun SalaryCard(salary: Salary) {
    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Date(salary.time).formatMonth(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "￥${salary.salary}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "工号: ${salary.eid}"
            )

            Text(
                text = "基础工资: ${salary.base}"
            )

            Text(
                text = "绩效工资: ${salary.perf}"
            )

            Text(
                text = "福利工资: ${salary.welfare}"
            )

            Text(
                text = "特殊工资: ${salary.special}"
            )

            Text(
                text = "加班费: ${salary.overtime}"
            )

            Text(
                text = "补助: ${salary.subsidy}"
            )

            Text(
                text = "扣款: ${salary.debit}"
            )

            Text(
                text = salary.description
            )
        }
    }
}