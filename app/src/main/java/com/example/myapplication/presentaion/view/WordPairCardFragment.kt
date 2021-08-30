package com.example.myapplication.presentaion.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import com.example.myapplication.presentaion.adapter.WordStackAdapter
import com.example.myapplication.presentaion.viewmodel.WordPairCardViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

private const val CARD_PACKAGE_TAG = "CardPackage"

interface WordPairCardActivity {
    fun onWordPairListEnded()
}

class WordPairCardFragment : Fragment(), CardStackListener {
    // TODO: Rename and change types of parameters
    private var wordPackage: WordPackage? = null
    private val wordPairCardViewModel: WordPairCardViewModel by viewModels()
    private val adapter = WordStackAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wordPackage = it.getParcelable(CARD_PACKAGE_TAG)
        }
        wordPackage?.let {
            wordPairCardViewModel.setWordPairs(it.wordPairList)
        }
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

    override fun onCardSwiped(direction: Direction?) {
        wordPairCardViewModel.onSwiped(direction)
    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

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
                (activity as? WordPairCardActivity)?.onWordPairListEnded()
            }
        }
        wordPairCardViewModel.isEndOfListLiveData.observe(viewLifecycleOwner, observer)

    }
}