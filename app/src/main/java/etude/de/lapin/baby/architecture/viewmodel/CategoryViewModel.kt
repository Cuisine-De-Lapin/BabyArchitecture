package etude.de.lapin.baby.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.usecase.category.CategoryDeleteUsecase
import etude.de.lapin.baby.domain.action.usecase.category.CategoryGetAllUsecase
import etude.de.lapin.baby.domain.action.usecase.category.CategoryInsertUsecase
import etude.de.lapin.baby.domain.action.usecase.category.CategoryUpdateUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryInsertUsecase: CategoryInsertUsecase,
    private val categoryUpdateUsecase: CategoryUpdateUsecase,
    private val categoryDeleteUsecase: CategoryDeleteUsecase,
    private val categoryGetAllUsecase: CategoryGetAllUsecase
): ViewModel() {

    val categoryFlow: Flow<List<Category>> = categoryGetAllUsecase.invoke()

    fun insertCategory(categoryName: String, needVolume: Boolean) {
        val category = Category(name = categoryName, needVolume =  needVolume)
        viewModelScope.launch(Dispatchers.IO) {
            categoryInsertUsecase.invoke(category)
        }
    }

    fun updateCategory(categoryId: Int, categoryName: String, needVolume: Boolean) {
        val category = Category(id = categoryId, name = categoryName, needVolume =  needVolume)
        viewModelScope.launch(Dispatchers.IO) {
            categoryUpdateUsecase.invoke(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDeleteUsecase.invoke(category)
        }
    }
}