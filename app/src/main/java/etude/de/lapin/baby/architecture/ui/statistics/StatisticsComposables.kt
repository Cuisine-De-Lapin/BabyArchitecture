package etude.de.lapin.baby.architecture.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import etude.de.lapin.baby.architecture.R
import etude.de.lapin.baby.architecture.toLocalDateTime
import etude.de.lapin.baby.architecture.toTimestamp
import etude.de.lapin.baby.architecture.ui.DialogAddCancel
import etude.de.lapin.baby.architecture.ui.DialogColumnTitle
import etude.de.lapin.baby.architecture.ui.ShowDatePicker
import etude.de.lapin.baby.architecture.ui.ShowDialog
import etude.de.lapin.baby.architecture.ui.ShowTimePicker
import etude.de.lapin.baby.architecture.viewmodel.ActionViewModel
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.architecture.viewmodel.StatisticsViewModel
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.model.Statistics
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

@Composable
fun MenuStatistics(
    statisticsViewModel: StatisticsViewModel,
    currentDate: MutableState<LocalDateTime>,
    defaultDate: LocalDateTime
) {
    var isInsertDialogShowing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row {
                Button(onClick = {
                    currentDate.value = currentDate.value.toLocalDate().minusDays(1).atStartOfDay()
                }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.action_prev_day))
                }
                ShowDatePicker(date = currentDate.value, onValueChanged = {
                    currentDate.value = it
                }, modifier = Modifier.weight(2f))
                Button(onClick = {
                    currentDate.value = currentDate.value.toLocalDate().plusDays(1).atStartOfDay()
                }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.action_next_day))
                }
                Button(onClick = {
                    currentDate.value = defaultDate.toLocalDate().atStartOfDay()
                }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.action_today))
                }
            }

            GetDailyStatisticsList(statisticsViewModel = statisticsViewModel, currentDate = currentDate.value)
        }
    }
}

@Composable
fun GetDailyStatisticsList(statisticsViewModel: StatisticsViewModel, currentDate: LocalDateTime) {
    val statistics: List<Statistics> by statisticsViewModel.getStatistics(currentDate)
        .collectAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.default_padding))) {
        items(statistics) {
            if (it.totalCount == 0) {
                return@items
            }
            Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.item_padding))) {
                Text(text = it.categoryName, fontSize = 20.sp)
                Row {
                   StatisticsItem(title = stringResource(id = R.string.statistics_total_count), content = it.totalCount.toString())
                    StatisticsItem(title = stringResource(id = R.string.statistics_avg_internal_time), content = getAvgIntervalTimeText(it.avgInternalTime))
                }
                Row {
                    if (it.totalVolume != null) {
                        StatisticsItem(title = stringResource(id = R.string.statistics_total_volume), content = it.totalVolume.toString())
                    }
                    if (it.avgVolume != null) {
                        StatisticsItem(title = stringResource(id = R.string.statistics_avg_volume), content = it.avgVolume.toString())
                    }
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.divider_height))
                    .padding(top = dimensionResource(id = R.dimen.item_padding))
                    .background(colorResource(id = R.color.grey)))
            }
        }
    }
}

@Composable
private fun getAvgIntervalTimeText(avgTime: Float): String {
    val second = 1000L
    val minute = 60L
    val hour = 60L
    val minutes = avgTime.roundToLong().div(second).div(minute)
    
    return stringResource(id = R.string.hour_min_format, minutes.div(hour), minutes.mod(hour))
}

@Composable
fun RowScope.StatisticsItem(title: String, content: String) {
    Row(modifier = Modifier.weight(1f)) {
        Text(text = title, textAlign = TextAlign.Start, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f))
        Text(text = content, textAlign = TextAlign.Start, modifier = Modifier.weight(2f))
    }
}