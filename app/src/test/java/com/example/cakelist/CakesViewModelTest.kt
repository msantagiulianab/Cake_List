package com.example.cakelist

import com.example.cakelist.models.Cake
import com.example.cakelist.sealed.DataState
import com.example.cakelist.ui.cakes.CakesViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CakesViewModelTest {
    @MockK
    lateinit var repository: CakesRepository

    private lateinit var viewModel: CakesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { repository.response } returns MutableStateFlow(DataState.Empty)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Getting the Cakes works`() = runBlocking {
        initializeViewModel()
        assertEquals(
            repository.response.value,
            viewModel.cakesState.value
        )
    }

    @Test
    fun `Detected query change`() = runBlocking {
        initializeViewModel()
        viewModel.onQueryChanged("Lemon")
        assertEquals(
            "Lemon",
            viewModel.query.value
        )
    }

    @Test
    fun `Filter results`() = runBlocking {
        initializeViewModel()

        val cake: Cake = mockk(relaxed = true) {
            every { title } returns "Banana"
        }
        val cake1: Cake = mockk(relaxed = true) {
            every { title } returns "Button"
        }
        val cake2: Cake = mockk(relaxed = true) {
            every { title } returns "Batman"
        }

        val list: List<Cake> = listOf(cake, cake1, cake2)

        viewModel.onQueryChanged("Ba")
        viewModel.countQuery(list)

        assertEquals(
            2,
            viewModel.results.value
        )
    }

    @Test
    fun `Clicking the sorting button`() = runBlocking {
        initializeViewModel()

        assertEquals(
            false,
            viewModel.selected.value
        )

        viewModel.changeSelection()

        assertEquals(
            true,
            viewModel.selected.value
        )
    }

    @Test
    fun `Change the position list state`() = runBlocking {
        initializeViewModel()

        viewModel.changePositionListState(5)

        assertEquals(
            5,
            viewModel.positionListState.value.firstVisibleItemIndex
        )
    }

    private fun initializeViewModel() {
        viewModel = CakesViewModel(
            repository = repository
        )
    }
}