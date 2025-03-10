package com.example.android_practice.data

import com.example.android_practice.entity.MovieEntity

object MovieData {
    val movies = listOf(
        MovieEntity(
            id = 7225950,
            name = "Кайрат",
            year = 2025,
            description = "Бизнесмен и президент ФК «Кайрат» Кайрат Боранбаев рассказывает, " +
                    "как управлять спортивным клубом в Казахстане и продавать воспитанников в АПЛ. ",
            rating = 8.1,
            posterUrl = "https://image.openmoviedb.com/kinopoisk-images/4483445/327cb4eb-707d-4a5c-8a98-c3be0d188f98/orig",
            genres = listOf("короткометражка", "документальный", "спорт"),
            countries = listOf("Казахстан")
        ),
        MovieEntity(
            id = 7035692,
            name = "Кризис среднего взрослого",
            year = 2025,
            description = "Обычный человек разбирается, как учиться после 30, знакомясь с экспертами по " +
                    "психологии и работе мозга: что делать, если чувствуешь, что застрял; как любопытство " +
                    "помогает найти призвание; и сколько надо стараться, чтобы призвание стало профессией.",
            rating = 7.6,
            posterUrl = "https://image.openmoviedb.com/kinopoisk-images/9784475/ce7482d5-5623-4cbf-bb55-68908da13870/orig",
            genres = listOf("короткометражка", "документальный"),
            countries = listOf("Россия")

        ),
        MovieEntity(
            id = 6797625,
            name = "Большой мир",
            year = 2024,
            description = "20-летний Лю Чуньхэ страдает от ДЦП и мечтает учиться в обычном университете, а для этого нужно" +
                    " написанное им от руки заявление. Мать не видит в сыне самостоятельного человека и не верит, что он " +
                    "сможет поступить, зато бабушка во всём поддерживает внука. Пока родители в отъезде, бабуля втихаря берёт" +
                    " Чуньхэ с собой на репетиции самодеятельности в парке, где парень знакомится с симпатичной девушкой.",
            rating = 7.4,
            posterUrl = "https://image.openmoviedb.com/kinopoisk-images/10953618/accf7210-4699-4df5-9169-9faaea0cd7f2/orig",
            genres = listOf("драма"),
            countries = listOf("Китай")
        ),
        MovieEntity(
            id = 6739489,
            name = "Это мой голос",
            year = 2024,
            description = "Двое молодых, ярких и полных жизни людей, встречаются на улицах города, и между ними возникает интерес." +
                    " Правда, сразу поговорить не удаётся, а со временем Ваня узнает, какой у Полины голос.",
            rating = 7.8,
            posterUrl = "https://image.openmoviedb.com/kinopoisk-images/10703959/81ded647-dd96-403e-9669-788411a77912/x1000",
            genres = listOf("короткометражка", "мелодрама", "комедия"),
            countries = listOf("Россия")
        )
    )
}