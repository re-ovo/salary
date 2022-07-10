package me.rerere.salary.ui.route.salary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.ma.charts.line.LineChart
import hu.ma.charts.line.data.LineChartData
import hu.ma.charts.pie.PieChart
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
import kotlinx.coroutines.launch
import me.rerere.salary.model.Salary
import me.rerere.salary.ui.component.AppBarStyle
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
fun SalaryScreen(
    navigator: DestinationsNavigator,
    viewModel: SalaryViewModel = hiltViewModel(),
    userId: Int
) {
    val scrollBehavior = remember {
        TopAppBarDefaults.enterAlwaysScrollBehavior()
    }
    LaunchedEffect(Unit){
        viewModel.getSalaryData(userId)
    }
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("工资信息")
                },
                appBarStyle = AppBarStyle.Small,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackIcon(navigator)
                }
            )
        }
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState()
        Column(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(innerPadding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(0)
                        }
                    },
                    text = {
                        Text("工资信息")
                    }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(1)
                        }
                    },
                    text = {
                        Text("工资统计")
                    }
                )
            }
            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) {
                when(it){
                    0 -> {
                        SalaryHistory(viewModel)
                    }
                    1 -> {
                        SalaryChart(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun SalaryHistory(viewModel: SalaryViewModel) {
    val state by viewModel.salaryHistory.collectAsState()
    when(state){
        is DataState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = WindowInsets.navigationBars.asPaddingValues() + PaddingValues(16.dp)
            ) {
                state.readSafely()?.let { items ->
                    items(items.reversed()) {
                        SalaryCard(it)
                    }
                }
            }
        }
        is DataState.Loading -> {
            CircularProgressIndicator()
        }
        is DataState.Error -> {
            Text("出错了")
        }
        else -> {}
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
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "￥${salary.salary}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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

@Composable
private fun SalaryChart(viewModel: SalaryViewModel) {
    val state by viewModel.salaryHistory.collectAsState()
    val monthSalary = state.readSafely()?.firstOrNull {
        Date(it.time).formatMonth() == Date().formatMonth()
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "本月工资成分图",
            style = MaterialTheme.typography.titleLarge
        )
        AnimatedVisibility(monthSalary != null) {
            if(monthSalary != null) {
                Card {
                    PieChart(
                        modifier = Modifier.padding(16.dp),
                        data = monthSalary.toPieChartData(),
                        useMinimumSliceAngle = false
                    )
                }
            }
        }
        Text("工资变化图", style = MaterialTheme.typography.titleLarge)
        AnimatedVisibility(state is DataState.Success) {
            val historyData = state.readSafely()?.takeLast(12)
            if(historyData?.isNotEmpty() == true) {
                Card {
                    Box(Modifier.padding(16.dp)) {
                        LineChart(
                            chartHeight = 100.dp,
                            data = LineChartData(
                                series = listOf(
                                    LineChartData.SeriesData(
                                        title = "工资走势",
                                        points = historyData.mapIndexed { index, salary ->
                                            LineChartData.SeriesData.Point(
                                                x = index,
                                                value = salary.salary
                                            )
                                        },
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ),
                                autoYLabels = true,
                                xLabels = historyData.map {
                                    Date(it.time).formatMonth()
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}
