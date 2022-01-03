package com.example.myapplication.presentaion.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.presentaion.viewmodel.WordPairCardStatisticViewModel
import javax.inject.Inject

const val CARD_STATISTIC_TAG = "CardStatistic"

interface WordPairCardStatisticCallback {
    fun returnToList()
}

/**
 * Фрагмент, показывающий статистики тестирования пользователя в наборе
 * @property resultStatistic - статистика
 */
class WordPairCardStatisticFragment : Fragment() {
    @Inject
    lateinit var wordPairCardViewModel: WordPairCardStatisticViewModel
    private lateinit var circleStatisticView: StatisticCircleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.wordPairCardStatisticComponent().create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_word_pair_result, container, false)
        circleStatisticView = view.findViewById(R.id.circle_statistic_view)
        setCircleViewObservers()
        val button = view.findViewById<Button>(R.id.finish_button)
        button.setOnClickListener {
            (activity as WordPairCardStatisticCallback).returnToList()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(resultStatistic: ResultStatistic) =
            WordPairCardStatisticFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CARD_STATISTIC_TAG, resultStatistic)
                }
            }
    }

    /**
     * Сеттинг LiveData, которая хранит информацию о кол-ве правильных и неправильных ответов
     */
    private fun setCircleViewObservers() {
        val observerRight = Observer<Float> { right ->
            circleStatisticView.rightAnswerPercent = right
        }
        wordPairCardViewModel.rightAnswerPercent.observe(viewLifecycleOwner, observerRight)
        val observerWrong = Observer<Float> { wrong ->
            circleStatisticView.wrongAnswerPercent = wrong
        }
        wordPairCardViewModel.wrongAnswerPercent.observe(viewLifecycleOwner, observerWrong)
        val observerNotAnswer = Observer<Float> { noAnswer ->
            circleStatisticView.notAnswerPercent = noAnswer
        }
        wordPairCardViewModel.notAnswerPercent.observe(viewLifecycleOwner, observerNotAnswer)
    }
}