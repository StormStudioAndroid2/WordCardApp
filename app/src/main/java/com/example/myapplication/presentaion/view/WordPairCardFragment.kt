package com.example.myapplication.presentaion.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import com.example.myapplication.presentaion.adapter.WordStackAdapter
import com.example.myapplication.presentaion.viewmodel.WordPairCardViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import javax.inject.Inject

const val CARD_PACKAGE_TAG = "CardPackage"

interface WordPairCardActivity {
    fun onWordPairListEnded(resultStatistic: ResultStatistic)
}

/**
 * Фрагмент для тестирования карточек. Карточки показываются в специально адаптере стопкой
 * и их можно смахивать влево или вправо. Фрагмент подсчитывает кол-во правильных ответов и неправильных
 *
 */
class WordPairCardFragment : Fragment(), CardStackListener {

    @Inject
    lateinit var wordPairCardViewModel: WordPairCardViewModel

    private val adapter = WordStackAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.wordPairCardComponent().create(this).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_word_pair_card, container, false)
        val cardStackView = view.findViewById<CardStackView>(R.id.card_stack_view)
        cardStackView.layoutManager = CardStackLayoutManager(view.context, this).apply {
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setScaleInterval(0.95f)
            setDirections(listOf(Direction.Left, Direction.Right))
        }
        cardStackView.adapter = adapter
        setIsEndStackObserver()
        setWordPairStackObserver()
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(wordPackage: WordPackage) =
            WordPairCardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CARD_PACKAGE_TAG, wordPackage)
                }
            }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    /**
     * Действия при смахивании карточки
     */
    override fun onCardSwiped(direction: Direction?) {
        wordPairCardViewModel.onSwiped(direction)
    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    /**
     * Действия, когда карточа пропадает с экрана.
     */
    override fun onCardDisappeared(view: View?, position: Int) {
        wordPairCardViewModel.onCardDisappeared(position)
    }

    private fun setWordPairStackObserver() {
        val observer = Observer<List<WordPair>> { wordPairs ->
            adapter.changeData(wordPairs)
        }
        wordPairCardViewModel.wordPairListLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun setIsEndStackObserver() {
        val observer = Observer<Boolean> { isEnd ->
            if (isEnd) {
                (activity as? WordPairCardActivity)?.onWordPairListEnded(wordPairCardViewModel.resultStatistic)
            }
        }
        wordPairCardViewModel.isEndOfListLiveData.observe(viewLifecycleOwner, observer)
    }
}