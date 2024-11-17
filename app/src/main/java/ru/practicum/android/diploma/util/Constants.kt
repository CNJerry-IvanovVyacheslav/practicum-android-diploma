package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.BuildConfig

const val EMPTY_STRING = ""

// ---- Network const ----
const val ACCESS_TOKEN = BuildConfig.HH_ACCESS_TOKEN
const val HH_REQUEST_HEADER = "Almighty job seeker/1.0 (andrei.urich@yandex.ru)"
const val RESULT_CODE_BAD_REQUEST = 400
const val RESULT_CODE_ERROR = -1
const val CODE_200 = 200
const val CODE_299 = 299

// ---- Details Vacancy const ----
const val DETAILS_VACANCY_ID = "vacancyID"

// ---- Favorite Vacancy const ----
const val DISPATCHER_IO_NAME = "dispatcherIO"
const val CLICK_FAVORITE_DEBOUNCE_DELAY = 2000L

// ---- Search const ----
const val CLICK_DEBOUNCE_DELAY = 1000L
const val SEARCH_DEBOUNCE_DELAY = 2000L
const val ERROR = "ERROR"
const val SEARCH_ERROR = "SEARCH_ERROR"
const val CONNECTION_ERROR = "CONNECTION_ERROR"
const val LOADING = "LOADING"
const val SHOW_RESULT = "SHOW_RESULT"
