package me.rerere.salary.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry

data class Salary(
    val eid: Int,
    val salary: Float,
    val time: Long,
    val base: Float,
    val perf: Float,
    val welfare: Float,
    val special: Float,
    val overtime: Float,
    val subsidy: Float,
    val debit: Float,
    val description: String
) {
    fun toPieChartData(): PieChartData {
        return PieChartData(
            entries = listOf(
                PieChartEntry(
                    value = base,
                    label = AnnotatedString("基本工资: $base"),
                ),
                PieChartEntry(
                    value = perf,
                    label = AnnotatedString("绩效工资: $perf"),
                ),
                PieChartEntry(
                    value = welfare,
                    label = AnnotatedString("福利: $welfare"),
                ),
                PieChartEntry(
                    value = special,
                    label = AnnotatedString("特殊奖金: $special"),
                ),
                PieChartEntry(
                    value = overtime,
                    label = AnnotatedString("加班费: $overtime"),
                ),
                PieChartEntry(
                    value = subsidy,
                    label = AnnotatedString("补贴: $subsidy"),
                ),
                PieChartEntry(
                    value = debit,
                    label = AnnotatedString("扣款: $debit"),
                )
            ),
            colors = listOf(
                Color.Green,
                Color.Blue,
                Color.Cyan,
                Color.Magenta,
                Color.Yellow,
                Color.Gray,
                Color.Red,
            ),
            animate = true
        )
    }
}