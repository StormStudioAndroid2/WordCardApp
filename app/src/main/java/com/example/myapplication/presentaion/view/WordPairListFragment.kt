package com.example.myapplication.presentaion.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.adapter.WordPairListAdapter
import com.example.myapplication.presentaion.model.WordPackageInfoModel
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.WordPairListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

const val WORD_PACKAGE_ID = "WordPackageId"
const val SPAN_COUNT = 2

interface WordPairActivity {
    fun showDialog()

    fun checkKnowledgeButtonPressed(wordPackage: WordPackage)
}

/**
 * Фрагмент, отображающий список всех карточек в пакете
 */
@ExperimentalCoroutinesApi
class WordPairListFragment : Fragment() {

    @Inject
    lateinit var wordPairListViewModel: WordPairListViewModel
    private lateinit var titleTextView: TextView
    private val wordPairListAdapter = WordPairListAdapter(listOf())


    private lateinit var errorTextView: TextView
    private lateinit var progressView: ProgressBar
    private lateinit var reversePackageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.wordPairComponent().create(this).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_word_pair_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.word_pair_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(activity, SPAN_COUNT)
        recyclerView.adapter = wordPairListAdapter

        titleTextView = view.findViewById(R.id.title_text_view)

        val addWordPairView = view.findViewById<View>(R.id.fab_add_word_pair)
        addWordPairView.setOnClickListener {
            (activity as? WordPairActivity)?.showDialog()
                ?: throw TypeCastException("Cannot cast activity to interface!")
        }
        val checkKnowledgeButton: Button = view.findViewById(R.id.check_knowledge_button)
        checkKnowledgeButton.setOnClickListener {
            (activity as? WordPairActivity)?.checkKnowledgeButtonPressed(wordPairListViewModel.wordPackageFlow.value)
                ?: throw TypeCastException("Cannot cast activity to interface!")
        }
        errorTextView = view.findViewById(R.id.error_text_view)
        progressView = view.findViewById(R.id.progress_view)
        reversePackageButton = view.findViewById(R.id.reverse_package)
        reversePackageButton.setOnClickListener {
            wordPairListViewModel.reversePackage()
        }
        setWordPairListObserver()
        setViewStateObserver()
        return view
    }

    companion object {

        fun newInstance(wordPackageInfoModel: WordPackageInfoModel) =
            WordPairListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WORD_PACKAGE_ID, wordPackageInfoModel)
                }
            }
    }

    /**
     * Сеттинг обсервера, следящего за обновлением карточек
     */
    private fun setWordPairListObserver() {
        wordPairListViewModel.wordPackageFlow.onEach { wordPackage ->
            wordPairListAdapter.changeData(wordPackage.wordPairList)
            titleTextView.text = resources.getString(R.string.title_package_text, wordPackage.name)
        }.launchIn(lifecycleScope)
    }

    /**
     * Сеттинг обсервера, следящего за состоянием view
     */
    private fun setViewStateObserver() {
        wordPairListViewModel.loadViewStateFlow.onEach { viewstate ->
            when (viewstate) {
                ViewState.ERROR -> {
                    errorTextView.visibility = View.VISIBLE
                    progressView.visibility = View.GONE
                }
                ViewState.LOADING -> {
                    errorTextView.visibility = View.GONE
                    progressView.visibility = View.VISIBLE
                }
                ViewState.LOADED -> {
                    errorTextView.visibility = View.GONE
                    progressView.visibility = View.GONE
                }
            }
        }.launchIn(lifecycleScope)
    }
}