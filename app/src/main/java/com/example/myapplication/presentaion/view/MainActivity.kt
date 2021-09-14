package com.example.myapplication.presentaion.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.presentaion.adapter.WordPackageAdapter
import com.example.myapplication.presentaion.adapter.WordPackageAdapterCallback
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.viewmodel.MainViewModel
import com.example.myapplication.presentaion.utils.ViewState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject


const val CREATE_PACKAGE_DIALOG_TAG = "CreatePackageDialogTag"

class MainActivity : AppCompatActivity(), WordPackageAdapterCallback, CreatePackageCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var packageAdapter: WordPackageAdapter
    private lateinit var errorTextView: TextView
    private lateinit var progressView: ProgressBar

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appComponent = (applicationContext as App).appComponent
        appComponent.mainComponent().create().inject(this)

        recyclerView = findViewById(R.id.package_recycler_view)
        packageAdapter = WordPackageAdapter(mutableListOf(), this)
        recyclerView.adapter = packageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        errorTextView = findViewById(R.id.error_text_view)
        progressView = findViewById(R.id.progress_view)

        val floatingActionButton: FloatingActionButton = findViewById(R.id.fab_add_package)
        floatingActionButton.setOnClickListener {
            CreatePackageFragment().show(supportFragmentManager, CREATE_PACKAGE_DIALOG_TAG)
        }
        val editText = findViewById<EditText>(R.id.search_edit)
        editText.addTextChangedListener {
            mainViewModel.onSearchEditTextChanged(it.toString())
        }

        //set observers
        setWordPackageListObserver()
        setViewStateObserver()
        setInsertStateObserver()

        mainViewModel.loadWordPackagesFromDatabase()
    }

    override fun onClick(adapterPosition: Int) {
        val id = mainViewModel.wordPackageListLiveData.value?.get(adapterPosition)?.id
        if (id != null) {
            startActivity(
                PackageActivity.createIntent(
                    this,
                    id
                )
            )
        }
    }

    private fun setWordPackageListObserver() {
        val observer = Observer<List<WordPackage>> { wordPackages ->
            packageAdapter.changeData(wordPackages)
        }
        mainViewModel.wordPackageListLiveData.observe(this, observer)
    }

    override fun onPackageFragmentYes(
        title: String,
        firstLanguage: String,
        secondLanguage: String
    ) {
        mainViewModel.insertWordPackage(
            WordPackage(0, title, firstLanguage, secondLanguage, listOf())
        )
    }

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
        mainViewModel.loadPackageStateLiveData.observe(this, observer)
    }

    private fun setInsertStateObserver() {
        val observer = Observer<ViewState> { viewstate ->
            if (viewstate == ViewState.ERROR) {
                Toast.makeText(this, getString(R.string.error_message_insert), Toast.LENGTH_LONG)
                    .show()
            }
        }
        mainViewModel.insertPackageStateLiveData.observe(this, observer)
    }
}