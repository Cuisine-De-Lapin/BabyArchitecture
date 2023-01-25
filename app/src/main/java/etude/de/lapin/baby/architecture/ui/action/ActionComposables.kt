package etude.de.lapin.baby.architecture.ui.action

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import etude.de.lapin.baby.architecture.HourText
import etude.de.lapin.baby.architecture.R
import etude.de.lapin.baby.architecture.ShowDatePicker
import etude.de.lapin.baby.architecture.ShowDialog
import etude.de.lapin.baby.architecture.ShowTimePicker
import etude.de.lapin.baby.architecture.toLocalDateTime
import etude.de.lapin.baby.architecture.toTimestamp
import etude.de.lapin.baby.architecture.viewmodel.ActionViewModel
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MenuAction(actionViewModel: ActionViewModel, categoryViewModel: CategoryViewModel, defaultDate: LocalDateTime) {
    var currentDate by remember { mutableStateOf(defaultDate.toLocalDate().atStartOfDay()) }


    Column {
        InsertAction(actionViewModel = actionViewModel, categoryViewModel = categoryViewModel, defaultDate = defaultDate)

        Row {
            Text(text = stringResource(id = R.string.action_prev_day), modifier = Modifier.clickable {
                currentDate = currentDate.toLocalDate().minusDays(1).atStartOfDay()
            })
            ShowDatePicker(date = currentDate) {
                currentDate = it
            }
            Text(text = stringResource(id = R.string.action_next_day), modifier = Modifier.clickable {
                currentDate = currentDate.toLocalDate().plusDays(1).atStartOfDay()
            })
        }

        GetDailyActionList(actionViewModel = actionViewModel, currentDate = currentDate)
    }
}

@Composable
fun GetDailyActionList(actionViewModel: ActionViewModel, currentDate: LocalDateTime) {
    val categories: List<Action> by actionViewModel.getActions(currentDate)
        .collectAsState(initial = emptyList())
    LazyColumn {
        items(categories) {
            Column {
                Text(it.categoryName)
                val format: DateTimeFormatter =
                    DateTimeFormatter.ofPattern(stringResource(id = R.string.date_time_format))
                if (it.volume != null) {
                    Text(it.volume.toString())
                }
                Text(text = format.format(it.timestamp.toLocalDateTime()))
            }

        }
    }
}

@Composable
fun InsertAction(
    actionViewModel: ActionViewModel,
    categoryViewModel: CategoryViewModel,
    defaultDate: LocalDateTime
) {
    var selectedDate by remember { mutableStateOf(defaultDate) }
    val category: MutableState<Category?> = remember { mutableStateOf(null) }
    var volume by remember { mutableStateOf(0f) }
    var categoryDropDownMenu by remember { mutableStateOf(false) }
    val categories: List<Category> by categoryViewModel.categoryFlow.collectAsState(initial = emptyList())

    val openWrongCategoryDialog: MutableState<Boolean> = remember { mutableStateOf(false) }

    if (categories.isEmpty()) {
        Text(text = stringResource(id = R.string.action_empty_category))
        return
    }

    Column {
        Row {
            Text(text = stringResource(id = R.string.action_category_name))
            Text(text = category.value?.name ?: "", modifier = Modifier.clickable {
                categoryDropDownMenu = true
            })
            DropdownMenu(
                expanded = categoryDropDownMenu,
                onDismissRequest = { categoryDropDownMenu = false }) {
                for (item in categories) {
                    DropdownMenuItem(onClick = {
                        category.value = item
                        categoryDropDownMenu = false
                    }) {
                        Text(text = item.name)
                    }
                }
            }
        }
        if (category.value?.needVolume == true) {
            Row {
                Text(text = stringResource(id = R.string.action_volume))
                TextField(value = volume.toString(), onValueChange = {
                    volume = it.toFloat()
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        }

        Row {
            ShowDatePicker(date = selectedDate) {
                selectedDate = it
            }
            ShowTimePicker(date = selectedDate) {
                selectedDate = it
            }
        }

        Button(onClick = {
            if (category.value == null) {
                openWrongCategoryDialog.value = true
                return@Button
            }
            openWrongCategoryDialog.value = false
            category.value?.let {
                actionViewModel.insertAction(
                    it,
                    selectedDate.toTimestamp(),
                    null,
                    if (it.needVolume) volume else null
                )
            }

        }) {
            Text(stringResource(id = R.string.insert_done))
        }
    }

    ShowDialog(
        openDialog = openWrongCategoryDialog,
        message = stringResource(id = R.string.action_wrong_category),
        closeText = stringResource(
            id = R.string.ok
        )
    )
}