package com.example.myapplication.presentaion.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.adapter.WordPackageAdapter
import com.example.myapplication.presentaion.adapter.WordPackageAdapterCallback
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.WordPackageListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

/**
 * Колбэк для фрагмента
 */
interface WordPackageListCallback {
    fun onClickWordPackage(wordPackage: WordPackage)
}

/**
 *  Фрагмент, выводящий список всех пакетов
 */
class WordPackageListFragment : Fragment(), WordPackageAdapterCallback {
    private lateinit var recyclerView: RecyclerView
    private lateinit var packageAdapter: WordPackageAdapter
    private lateinit var errorTextView: TextView
    private lateinit var progressView: ProgressBar

    @Inject
    lateinit var wordPackageListViewModel: WordPackageListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.wordpackage_list_layout, container, false)
        val appComponent = (activity?.applicationContext as App).appComponent
        appComponent.wordPackageListComponent().create().inject(this)

        recyclerView = view.findViewById(R.id.package_recycler_view)
        packageAdapter = WordPackageAdapter(mutableListOf(), this)
        recyclerView.adapter = packageAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        errorTextView = view.findViewById(R.id.error_text_view)
        progressView = view.findViewById(R.id.progress_view)

        val floatingActionButton: FloatingActionButton = view.findViewById(R.id.fab_add_package)
        floatingActionButton.setOnClickListener {
            activity?.supportFragmentManager?.let { it1 ->
                CreatePackageFragment().show(
                    it1,
                    CREATE_PACKAGE_DIALOG_TAG
                )
            }
        }
        val editText = view.findViewById<EditText>(R.id.search_edit)
        editText.addTextChangedListener {
            wordPackageListViewModel.onSearchEditTextChanged(it.toString())
        }

        //set observers
        setWordPackageListObserver()
        setViewStateObserver()
        wordPackageListViewModel.loadWordPackagesFromDatabase()
        return view
    }

    /**
     *  Сеттинг обсервера, который наблюдает за списком пакетов
     */
    private fun setWordPackageListObserver() {
        val observer = Observer<List<WordPackage>> { wordPackages ->
            packageAdapter.changeData(wordPackages)
        }
        wordPackageListViewModel.wordPackageListLiveData.observe(viewLifecycleOwner, observer)
    }

    /**
     *  Сеттинг обсервера, который наблюдает за состоянием View
     */
    private fun setViewStateObserver() {
        val observer = Observer<ViewState> { viewState ->
            when (viewState) {
                ViewState.LOADING -> {
                    progressView.visibility = View.VISIBLE
                    errorTextView.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
                ViewState.ERROR -> {
                    progressView.visibility = View.GONE
                    errorTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                ViewState.LOADED -> {
                    progressView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
        wordPackageListViewModel.loadPackageStateLiveData.observe(viewLifecycleOwner, observer)
    }

    override fun onClick(adapterPosition: Int) {
        wordPackageListViewModel.wordPackageListLiveData.value?.let { list ->
            if (adapterPosition != -1) {
                (activity as WordPackageListCallback).onClickWordPackage(list[adapterPosition])
            }
        }
    }
}