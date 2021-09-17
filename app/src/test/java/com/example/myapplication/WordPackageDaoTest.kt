package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WordPackageDaoTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var wordPackageObserver: Observer<List<WordPackageEntity>>

    @Mock
    private lateinit var wordPackageListWithWordsObserver: Observer<List<WordPackageWithWords>>

    @Mock
    private lateinit var wordPackageWithWordsObserver: Observer<WordPackageWithWords>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordPackageDao: WordPackageDao
    private lateinit var wordPairDao: WordPairDao
    private lateinit var db: WordRoomDatabase

    @Before
    fun createDb() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.initMocks(this)

        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WordRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        wordPackageDao = db.wordPackageDao()
        wordPairDao = db.wordPairDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testInsertDao() = dispatcher.runBlockingTest {
        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1")
        wordPackageDao.getAll().asLiveData().observeForever(wordPackageObserver)
        wordPackageDao.insertAll(wordPackageEntity)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
        )
        inOrder.verify(wordPackageObserver).onChanged(listOf(wordPackageEntity))
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testUpdateDao() = dispatcher.runBlockingTest {
        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1").apply {
            wordPackageId = 1
        }
        wordPackageDao.getAll().asLiveData().observeForever(wordPackageObserver)
        wordPackageDao.insertAll(wordPackageEntity)
        val wordPackageEntity1 = createWordPackage(title = "title2").apply {
            wordPackageId = 1
        }
        wordPackageDao.update(wordPackageEntity1)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
        )
        inOrder.verify(wordPackageObserver).onChanged(listOf(wordPackageEntity))
        inOrder.verify(wordPackageObserver).onChanged(listOf(wordPackageEntity1))
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testDeleteDao() = dispatcher.runBlockingTest {
        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1").apply {
            wordPackageId = 1
        }
        wordPackageDao.getAll().asLiveData().observeForever(wordPackageObserver)
        wordPackageDao.insertAll(wordPackageEntity)
        wordPackageDao.delete(wordPackageEntity)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
        )
        inOrder.verify(wordPackageObserver).onChanged(listOf(wordPackageEntity))
        inOrder.verify(wordPackageObserver).onChanged(listOf())
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testWordPackageWithWordsDao() = dispatcher.runBlockingTest {

        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1").apply {
            wordPackageId = 1
        }
        val wordPackageEntity2: WordPackageEntity = createWordPackage(title = "title2").apply {
            wordPackageId = 2
        }
        wordPackageDao.insertAll(wordPackageEntity, wordPackageEntity2)
        val wordPair1 = createWordPair().apply {
            wordPairId = 1
        }
        val wordPair2 = createWordPair(wordPackageId = 2L).apply {
            wordPairId = 2
        }
        wordPackageDao.getWordPackageAndPairs().asLiveData()
            .observeForever(wordPackageListWithWordsObserver)
        wordPairDao.insertAll(wordPair1, wordPair2)

        val inOrder = Mockito.inOrder(
            wordPackageListWithWordsObserver,
        )
        inOrder.verify(wordPackageListWithWordsObserver).onChanged(
            listOf(
                WordPackageWithWords(wordPackageEntity, listOf(wordPair1)),
                WordPackageWithWords(wordPackageEntity2, listOf(wordPair2))
            )
        )
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testWordPackageWithWordsByIdDao() = dispatcher.runBlockingTest {

        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1").apply {
            wordPackageId = 1
        }
        val wordPackageEntity2: WordPackageEntity = createWordPackage(title = "title2").apply {
            wordPackageId = 2
        }
        wordPackageDao.insertAll(wordPackageEntity, wordPackageEntity2)
        val wordPair1 = createWordPair().apply {
            wordPairId = 1
        }
        val wordPair2 = createWordPair(wordPackageId = 2L).apply {
            wordPairId = 2
        }
        wordPackageDao.getWordPackageAndPairs(1L).asLiveData()
            .observeForever(wordPackageWithWordsObserver)
        wordPairDao.insertAll(wordPair1, wordPair2)

        val inOrder = Mockito.inOrder(
            wordPackageWithWordsObserver,
        )
        inOrder.verify(wordPackageWithWordsObserver).onChanged(
            WordPackageWithWords(wordPackageEntity, listOf(wordPair1)),
        )
        inOrder.verifyNoMoreInteractions()
    }


}