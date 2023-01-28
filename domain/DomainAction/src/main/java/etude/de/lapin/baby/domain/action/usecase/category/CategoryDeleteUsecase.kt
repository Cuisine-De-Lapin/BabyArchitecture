package etude.de.lapin.baby.domain.action.usecase.category

import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import javax.inject.Inject

class CategoryDeleteUsecase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val actionRepository: ActionRepository
    ) {
    suspend operator fun invoke(category: Category) {
        actionRepository.deleteByCategoryId(category.id)
        categoryRepository.delete(category)
    }
}