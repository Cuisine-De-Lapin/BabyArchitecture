package etude.de.lapin.baby.architecture.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import etude.de.lapin.baby.architecture.R
import etude.de.lapin.baby.architecture.ui.DialogAddCancel
import etude.de.lapin.baby.architecture.ui.DialogColumnTitle
import etude.de.lapin.baby.architecture.ui.DialogModifyDeleteCancel
import etude.de.lapin.baby.architecture.ui.ShowDialog
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.domain.action.model.Category

@Composable
fun MenuCategory(categoryViewModel: CategoryViewModel) {
    var isInsertDialogShowing by remember { mutableStateOf(false) }
    val currentCategory: MutableState<Category?> = remember {
        mutableStateOf(null)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CategoryList(categoryViewModel = categoryViewModel) {
            currentCategory.value = it
            isInsertDialogShowing = true
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
            currentCategory.value = null
        }) {
            Surface(
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.dialog_insert_width),
                        dimensionResource(id = R.dimen.dialog_insert_height)
                    )
            ) {
                InsertCategory(
                    categoryViewModel = categoryViewModel,
                    category = currentCategory.value
                ) {
                    isInsertDialogShowing = false
                    currentCategory.value = null
                }
            }

        }
    }
}

@Composable
fun CategoryList(categoryViewModel: CategoryViewModel, onClickItem: (Category) -> Unit) {
    val categories: List<Category> by categoryViewModel.categoryFlow.collectAsState(initial = emptyList())
    LazyColumn(
        modifier = Modifier.padding(
            start = dimensionResource(id = R.dimen.default_padding), top = dimensionResource(
                id = R.dimen.default_padding
            )
        )
    ) {
        items(categories) {
            Text(
                text = it.name,
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.item_padding))
                    .clickable { onClickItem(it) }
            )
        }
    }
}

@Composable
fun InsertCategory(
    categoryViewModel: CategoryViewModel,
    category: Category? = null,
    onInsertDismiss: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var needVolume by remember { mutableStateOf(false) }
    var categoryInitialized by remember { mutableStateOf(false) }
    val deleteWarnDialogShow = remember { mutableStateOf(false) }

    if (category != null && categoryInitialized.not()) {
        categoryInitialized = true
        categoryName = category.name
        needVolume = category.needVolume
    }

    Column {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                DialogColumnTitle(stringResource(id = R.string.category_insert_name))
                TextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                    },
                    modifier = Modifier.width(dimensionResource(id = R.dimen.dialog_insert_column_content_width))
                )
            }
            Row {
                DialogColumnTitle(stringResource(id = R.string.category_insert_need_volume))
                Switch(checked = needVolume, onCheckedChange = {
                    needVolume = it
                })
            }
        }

        if (category != null) {
            DialogModifyDeleteCancel(onModifyAction = {
                categoryViewModel.updateCategory(category.id, categoryName, needVolume)
                onInsertDismiss.invoke()
            }, onDeleteAction = {
                deleteWarnDialogShow.value = true
            }, onCancelAction = { onInsertDismiss.invoke() })
        } else {
            DialogAddCancel(onAddAction = {
                categoryViewModel.insertCategory(categoryName, needVolume)
                onInsertDismiss.invoke()
            }, onCancelAction = { onInsertDismiss.invoke() })
        }
    }

    ShowDialog(
        openDialog = deleteWarnDialogShow,
        message = stringResource(id = R.string.category_delete_warning),
        okText = stringResource(id = R.string.delete_done),
        cancelText = stringResource(id = R.string.cancel),
        doOnOk = {
            category?.let {
                categoryViewModel.deleteCategory(category)
            }
            onInsertDismiss.invoke()
            deleteWarnDialogShow.value = false
        },
        doOnCancel = {
            onInsertDismiss.invoke()
            deleteWarnDialogShow.value = false
        }
    )
}