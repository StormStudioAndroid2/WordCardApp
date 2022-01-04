package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.WordPairListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import java.lang.RuntimeException


@RunWith(JUnit4::class)
class WordPairListViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var wordPairListViewModel: WordPairListViewModel

    @Mock
    lateinit var repository: IWordRepository

    @Mock
    private lateinit var wordPackageObserver: Observer<WordPackage>

    @Mock
    private lateinit var loadDataObserver: Observer<ViewState>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        wordPairListViewModel = WordPairListViewModel(repository)
        wordPairListViewModel.wordPackageFlow.observeForever(wordPackageObserver)
        wordPairListViewModel.loadViewStateLiveData.observeForever(loadDataObserver)
    }

    @Test
    fun testOnSuccessLoadData() {
        val data = getTestData()
        Mockito.`when`(repository.getPackageWithWordsById(anyLong())).thenReturn(flowOf(data))
        wordPairListViewModel.updateList(0)
        val inOrder = Mockito.inOrder(
            loadDataObserver,
            wordPackageObserver
        )
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADING))
        inOrder.verify(wordPackageObserver).onChanged(eq(data))
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADED))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testOnErrorLoadData() {
        getTestData()
        Mockito.`when`(repository.getPackageWithWordsById(anyLong())).thenThrow(RuntimeException())
        wordPairListViewModel.updateList(0)
        val inOrder = Mockito.inOrder(
            loadDataObserver,
            wordPackageObserver
        )
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.LOADING))
        inOrder.verify(loadDataObserver).onChanged(eq(ViewState.ERROR))
        inOrder.verifyNoMoreInteractions()
    }

    private fun getTestData(): WordPackage {
        return WordPackage(0, "first", "English", "Russian", listOf())
    }
}