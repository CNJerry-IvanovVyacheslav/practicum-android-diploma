package ru.practicum.android.diploma.data.utils

import android.util.Log

enum class Currency(val abbr: String, val curName: String) {
    AZN("₼", "Манаты"),
    BYR("Br", "Белорусские рубли"),
    EUR("€", "Евро"),
    GEL("₾", "Грузинский лари"),
    KGS("сом", "Кыргызский сом"),
    KZT("₸", "Тенге"),
    RUR("₽", "Рубли"),
    UAH("₴", "Гривны"),
    USD("$", "Доллары"),
    UZS("so'm", "Узбекский сум");

    companion object {
        fun valueOrNull(abbr: String): Currency? {
            return try {
                valueOf(abbr)
            } catch (e: IllegalArgumentException) {
                e.message?.let { Log.e("Exception", it) }
                null
            }
        }
    }
}
