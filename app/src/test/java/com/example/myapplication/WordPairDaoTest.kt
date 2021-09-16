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
class WordPairDaoTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var wordPairObserver: Observer<List<WordPairEntity>>

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
        val wordPairEntity: WordPairEntity = createWordPair()
        wordPairDao.getAll().asLiveData().observeForever(wordPairObserver)
        wordPairDao.insertAll(wordPairEntity)
        val inOrder = Mockito.inOrder(
            wordPairObserver,
        )
        inOrder.verify(wordPairObserver).onChanged(listOf(wordPairEntity))
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testUpdateDao() = dispatcher.runBlockingTest {
        val wordPairEntity: WordPairEntity = createWordPair(firstWord = "first1").apply {
            wordPairId = 1
        }
        wordPairDao.getAll().asLiveData().observeForever(wordPairObserver)
        wordPairDao.insertAll(wordPairEntity)
        val wordPairEntity1 = createWordPair(firstWord = "first2").apply {
            wordPairId = 1
        }
        wordPairDao.update(wordPairEntity1)
        val inOrder = Mockito.inOrder(
            wordPairObserver,
        )
        inOrder.verify(wordPairObserver).onChanged(listOf(wordPairEntity))
        inOrder.verify(wordPairObserver).onChanged(listOf(wordPairEntity1))
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testDeleteDao() = dispatcher.runBlockingTest {
        val wordPairEntity: WordPairEntity = createWordPair(firstWord = "first1").apply {
            wordPairId = 1
        }
        wordPairDao.getAll().asLiveData().observeForever(wordPairObserver)
        wordPairDao.insertAll(wordPairEntity)
        wordPairDao.delete(wordPairEntity)
        val inOrder = Mockito.inOrder(
            wordPairObserver,
        )
        inOrder.verify(wordPairObserver).onChanged(listOf(wordPairEntity))
        inOrder.verify(wordPairObserver).onChanged(listOf())
        inOrder.verifyNoMoreInteractions()
    }

}