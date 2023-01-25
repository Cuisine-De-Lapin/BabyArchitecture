package etude.de.lapin.baby.architecture.ui.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import etude.de.lapin.baby.architecture.R
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.domain.action.model.Category

@Composable
fun MenuCategory(categoryViewModel: CategoryViewModel) {
    Column {
        InsertCategory(categoryViewModel = categoryViewModel)
        CategoryList(categoryViewModel = categoryViewModel)
    }
}

@Composable
fun CategoryList(categoryViewModel: CategoryViewModel) {
    val categories: List<Category> by categoryViewModel.categoryFlow.collectAsState(initial = emptyList())
    LazyColumn {
        items(categories) {
            Text(it.name)
        }
    }
}

@Composable
fun InsertCategory(categoryViewModel: CategoryViewModel) {
    var categoryName by remember { mutableStateOf("") }
    var needVolume by remember { mutableStateOf(false) }

    Column {
        Row {
            Text(text = stringResource(id = R.string.category_insert_name))
            TextField(value = categoryName, onValueChange = {
                categoryName = it
            })
        }
        Row {
            Text(text = stringResource(id = R.string.category_insert_need_volume))
            Switch(checked = needVolume, onCheckedChange = {
                needVolume = it
            })
        }
        Button(onClick = {
            categoryViewModel.insertCategory(categoryName, needVolume)
            categoryName = ""
            needVolume = false
        }) {
            Text(stringResource(id = R.string.insert_done))
        }
    }
}