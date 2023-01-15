package etude.de.lapin.baby.domain.action.usecase.category

import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import javax.inject.Inject

class CategoryGetByIdUsecase @Inject constructor(private val categoryRepository: CategoryRepository) {
    operator fun invoke(id: Int) = categoryRepository.getCategoryById(id)
}