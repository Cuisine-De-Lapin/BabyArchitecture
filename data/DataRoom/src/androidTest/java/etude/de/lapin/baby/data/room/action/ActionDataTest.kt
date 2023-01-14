package etude.de.lapin.baby.data.room.action

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import etude.de.lapin.baby.data.room.BabyDatabase
import etude.de.lapin.baby.data.room.dao.ActionDAO
import etude.de.lapin.baby.data.room.dao.CategoryDAO
import etude.de.lapin.baby.data.room.mapper.ActionMapper
import etude.de.lapin.baby.data.room.mapper.CategoryMapper
import etude.de.lapin.baby.data.room.repository.ActionRepositoryImpl
import etude.de.lapin.baby.data.room.repository.CategoryRepositoryImpl
import etude.de.lapin.baby.domain.action.model.Action
import etude.de.lapin.baby.domain.action.model.Category
import etude.de.lapin.baby.domain.action.repository.ActionRepository
import etude.de.lapin.baby.domain.action.repository.CategoryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ActionDataTest {
    private lateinit var actionDAO: ActionDAO
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var database: BabyDatabase
    private lateinit var actionRepository: ActionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var actionMapper: ActionMapper
    private lateinit var categoryMapper: CategoryMapper

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, BabyDatabase::class.java).build()
        actionDAO = database.actionDao()
        categoryDAO = database.categoryDao()
        actionMapper = ActionMapper()
        categoryMapper = CategoryMapper()
        actionRepository = ActionRepositoryImpl(actionDAO, actionMapper)
        categoryRepository = CategoryRepositoryImpl(categoryDAO, categoryMapper)
    }

    @Test
    fun insertAction() = runTest {
        val today = 91152000000L
        val category = Category(
            id = 0,
            name = "yainsidae",
            needVolume = true,
            visible = true
        )
        val action = Action(
            id = 0,
            categoryId = category.id,
            categoryName = category.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        launch {
            categoryRepository.insert(category)

            val getAllCategory = categoryRepository.getAllCategory()
            assert(getAllCategory?.size == 1)
            assert(getAllCategory?.get(0) == category)

            val getCategory = categoryRepository.getCategoryById(category.id)
            assert(getCategory == category)

            actionRepository.insert(action)

            val getById = actionRepository.getActionById(action.id)
            val getDailyList = actionRepository.getDailyAction(today)
            val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id)

            assert(action == getById)
            assert(getDailyList?.size == 1)
            assert(action == getDailyList?.get(0))
            assert(getCategoryList?.size == 1)
            assert(action == getCategoryList?.get(0))
        }

    }

    // Test Plan
    // 없는 카테고리를 지정한 액션을 노출하지 않는다.
    // 카테고리 삭제 후 액션이 보이지 않는 것 및 삭제된거 확인하기
    // 카테고리 감춤 후 액션이 보이지 않는 것 확인하기
    // 날짜가 다르면 액션을 노출하지 않는다
    // 카테고리 이름을 수정하면 액션 조회시에도 반영이 된다
    //

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

}