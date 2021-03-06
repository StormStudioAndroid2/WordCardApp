package com.example.myapplication.presentaion.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.data.repository.WordRoomRepository
import com.example.myapplication.domain.model.ResultStatistic
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.model.WordPackageInfoModel
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.PackageViewModel
import javax.inject.Inject

const val PACKAGE_ID_TAG = "PackageIdTag"
private const val WORD_LIST_FRAGMENT = "WordListFragment"
private const val WORD_STACK_FRAGMENT = "WordStackFragment"
private const val WORD_STATISTIC_FRAGMENT = "WordStatisticFragment"

private const val CREATE_WORD_PAIR_DIALOG_TAG = "CreateWordPairDialogTag"

/**
 *  Активити пакета с карточками
 */
class PackageActivity : AppCompatActivity(), WordPairActivity, CreateWordPairCallback,
    WordPairCardActivity, WordPairCardStatisticCallback {

    private lateinit var errorTextView: TextView
    private lateinit var progressView: ProgressBar

    @Inject
    lateinit var packageViewModel: PackageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package)
        errorTextView = findViewById(R.id.error_text_view)
        progressView = findViewById(R.id.progress_view)
        (application as App).appComponent.packageComponent().create().inject(this)

        packageViewModel.loadPackage(intent.getLongExtra(PACKAGE_ID_TAG, -1))
        setViewStateObserver()
        setPackageWithWordsObserver()
        setInsertViewStateObserver()
    }

    companion object {
        fun createIntent(context: Context, packageId: Long): Intent {
            return Intent(context, PackageActivity::class.java).putExtra(PACKAGE_ID_TAG, packageId)
        }
    }

    /**
     * Показывает диалог создания новой карточки
     */
    override fun showDialog() {
        CreateWordPairFragment().show(supportFragmentManager, CREATE_WORD_PAIR_DIALOG_TAG)
    }

    /**
     * Нажатие на кнопку для проверки знаний
     */
    override fun checkKnowledgeButtonPressed(wordPackage: WordPackage) {
            if (wordPackage.wordPairList.isNotEmpty()) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        WordPairCardFragment.newInstance(wordPackage),
                        WORD_STACK_FRAGMENT
                    )
                    .commit()
            }
    }

    /**
     * Обработка, если пользователь нажал на да на диалоге, создающем новую карточку
     * @see CreateWordPairCallback
     */
    override fun onWordPairFragmentYes(frontWord: String, backWord: String) {
        packageViewModel.addNewCardToPackage(frontWord, backWord)
    }

    /**
     * Обработка ситуации, когда список тестируемых карточек кончился и необходимо показать статистику
     */
    override fun onWordPairListEnded(resultStatistic: ResultStatistic) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                WordPairCardStatisticFragment.newInstance(resultStatistic),
                WORD_STATISTIC_FRAGMENT
            )
            .commit()
    }

    /**
     * Установка viewstate
     */
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
        packageViewModel.loadViewStateLiveData.observe(this, observer)
    }


    private fun setInsertViewStateObserver() {
        val observer = Observer<ViewState> { viewstate ->
            if (viewstate == ViewState.ERROR) {
                Toast.makeText(
                    this,
                    getString(R.string.error_message_insert),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        packageViewModel.insertViewStateLiveData.observe(this, observer)
    }

    private fun setPackageWithWordsObserver() {
        val observer = Observer<WordPackage> {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    WordPairListFragment.newInstance(WordPackageInfoModel(it.id)),
                    WORD_LIST_FRAGMENT
                )
                .commit()
        }
        packageViewModel.wordPackageLiveData.observe(this, observer)
    }

    override fun returnToList() {
        loadListWordPairs()
    }

    private fun loadListWordPairs() {
        packageViewModel.wordPackageLiveData.value?.let {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    WordPairListFragment.newInstance(WordPackageInfoModel(it.id)),
                    WORD_LIST_FRAGMENT
                )
                .commit()
        }
    }
}