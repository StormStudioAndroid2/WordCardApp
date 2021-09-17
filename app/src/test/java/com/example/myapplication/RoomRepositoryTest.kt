package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.database.*
import com.example.myapplication.data.repository.WordRoomRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
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
import org.mockito.kotlin.eq
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WordRoomRepositoryTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var wordPairObserver: Observer<List<WordPair>>

    @Mock
    private lateinit var wordPackageListObserver: Observer<List<WordPackage>>

    @Mock
    private lateinit var wordPackageObserver: Observer<WordPackage>

    @Mock
    private lateinit var wordPackageEntityObserver: Observer<List<WordPackageEntity>>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordRoomRepository: WordRoomRepository

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
        wordRoomRepository = WordRoomRepository(db)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testGetAllPackages() = dispatcher.runBlockingTest {
        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1").apply {
            wordPackageId = 1
        }
        wordPackageDao.insertAll(wordPackageEntity)
        val inOrder = Mockito.inOrder(
            wordPackageListObserver,
        )
        wordRoomRepository.getAllPackages().asLiveData().observeForever(wordPackageListObserver)
        inOrder.verify(wordPackageListObserver)
            .onChanged(eq(listOf(convertToDomain(wordPackageEntity))))
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun testGetAllPackagesWithWordPairs() = dispatcher.runBlockingTest {
        val wordPackageEntity: WordPackageEntity = createWordPackage(title = "title1").apply {
            wordPackageId = 1
        }
        val wordPair1 = createWordPair().apply {
            wordPairId = 1
        }
        wordPackageDao.insertAll(wordPackageEntity)
        wordPairDao.insertAll(wordPair1)
        val inOrder = Mockito.inOrder(
            wordPackageObserver,
        )
        wordRoomRepository.getPackageWithWordsById(wordPackageId = 1).asLiveData()
            .observeForever(wordPackageObserver)
        val wordPackage = convertToDomain(wordPackageEntity).apply {
            wordPairList = listOf(convertToDomain(wordPair1))
        }
        inOrder.verify(wordPackageObserver).onChanged(eq(wordPackage))
        inOrder.verifyNoMoreInteractions()
    }

    @InternalCoroutinesApi
    @Test
    @Throws(Exception::class)
    fun insertInDatabase() = dispatcher.runBlockingTest {
        val wordPackageEntity = createWordPackage().apply {
            wordPackageId = 1
        }
        wordRoomRepository.addPackage(convertToDomain(wordPackageEntity))
        val inOrder = Mockito.inOrder(
            wordPackageEntityObserver,
        )
        wordPackageDao.getAll().asLiveData()
            .observeForever(wordPackageEntityObserver)
        inOrder.verify(wordPackageEntityObserver).onChanged(listOf(wordPackageEntity))
        inOrder.verifyNoMoreInteractions()
    }
}