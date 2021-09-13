package com.example.myapplication.presentaion.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.data.repository.WordRoomRepository
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.presentaion.viewmodel.PackageViewModel
import javax.inject.Inject

private const val PACKAGE_ID_TAG = "PackageIdTag"
private const val WORD_LIST_FRAGMENT = "WordListFragment"
private const val WORD_STACK_FRAGMENT = "WordStackFragment"
private const val WORD_STATISTIC_FRAGMENT = "WordStatisticFragment"

private const val CREATE_WORD_PAIR_DIALOG_TAG = "CreateWordPairDialogTag"

class PackageActivity : AppCompatActivity(), WordPairActivity, CreateWordPairCallback,
    WordPairCardActivity {

    @Inject
    lateinit var packageViewModel: PackageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package)
        (application as App).appComponent.packageComponent().create().inject(this)
        val id = intent.getLongExtra(PACKAGE_ID_TAG, -1)
        if (id != -1L) {
            packageViewModel.loadPackage(id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WordPairListFragment.newInstance(id), WORD_LIST_FRAGMENT)
                .commit()
        } else throw NullPointerException("Not pass package id!")
    }

    companion object {
        fun createIntent(context: Context, packageId: Long): Intent {
            return Intent(context, PackageActivity::class.java).putExtra(PACKAGE_ID_TAG, packageId)
        }
    }

    override fun showDialog() {
        CreateWordPairFragment().show(supportFragmentManager, CREATE_WORD_PAIR_DIALOG_TAG)
    }

    override fun checkKnowledgeButtonPressed() {
        packageViewModel.wordPackageLiveData.value?.let { wordPackage ->
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    WordPairCardFragment.newInstance(wordPackage),
                    WORD_STACK_FRAGMENT
                )
                .commit()
        }
    }

    override fun onWordPairFragmentYes(frontWord: String, backWord: String) {
        packageViewModel.addNewCardToPackage(frontWord, backWord)
    }

    override fun onWordPairListEnded(resultStatistic: ResultStatistic) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                WordPairCardStatisticFragment.newInstance(resultStatistic),
                WORD_STATISTIC_FRAGMENT
            )
            .commit()
    }
}