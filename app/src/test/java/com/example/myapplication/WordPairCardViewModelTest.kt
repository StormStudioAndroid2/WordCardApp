package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.domain.model.AnswerType
import com.example.myapplication.domain.model.WordPair
import com.example.myapplication.presentaion.viewmodel.WordPairCardViewModel
import com.yuyakaido.android.cardstackview.Direction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq

@RunWith(JUnit4::class)
class WordPairCardViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var wordPairCardViewModel: WordPairCardViewModel

    @Mock
    private lateinit var isEndObserver: Observer<Boolean>

    @Mock
    private lateinit var wordPairsObserver: Observer<List<WordPair>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        wordPairCardViewModel = WordPairCardViewModel()
        wordPairCardViewModel.isEndOfListLiveData.observeForever(isEndObserver)
        wordPairCardViewModel.wordPairListLiveData.observeForever(wordPairsObserver)
    }

    @Test
    fun testOnLastCardDisappeared() {
        val data = WordPair("first", "first", 0, 0, AnswerType.NOT_ANSWER)
        wordPairCardViewModel.setWordPairs(listOf(data))
        wordPairCardViewModel.onCardDisappeared(0)
        val inOrder = Mockito.inOrder(
            isEndObserver,
            wordPairsObserver
        )
        inOrder.verify(wordPairsObserver).onChanged(eq(listOf(data)))
        inOrder.verify(isEndObserver).onChanged(true)
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun testOnAnySwiped() {
        wordPairCardViewModel.onSwiped(null)
        assertEquals(0, wordPairCardViewModel.resultStatistic.rightAnswers)
        assertEquals(0, wordPairCardViewModel.resultStatistic.wrongAnswers)
        assertEquals(0, wordPairCardViewModel.resultStatistic.noAnswer)
    }

    @Test
    fun testOnLeftSwiped() {
        wordPairCardViewModel.onSwiped(Direction.Left)
        assertEquals(0, wordPairCardViewModel.resultStatistic.rightAnswers)
        assertEquals(1, wordPairCardViewModel.resultStatistic.wrongAnswers)
        assertEquals(0, wordPairCardViewModel.resultStatistic.noAnswer)
    }

    @Test
    fun testOnRightSwiped() {
        wordPairCardViewModel.onSwiped(Direction.Right)
        assertEquals(1, wordPairCardViewModel.resultStatistic.rightAnswers)
        assertEquals(0, wordPairCardViewModel.resultStatistic.wrongAnswers)
        assertEquals(0, wordPairCardViewModel.resultStatistic.noAnswer)
    }
}