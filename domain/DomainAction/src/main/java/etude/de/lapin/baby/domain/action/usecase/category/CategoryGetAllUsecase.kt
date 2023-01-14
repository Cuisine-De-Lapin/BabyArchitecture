package etude.de.lapin.baby.domain.action.usecase.category

import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import javax.inject.Inject

class CategoryInsertUsecase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(category: Category) {
        categoryRepository.insert(category)
    }
}