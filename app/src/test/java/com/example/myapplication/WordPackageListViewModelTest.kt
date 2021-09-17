package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.WordPackageListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class WordPackageListViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: IWordRepository

    lateinit var wordPackageListViewModel: WordPackageListViewModel

    @Mock
    private lateinit var wordPackageObserver: Observer<List<WordPackage>>

    @Mock
    private lateinit var loadDataObserver: Observer<ViewState>

    @Mock
    private lateinit var insertDataObserver: Observer<ViewState>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        wordPackageListViewModel = WordPackageListViewModel(repository)
        wordPackageListViewModel.wordPackageListLiveData.observeForever(wordPackageObserver)
        wordPackageListViewModel.loadPackageStateLiveData.observeForever(loadDataObserver)
    }

    @Test
    fun testLoadDataFromRepo() {
        val data = createTestData()
        `when`(repository.getAllPackages()).thenReturn(flowOf(data))
        wordPackageListViewModel.loadWordPackagesFromDatabase()
        val inOrder = Mockito.inOrder(
            insertDataObserver,
            wordPackageObserver,
            loadDataObserver
        )
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADING))
        inOrder.verify(wordPackageObserver).onChanged(ArgumentMatchers.eq(data))
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADED))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testLoadDataFromRepoError() {
        `when`(repository.getAllPackages()).thenThrow(RuntimeException())
        wordPackageListViewModel.loadWordPackagesFromDatabase()
        val inOrder = Mockito.inOrder(
            loadDataObserver,
            wordPackageObserver,
        )
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADING))
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.ERROR))

        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testFilterData() {
        val data = createTestData()
        `when`(repository.getAllPackages()).thenReturn(flowOf(data))
        wordPackageListViewModel.loadWordPackagesFromDatabase()
        wordPackageListViewModel.onSearchEditTextChanged("fi")
        val inOrder = Mockito.inOrder(
            insertDataObserver,
            wordPackageObserver,
            loadDataObserver
        )
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADING))
        inOrder.verify(wordPackageObserver).onChanged(ArgumentMatchers.eq(data))
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADED))
        inOrder.verify(wordPackageObserver).onChanged(ArgumentMatchers.eq(listOf(data.first())))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testFilterDataNotFound() {
        val data = createTestData()
        `when`(repository.getAllPackages()).thenReturn(flowOf(data))
        wordPackageListViewModel.loadWordPackagesFromDatabase()
        wordPackageListViewModel.onSearchEditTextChanged("123")
        val inOrder = Mockito.inOrder(
            insertDataObserver,
            wordPackageObserver,
            loadDataObserver
        )
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADING))
        inOrder.verify(wordPackageObserver).onChanged(ArgumentMatchers.eq(data))
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADED))
        inOrder.verify(wordPackageObserver).onChanged(ArgumentMatchers.eq(listOf()))
        inOrder.verifyNoMoreInteractions()
    }

    private fun createTestData(): List<WordPackage> {
        return listOf(
            WordPackage(0, "first", "English", "Russian", listOf()),
            WordPackage(1, "second", "English", "Russian", listOf()),
            WordPackage(2, "third", "English", "Russian", listOf()),
        )
    }
}