package com.example.myapplication.domain.usecase

import com.example.myapplication.data.repository.IWordRepository
import com.example.myapplication.domain.model.WordPackage
import com.example.myapplication.presentaion.model.WordPackageInfoModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UpdateWordPairsUseCase @Inject constructor(
    iWordRepository: IWordRepository,
    wordPackageInfoModel: WordPackageInfoModel
) {
    private val reverseFlow = MutableStateFlow(false)

    fun subscribeOnWordPackageById(): Flow<WordPackage> {
        return packageFlow
    }

    fun updateReverse() {
        reverseFlow.value = !reverseFlow.value
    }

    private val packageFlow = iWordRepository.getPackageWithWordsById(wordPackageInfoModel.id)
        ?.combine(reverseFlow) { wordPackege, reverse ->
            if (wordPackege.isReversed != reverse) {
                return@combine wordPackege.reversePackage()
            }
            return@combine wordPackege
        } ?: flowOf()
}