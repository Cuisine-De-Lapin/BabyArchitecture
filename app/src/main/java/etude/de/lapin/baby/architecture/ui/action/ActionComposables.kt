package etude.de.lapin.baby.architecture.ui.action

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import etude.de.lapin.baby.architecture.ui.ShowDatePicker
import etude.de.lapin.baby.architecture.ui.ShowDialog
import etude.de.lapin.baby.architecture.ui.ShowTimePicker
import etude.de.lapin.baby.architecture.toLocalDateTime
import etude.de.lapin.baby.architecture.toTimestamp
import etude.de.lapin.baby.architecture.ui.DialogAddCancel
import etude.de.lapin.baby.architecture.ui.DialogColumnTitle
import etude.de.lapin.baby.architecture.ui.DialogModifyDeleteCancel
import etude.de.lapin.baby.architecture.viewmodel.ActionViewModel
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MenuAction(
    actionViewModel: ActionViewModel,
    categoryViewModel: CategoryViewModel,
    currentDate: MutableState<LocalDateTime>,
    defaultDate: LocalDateTime
) {
    var isInsertDialogShowing by remember { mutableStateOf(false) }
    val currentAction: MutableState<Action?> = remember {
        mutableStateOf(null)
    }
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

            GetDailyActionList(actionViewModel = actionViewModel, currentDate = currentDate.value) {
                currentAction.value = it
                isInsertDialogShowing = true
            }
        }

        FloatingActionButton(
            onClick = {
                isInsertDialogShowing = true
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(id = R.dimen.default_padding))
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    if (isInsertDialogShowing) {
        Dialog(onDismissRequest = {
            isInsertDialogShowing = false
            currentAction.value = null
        }) {
            Surface(
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.dialog_insert_width),
                        dimensionResource(id = R.dimen.dialog_insert_action_height)
                    )
            ) {
                InsertAction(
                    actionViewModel = actionViewModel,
                    categoryViewModel = categoryViewModel,
                    currentDate = currentDate.value,
                    action = currentAction.value
                ) {
                    isInsertDialogShowing = false
                    currentAction.value = null
                }
            }

        }
    }


}

@Composable
fun GetDailyActionList(actionViewModel: ActionViewModel, currentDate: LocalDateTime, onClickItem: (Action) -> Unit) {
    val categories: List<Action> by actionViewModel.getActions(currentDate)
        .collectAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.default_padding))) {
        items(categories) {
            Column(modifier = Modifier
                .padding(bottom = dimensionResource(id = R.dimen.item_padding))
                .clickable {
                    onClickItem.invoke(it)
                }) {
                val format: DateTimeFormatter =
                    DateTimeFormatter.ofPattern(stringResource(id = R.string.date_time_format))
                Text(text = format.format(it.timestamp.toLocalDateTime()))
                Row {
                    Text(text = it.categoryName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    it.volume?.let {
                        Text(" ($it)")
                    }
                }
                it.memo?.let {
                    Text(text = it, color = colorResource(id = R.color.grey))
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
fun InsertAction(
    actionViewModel: ActionViewModel,
    categoryViewModel: CategoryViewModel,
    currentDate: LocalDateTime,
    action: Action? = null,
    onInsertDismiss: () -> Unit,
) {
    var selectedDate by remember { mutableStateOf(currentDate) }
    var volume by remember { mutableStateOf(0f) }
    var categoryDropDownMenu by remember { mutableStateOf(false) }
    val memo: MutableState<String?> = remember { mutableStateOf(null) }
    val categories: List<Category> by categoryViewModel.categoryFlow.collectAsState(initial = emptyList())
    val category: MutableState<Category?> = remember { mutableStateOf(null) }
    var updateInitialized by remember { mutableStateOf(false) }

    val openWrongCategoryDialog: MutableState<Boolean> = remember { mutableStateOf(false) }

    if (categories.isEmpty()) {
        Text(text = stringResource(id = R.string.action_empty_category))
        return
    }

    if (action != null && updateInitialized.not()) {
        category.value = categories.firstOrNull { it.id == action.categoryId }
        action.volume?.let { volume = it }
        selectedDate = action.timestamp.toLocalDateTime()
        memo.value = action.memo
        updateInitialized = true
    }

    Column {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = R.string.show_insert_dialog),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                DialogColumnTitle(stringResource(id = R.string.action_category_name))
                Text(text = category.value?.name ?: "", fontSize = 16.sp, modifier = Modifier
                    .clickable {
                        categoryDropDownMenu = true
                    }
                    .width(dimensionResource(id = R.dimen.dialog_insert_column_content_width))
                    .background(
                        colorResource(id = R.color.grey)
                    ))
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
                    DialogColumnTitle(stringResource(id = R.string.action_volume))
                    TextField(
                        value = volume.toString(), onValueChange = {
                            volume = it.toFloat()
                        }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(dimensionResource(id = R.dimen.dialog_insert_column_content_width))
                    )
                }
            }

            Row {
                DialogColumnTitle(stringResource(id = R.string.action_timestamp))
                ShowDatePicker(date = selectedDate, onValueChanged = {
                    selectedDate = it
                })
                ShowTimePicker(date = selectedDate, onValueChanged = {
                    selectedDate = it
                })
            }
            Row {
                DialogColumnTitle(stringResource(id = R.string.action_memo))
                TextField(
                    value = memo.value ?: "", onValueChange = {
                        memo.value = it
                    },
                    modifier = Modifier.width(dimensionResource(id = R.dimen.dialog_insert_column_content_width))
                )
            }
        }

        if (action == null) {
            DialogAddCancel(onAddAction = {
                if (category.value == null) {
                    openWrongCategoryDialog.value = true
                    return@DialogAddCancel
                }
                openWrongCategoryDialog.value = false
                category.value?.let {
                    actionViewModel.insertAction(
                        it,
                        selectedDate.toTimestamp(),
                        if (memo.value.isNullOrEmpty()) null else memo.value,
                        if (it.needVolume) volume else null
                    )
                }

                onInsertDismiss.invoke()
            }, onCancelAction = {
                onInsertDismiss.invoke()
            })
        } else {
            DialogModifyDeleteCancel(onModifyAction = {
                if (category.value == null) {
                    openWrongCategoryDialog.value = true
                    return@DialogModifyDeleteCancel
                }
                openWrongCategoryDialog.value = false
                category.value?.let {
                    actionViewModel.updateAction(
                        action.id,
                        it,
                        selectedDate.toTimestamp(),
                        if (memo.value.isNullOrEmpty()) null else memo.value,
                        if (it.needVolume) volume else null
                    )
                }

                onInsertDismiss.invoke()
            }, onDeleteAction = {
                actionViewModel.deleteAction(action)
                onInsertDismiss.invoke()
            }, onCancelAction = {
                onInsertDismiss.invoke()
            })
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
