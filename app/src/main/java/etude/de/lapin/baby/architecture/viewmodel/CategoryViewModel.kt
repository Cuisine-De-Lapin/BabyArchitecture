package etude.de.lapin.baby.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.usecase.category.CategoryGetAllUsecase
import etude.de.lapin.baby.domain.action.usecase.category.CategoryInsertUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryInsertUsecase: CategoryInsertUsecase,
    private val categoryGetAllUsecase: CategoryGetAllUsecase
): ViewModel() {

    val categoryFlow: Flow<List<Category>> = categoryGetAllUsecase.invoke()

    fun insertCategory(categoryName: String, needVolume: Boolean) {
        val category = Category(name = categoryName, needVolume =  needVolume)
        viewModelScope.launch(Dispatchers.IO) {
            categoryInsertUsecase.invoke(category)
        }
    }
}