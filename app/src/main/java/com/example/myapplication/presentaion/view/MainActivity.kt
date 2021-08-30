package com.example.myapplication.presentaion.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.presentaion.adapter.WordPackageAdapter
import com.example.myapplication.presentaion.adapter.WordPackageAdapterCallback
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.viewmodel.MainViewModel
import com.example.myapplication.data.repository.WordRoomRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), WordPackageAdapterCallback, CreatePackageCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var packageAdapter: WordPackageAdapter

    companion object {
        val CREATE_PACKAGE_DIALOG_TAG = "CreatePackageDialogTag"
    }

    private val mainViewModel: MainViewModel by viewModels() {
        MainViewModel.MainFactory(WordRoomRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.package_recycler_view)
        packageAdapter = WordPackageAdapter(mutableListOf(), this)
        recyclerView.adapter = packageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val floatingActionButton: FloatingActionButton = findViewById(R.id.fab_add_package)
        floatingActionButton.setOnClickListener {
            CreatePackageFragment().show(supportFragmentManager, CREATE_PACKAGE_DIALOG_TAG)
        }

        setWordPackageListObserver()
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
}