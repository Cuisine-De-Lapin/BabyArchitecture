package etude.de.lapin.baby.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import etude.de.lapin.baby.architecture.ui.action.MenuAction
import etude.de.lapin.baby.architecture.ui.category.MenuCategory
import etude.de.lapin.baby.architecture.ui.statistics.MenuStatistics
import etude.de.lapin.baby.architecture.ui.theme.BabyArchitectureTheme
import etude.de.lapin.baby.architecture.viewmodel.ActionViewModel
import etude.de.lapin.baby.architecture.viewmodel.CategoryViewModel
import etude.de.lapin.baby.architecture.viewmodel.StatisticsViewModel
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val categoryViewModel = hiltViewModel<CategoryViewModel>()
            val actionViewModel = hiltViewModel<ActionViewModel>()
            val statisticsViewModel = hiltViewModel<StatisticsViewModel>()
            var currentMenu by remember { mutableStateOf(BottomMenu.ACTIONS) }
            val defaultDate = LocalDateTime.now()
            val currentDate: MutableState<LocalDateTime> = remember { mutableStateOf(defaultDate.toLocalDate().atStartOfDay()) }

            //val exampleEntities: List<ExampleEntity> by viewModel.exampleEntities.collectAsState(initial = emptyList())

            BabyArchitectureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(modifier = Modifier.weight(1f, true)) {
                            when (currentMenu) {
                                BottomMenu.ACTIONS -> {
                                    Column {
                                        MenuAction(
                                            actionViewModel = actionViewModel,
                                            categoryViewModel = categoryViewModel,
                                            currentDate = currentDate,
                                            defaultDate = defaultDate
                                        )
                                    }

                                }
                                BottomMenu.CATEGORY -> {
                                    MenuCategory(categoryViewModel = categoryViewModel)
                                }
                                BottomMenu.STATISTICS -> {
                                    MenuStatistics(
                                        statisticsViewModel = statisticsViewModel,
                                        currentDate = currentDate,
                                        defaultDate = defaultDate
                                    )
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
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomMenu.values().forEach {
                Button(onClick = {
                    onMenuChanged(it)
                }, modifier = Modifier.weight(1f, true)) {
                    Text(
                        text = it.name,
                        color = colorResource(id = if (it == currentMenu) R.color.white else R.color.purple_200),
                    )
                }
            }
        }
    }
}

enum class BottomMenu {
    ACTIONS, CATEGORY, STATISTICS
}
