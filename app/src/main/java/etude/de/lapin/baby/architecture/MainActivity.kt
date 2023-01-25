package etude.de.lapin.baby.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import etude.de.lapin.baby.architecture.ui.action.GetDailyActionList
import etude.de.lapin.baby.architecture.ui.action.InsertAction
import etude.de.lapin.baby.architecture.ui.action.MenuAction
import etude.de.lapin.baby.architecture.ui.category.MenuCategory
import etude.de.lapin.baby.architecture.ui.theme.BabyArchitectureTheme
import etude.de.lapin.baby.architecture.viewmodel.ActionViewModel
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val categoryViewModel = hiltViewModel<CategoryViewModel>()
            val actionViewModel = hiltViewModel<ActionViewModel>()
            var currentMenu by remember { mutableStateOf(BottomMenu.ACTIONS) }
            val defaultDate = LocalDateTime.now()

            //val exampleEntities: List<ExampleEntity> by viewModel.exampleEntities.collectAsState(initial = emptyList())

            BabyArchitectureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Box(modifier = Modifier.weight(1f, true)) {
                            when (currentMenu) {
                                BottomMenu.ACTIONS -> {
                                    Column {
                                        MenuAction(
                                            actionViewModel = actionViewModel,
                                            categoryViewModel = categoryViewModel,
                                            defaultDate = defaultDate
                                        )
                                    }

                                }
                                BottomMenu.CATEGORY -> {
                                    MenuCategory(categoryViewModel = categoryViewModel)
                                }
                            }
                        }
                        bottomMenu(currentMenu = currentMenu, onMenuChanged = { currentMenu = it })
                    }
                }
            }
        }
    }

    @Composable
    fun bottomMenu(currentMenu: BottomMenu, onMenuChanged: (BottomMenu) -> Unit) {
        LazyRow {
            items(BottomMenu.values()) {
                Text(
                    text = it.name,
                    color = colorResource(id = if (it == currentMenu) R.color.purple_200 else R.color.black),
                    modifier = Modifier.clickable {
                        onMenuChanged(it)
                    })
            }
        }
    }
}

enum class BottomMenu {
    ACTIONS, CATEGORY
}
