package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
import org.mockito.kotlin.whenever

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class MainViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: IWordRepository

    lateinit var mainViewModel: MainViewModel

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
        mainViewModel = MainViewModel(repository)
        mainViewModel.wordPackageListLiveData.observeForever(wordPackageObserver)
        mainViewModel.loadPackageStateLiveData.observeForever(loadDataObserver)
        mainViewModel.insertPackageStateLiveData.observeForever(insertDataObserver)
    }

    @Test
    fun testLoadDataFromRepo() {
        val data = createTestData()
        `when`(repository.getAllPackages()).thenReturn(flowOf(data))
        mainViewModel.loadWordPackagesFromDatabase()
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
        mainViewModel.loadWordPackagesFromDatabase()
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
        mainViewModel.loadWordPackagesFromDatabase()
        mainViewModel.onSearchEditTextChanged("fi")
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
        mainViewModel.loadWordPackagesFromDatabase()
        mainViewModel.onSearchEditTextChanged("123")
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

    @Test
    fun testInsertDataSuccess() = runBlocking {
        val wordPackage = createTestData().first()

        `when`(repository.addPackage(wordPackage)).thenReturn(true)
        mainViewModel.insertWordPackage(wordPackage)
        val inOrder = Mockito.inOrder(
            insertDataObserver,
            wordPackageObserver,
            loadDataObserver
        )
        inOrder.verify(insertDataObserver).onChanged(eq(ViewState.LOADING))

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