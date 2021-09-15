package com.example.myapplication.presentaion.view

import android.os.Bundle
import android.os.RecoverySystem
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.data.repository.WordRoomRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import com.example.myapplication.presentaion.adapter.WordPairListAdapter
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.MainViewModel
import com.example.myapplication.presentaion.viewmodel.WordPairListViewModel
import java.lang.NullPointerException
import javax.inject.Inject

private const val WORD_PACKAGE_ID = "WordPackageId"
const val SPAN_COUNT = 2

interface WordPairActivity {
    fun showDialog()

    fun checkKnowledgeButtonPressed()
}

class WordPairListFragment : Fragment() {

    @Inject
    lateinit var wordPairListViewModel: WordPairListViewModel
    private lateinit var titleTextView: TextView
    private var wordPackageId: Long? = null
    private val wordPairListAdapter = WordPairListAdapter(listOf())


    private lateinit var errorTextView: TextView
    private lateinit var progressView: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.wordPairComponent().create().inject(this)
        arguments?.let {
            wordPackageId = it.getLong(WORD_PACKAGE_ID)
        }
        wordPackageId?.let { id ->
            wordPairListViewModel.updateList(id)
        } ?: throw NullPointerException("wordPackageId cannot be null!")
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
            (activity as? WordPairActivity)?.checkKnowledgeButtonPressed()
                ?: throw TypeCastException("Cannot cast activity to interface!")
        }
        errorTextView = view.findViewById(R.id.error_text_view)
        progressView = view.findViewById(R.id.progress_view)
        setWordPairListObserver()
        setViewStateObserver()
        return view
    }

    companion object {

        fun newInstance(wordPackageId: Long) =
            WordPairListFragment().apply {
                arguments = Bundle().apply {
                    putLong(WORD_PACKAGE_ID, wordPackageId)
                }
            }
    }

    private fun setWordPairListObserver() {
        val observer = Observer<WordPackage> { wordPackage ->
            wordPairListAdapter.changeData(wordPackage.wordPairList)
            titleTextView.text = resources.getString(R.string.title_package_text, wordPackage.name)
        }
        wordPairListViewModel.wordListPackage.observe(viewLifecycleOwner, observer)
    }

    private fun setViewStateObserver() {
        val observer = Observer<ViewState> { viewstate ->
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
        }
        wordPairListViewModel.loadViewStateLiveData.observe(this, observer)
    }
}