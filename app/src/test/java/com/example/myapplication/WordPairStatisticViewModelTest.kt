package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.presentaion.viewmodel.WordPairCardStatisticViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class WordPairStatisticViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var wordPairCardStatisticViewModel: WordPairCardStatisticViewModel

    @Mock
    private lateinit var rightAnswersObserver: Observer<Float>

    @Mock
    private lateinit var wrongAnswersObserver: Observer<Float>

    @Mock
    private lateinit var notAnswersObserver: Observer<Float>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        wordPairCardStatisticViewModel = WordPairCardStatisticViewModel()
        wordPairCardStatisticViewModel.rightAnswerPercent.observeForever(rightAnswersObserver)
        wordPairCardStatisticViewModel.wrongAnswerPercent.observeForever(wrongAnswersObserver)
        wordPairCardStatisticViewModel.notAnswerPercent.observeForever(notAnswersObserver)

    }

    @Test
    fun testSendNewResult() {
        wordPairCardStatisticViewModel.onResultStatisticReceived(createTestData())
        val inOrder = Mockito.inOrder(
            rightAnswersObserver,
            wrongAnswersObserver,
            notAnswersObserver
        )
        inOrder.verify(rightAnswersObserver).onChanged(ArgumentMatchers.eq(0.6f))
        inOrder.verify(wrongAnswersObserver).onChanged(ArgumentMatchers.eq(0.2f))
        inOrder.verify(notAnswersObserver).onChanged(ArgumentMatchers.eq(0.2f))
        inOrder.verifyNoMoreInteractions()
    }

    private fun createTestData(): ResultStatistic {
        return ResultStatistic(6, 2, 2)
    }
}