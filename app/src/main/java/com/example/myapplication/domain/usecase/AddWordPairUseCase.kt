package com.example.myapplication.domain.usecase

import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.domain.model.WordPair
import javax.inject.Inject

class AddWordPairUseCase @Inject constructor(private val iWordRepository: IWordRepository) {

    suspend fun addWordPair(wordPair: WordPair, wordPackage: WordPackage) {
        iWordRepository.addWordPair(wordPair.frontWord, wordPair.backWord, wordPackage.id)
    }
}