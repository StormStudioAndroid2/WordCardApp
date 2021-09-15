package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.PackageViewModel
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
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq

@RunWith(JUnit4::class)
class PackageViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: IWordRepository

    lateinit var packageViewModel: PackageViewModel

    @Mock
    private lateinit var wordPackageObserver: Observer<WordPackage>

    @Mock
    private lateinit var loadDataObserver: Observer<ViewState>

    @Mock
    private lateinit var insertLiveDataObserver: Observer<ViewState>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        packageViewModel = PackageViewModel(repository)
        packageViewModel.wordPackageLiveData.observeForever(wordPackageObserver)
        packageViewModel.loadViewStateLiveData.observeForever(loadDataObserver)
        packageViewModel.insertViewStateLiveData.observeForever(insertLiveDataObserver)
    }

    @Test
    fun testLoadWordPackageFromRepo() {
        val data = createTestData()
        Mockito.`when`(repository.getPackageWithWordsById(eq(2))).thenReturn(flowOf(data))
        packageViewModel.loadPackage(2)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
            loadDataObserver,
            insertLiveDataObserver
        )
        inOrder.verify(loadDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADING))
        inOrder.verify(wordPackageObserver).onChanged(ArgumentMatchers.eq(data))
        inOrder.verify(loadDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADED))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testUncorrectedId() {
        val data = createTestData()
        Mockito.`when`(repository.getPackageWithWordsById(eq(2))).thenReturn(flowOf(data))
        packageViewModel.loadPackage(-1)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
            loadDataObserver,
            insertLiveDataObserver
        )
        inOrder.verify(loadDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADING))
        inOrder.verify(loadDataObserver).onChanged(ArgumentMatchers.eq(ViewState.ERROR))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testErrorFromRepo() {
        Mockito.`when`(repository.getPackageWithWordsById(eq(2))).thenThrow(RuntimeException())
        packageViewModel.loadPackage(2)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
            loadDataObserver,
            insertLiveDataObserver
        )
        inOrder.verify(loadDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADING))
        inOrder.verify(loadDataObserver).onChanged(ArgumentMatchers.eq(ViewState.ERROR))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testErrorInsert() = runBlocking {
        val data = createTestData()
        Mockito.`when`(repository.getPackageWithWordsById(anyLong())).thenReturn(flowOf(data))
        Mockito.`when`(repository.addWordPair(anyString(), anyString(), anyLong()))
            .thenReturn(false)
        packageViewModel.loadPackage(0)
        packageViewModel.addNewCardToPackage("first", "second")
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
            loadDataObserver,
            insertLiveDataObserver
        )
        inOrder.verify(insertLiveDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADING))
        inOrder.verify(insertLiveDataObserver).onChanged(ArgumentMatchers.eq(ViewState.ERROR))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testSuccessInsert() = runBlocking {
        val data = createTestData()
        Mockito.`when`(repository.getPackageWithWordsById(anyLong())).thenReturn(flowOf(data))
        Mockito.`when`(repository.addWordPair(anyString(), anyString(), anyLong()))
            .thenReturn(true)
        packageViewModel.loadPackage(0)
        packageViewModel.addNewCardToPackage("first", "second")
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
            loadDataObserver,
            insertLiveDataObserver
        )
        inOrder.verify(insertLiveDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADING))
        inOrder.verify(insertLiveDataObserver).onChanged(ArgumentMatchers.eq(ViewState.LOADED))
        inOrder.verifyNoMoreInteractions()
    }

    private fun createTestData(): WordPackage {
        return WordPackage(0, "first", "English", "Russian", listOf())
    }
}