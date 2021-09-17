package com.example.myapplication.presentaion.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.utils.ViewState
import com.example.myapplication.presentaion.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject


const val CREATE_PACKAGE_DIALOG_TAG = "CreatePackageDialogTag"

/**
 *  Главная активити, на которой размещаются список пакетов и настройки
 */
class MainActivity : AppCompatActivity(), WordPackageListCallback, CreatePackageCallback {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        setTheme()
        setUpBottomNav(navController)
        (application as App).appComponent.mainComponent().create().inject(this)
        setInsertStateObserver()
    }

    /**
     *  Нажатие на пакет, обработка и открытие другой активности
     *  @param wordPackage - пакет, который нужно передать в другую активность
     */
    override fun onClickWordPackage(wordPackage: WordPackage) {
        startActivity(
            PackageActivity.createIntent(
                this,
                wordPackage.id
            )
        )
    }

    /**
     * Обработка, когда пользователь создает новый пакет
     * @see CreatePackageCallback
     */
    override fun onPackageFragmentYes(
        title: String,
        firstLanguage: String,
        secondLanguage: String
    ) {
        mainViewModel.insertWordPackage(
            WordPackage(0, title, firstLanguage, secondLanguage, listOf())
        )
    }

    /**
     *  Наблюдатель, смотрящий, в каком состоянии находится сохранение пакета в базу данных
     */
    private fun setInsertStateObserver() {
        val observer = Observer<ViewState> { viewstate ->
            if (viewstate == ViewState.ERROR) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_message_insert),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        mainViewModel.insertPackageStateLiveData.observe(this, observer)
    }

    /**
     *  установка нижней панели навигации
     */
    private fun setUpBottomNav(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    /**
     * Опреденеие, ночную тему показывать или дневную в приложении
     */
    private fun setTheme() {
        val sharedPreferences = getSharedPreferences(
            "sharedPrefs", MODE_PRIVATE
        )
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }
    }
}