package com.example.myapplication.presentaion.utils

/**
 *  Enum, хранящий состояние View
 *  LOADING - данные для View в процессе загрузки
 *  LOADED - данные для View успешно загружены. Их необходимо показать
 *  ERROR - произошла ошибка
 */
enum class ViewState {
    LOADING, LOADED, ERROR
}