package etude.de.lapin.baby.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import etude.de.lapin.baby.architecture.ui.theme.BabyArchitectureTheme
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.domain.action.model.Category

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val categoryViewModel = hiltViewModel<CategoryViewModel>()
            //val exampleEntities: List<ExampleEntity> by viewModel.exampleEntities.collectAsState(initial = emptyList())

            BabyArchitectureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        insertCategory(categoryViewModel = categoryViewModel)
                        categoryList(categoryViewModel = categoryViewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun categoryList(categoryViewModel: CategoryViewModel) {
        val categories: List<Category> by categoryViewModel.categoryFlow.collectAsState(initial = emptyList())
        LazyColumn {
            items(categories) {
                Text(it.name)
            }
        }
    }

    @Composable
    fun insertCategory(categoryViewModel: CategoryViewModel) {
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
                Text(stringResource(id = R.string.category_insert_done))
            }
        }
    }
}
