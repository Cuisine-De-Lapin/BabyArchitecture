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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
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
            context, BabyDatabase::class.java
        ).build()
        actionDAO = database.actionDao()
        categoryDAO = database.categoryDao()
        actionMapper = ActionMapper()
        categoryMapper = CategoryMapper()
        actionRepository = ActionRepositoryImpl(actionDAO, actionMapper)
        categoryRepository = CategoryRepositoryImpl(categoryDAO, categoryMapper)
    }

    @Test
    fun insertAction() = runTest {
        val today = 91152000000L // 1972-11-21
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

            val getAllCategory = categoryRepository.getAllCategory().first()
            assert(getAllCategory.size == 1)
            assert(getAllCategory.get(0) == category)

            val getCategory = categoryRepository.getCategoryById(category.id).first()
            assert(getCategory == category)

            actionRepository.insert(action)

            val getById = actionRepository.getActionById(action.id).first()
            val getDailyList = actionRepository.getDailyAction(today).first()
            val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id).first()

            assert(action == getById)
            assert(getDailyList.size == 1)
            assert(action == getDailyList[0])
            assert(getCategoryList.size == 1)
            assert(action == getCategoryList[0])
        }

    }

    @Test
    fun noCategoryNoAction() = runTest {
        val today = 91152000000L // 1972-11-21

        // insert category isn't inserted.
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
            actionRepository.insert(action)

            val getById = actionRepository.getActionById(action.id).first()
            val getDailyList = actionRepository.getDailyAction(today).first()
            val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id).first()

            assert(getById == null)
            assert(getDailyList.isEmpty())
            assert(getCategoryList.isEmpty())
        }
    }

    // 등록되지 않은 카테고리로 등록된 액션이 있는 경우 보이지 않도록
    @Test
    fun wrongCategoryNoAction() = runTest {
        val today = 91152000000L // 1972-11-21

        // 등록된 카테고리
        val category = Category(
            id = 0,
            name = "yainsidae",
            needVolume = true,
            visible = true
        )

        // 등록되지 않은 카테고리
        val wrongCategory = Category(
            id = 1,
            name = "simyoung",
            needVolume = true,
            visible = true
        )

        val action = Action(
            id = 0,
            categoryId = wrongCategory.id,
            categoryName = wrongCategory.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        launch {
            categoryRepository.insert(category)

            val getAllCategory = categoryRepository.getAllCategory().first()
            assert(getAllCategory.size == 1)
            assert(getAllCategory[0] == category)

            val getCategory = categoryRepository.getCategoryById(category.id).first()
            assert(getCategory == category)

            actionRepository.insert(action)

            val getById = actionRepository.getActionById(action.id).first()
            val getDailyList = actionRepository.getDailyAction(today).first()
            val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id).first()

            assert(getById == null)
            assert(getDailyList.isEmpty())
            assert(getCategoryList.isEmpty())
        }
    }

    // 감춰진 카테고리(카테고리만 삭제)의 경우 카테고리 리스트에는 나오지 말아야 하지만,
    // 개별로 조회시에 카테고리 노출하고, 액션들도 노출시켜야 한다.(액션 유지하기)
    @Test
    fun hiddenCategoryAction() = runTest {
        val today = 91152000000L // 1972-11-21

        // 등록된 카테고리 (카테고리 리스트에는 감춰진 상태)
        val category = Category(
            id = 0,
            name = "yainsidae",
            needVolume = true,
            visible = false
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

            val getAllCategory = categoryRepository.getAllCategory().first()
            assert(getAllCategory?.size == 0)

            val getCategory = categoryRepository.getCategoryById(category.id).first()
            assert(getCategory == category)

            actionRepository.insert(action)

            val getById = actionRepository.getActionById(action.id).first()
            val getDailyList = actionRepository.getDailyAction(today).first()
            val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id).first()

            assert(action == getById)
            assert(getDailyList.size == 1)
            assert(action == getDailyList[0])
            assert(getCategoryList.size == 1)
            assert(action == getCategoryList[0])
        }
    }

    @Test
    fun categoryDeleteTest() = runTest {
        val today = 91152000000L // 1972-11-21

        // 등록된 카테고리 (카테고리 리스트에는 감춰진 상태)
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

        categoryRepository.insert(category)
        actionRepository.insert(action)

        val getById = actionRepository.getActionById(action.id).first()
        val getDailyList = actionRepository.getDailyAction(today).first()
        val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id).first()

        assert(action == getById)
        assert(getDailyList.size == 1)
        assert(action == getDailyList[0])
        assert(getCategoryList.size == 1)
        assert(action == getCategoryList[0])

        categoryRepository.delete(category)

        val getAllCategoryDeleted = categoryRepository.getAllCategory().first()
        assert(getAllCategoryDeleted.isEmpty())

        val getCategoryDeleted = categoryRepository.getCategoryById(category.id).first()
        assert(getCategoryDeleted == null)

        val getByIdDeleted = actionRepository.getActionById(action.id).first()
        val getDailyListDeleted = actionRepository.getDailyAction(today).first()
        val getCategoryListDeleted = actionRepository.getDailyActionByCategory(today, category.id).first()

        assert(getByIdDeleted == null)
        assert(getDailyListDeleted.isEmpty())
        assert(getCategoryListDeleted.isEmpty())
    }

    @Test
    fun insertActionByDate() = runTest {
        val today = 91119600000L // 1972-11-21
        val yesterday = 91033200000L // 1972-11-20
        val tomorrow = 91206000000L // 1972-11-22

        val endOfYesterday = 91119599999L
        val endOfToday = 91205999999L

        val category = Category(
            id = 0,
            name = "yainsidae",
            needVolume = true,
            visible = true
        )

        val todayAction = Action(
            id = 0,
            categoryId = category.id,
            categoryName = category.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        val endOfTodayAction = Action(
            id = 1,
            categoryId = category.id,
            categoryName = category.name,
            volume = 100,
            timestamp = endOfToday,
            memo = "orange-bottle"
        )

        val endOfYesterdayAction = Action(
            id = 2,
            categoryId = category.id,
            categoryName = category.name,
            volume = 100,
            timestamp = endOfYesterday,
            memo = "orange-bottle"
        )

        launch {

            categoryRepository.insert(category)

            actionRepository.insert(todayAction)
            actionRepository.insert(endOfTodayAction)
            actionRepository.insert(endOfYesterdayAction)

            val getDailyList = actionRepository.getDailyAction(today).first()
            val getCategoryList = actionRepository.getDailyActionByCategory(today, category.id).first()

            assert(getDailyList.size == 2)
            assert(getCategoryList.size == 2)

            val getYesterdayList = actionRepository.getDailyAction(yesterday).first()
            val getYesterdayListCategory = actionRepository.getDailyActionByCategory(yesterday, category.id).first()
            assert(getYesterdayList.size == 1)
            assert(getYesterdayListCategory.size == 1)

            val getTomorrowList = actionRepository.getDailyAction(tomorrow).first()
            val getTomorrowListCategory = actionRepository.getDailyActionByCategory(tomorrow, category.id).first()
            assert(getTomorrowList.isEmpty())
            assert(getTomorrowListCategory.isEmpty())
        }

    }

    @Test
    fun changeCategory() = runTest {
        val today = 91152000000L // 1972-11-21
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
        val newCategory = Category(
            id = 0,
            name = "simyoungmul",
            needVolume = true,
            visible = true
        )

        launch {
            categoryRepository.insert(category)
            actionRepository.insert(action)
            val getById = actionRepository.getActionById(action.id).first()
            assert(getById?.categoryName == category.name)

            categoryRepository.insert(newCategory)
            val getByIdCategoryChanged = actionRepository.getActionById(action.id).first()
            assert(getByIdCategoryChanged?.categoryName == newCategory.name)

        }
    }

    @Test
    fun getByCategory() = runTest {
        val today = 91152000000L // 1972-11-21
        val category1 = Category(
            id = 0,
            name = "yainsidae",
            needVolume = true,
            visible = true
        )
        val category2 = Category(
            id = 1,
            name = "simyoungmool",
            needVolume = true,
            visible = true
        )
        val category3 = Category(
            id = 2,
            name = "pok8",
            needVolume = true,
            visible = true
        )

        val action1_1 = Action(
            id = 0,
            categoryId = category1.id,
            categoryName = category1.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        val action1_2 = Action(
            id = 1,
            categoryId = category1.id,
            categoryName = category1.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        val action1_3 = Action(
            id = 2,
            categoryId = category1.id,
            categoryName = category1.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        val action2_1 = Action(
            id = 3,
            categoryId = category2.id,
            categoryName = category2.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        val action3_1 = Action(
            id = 4,
            categoryId = category3.id,
            categoryName = category3.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )

        val action3_2 = Action(
            id = 5,
            categoryId = category3.id,
            categoryName = category3.name,
            volume = 100,
            timestamp = today,
            memo = "orange-bottle"
        )


        launch {
            categoryRepository.insert(category1)
            categoryRepository.insert(category2)
            categoryRepository.insert(category3)

            actionRepository.insert(action1_1)
            actionRepository.insert(action1_2)
            actionRepository.insert(action1_3)
            actionRepository.insert(action2_1)
            actionRepository.insert(action3_1)
            actionRepository.insert(action3_2)

            val getDailyList = actionRepository.getDailyAction(today).first()
            val getCategory1List = actionRepository.getDailyActionByCategory(today, category1.id).first()
            val getCategory2List = actionRepository.getDailyActionByCategory(today, category2.id).first()
            val getCategory3List = actionRepository.getDailyActionByCategory(today, category3.id).first()

            assert(getDailyList?.size == 6)
            assert(getCategory1List?.size == 3)
            assert(getCategory2List?.size == 1)
            assert(getCategory3List?.size == 2)
        }

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }


}