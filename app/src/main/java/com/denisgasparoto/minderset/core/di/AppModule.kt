package com.denisgasparoto.minderset.core.di

import androidx.room.Room
import com.denisgasparoto.minderset.core.db.AppDatabase
import com.denisgasparoto.minderset.data.repository.FlashCardRepositoryImpl
import com.denisgasparoto.minderset.domain.repository.FlashCardRepository
import com.denisgasparoto.minderset.domain.usecase.AddFlashCardUseCase
import com.denisgasparoto.minderset.domain.usecase.DeleteFlashCardUseCase
import com.denisgasparoto.minderset.domain.usecase.GetFlashCardsUseCase
import com.denisgasparoto.minderset.domain.usecase.ValidateFlashCardUseCase
import com.denisgasparoto.minderset.presentation.FlashCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "minderset-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().flashCardDao() }

    single<FlashCardRepository> { FlashCardRepositoryImpl(get()) }

    factory { GetFlashCardsUseCase(get()) }
    factory { AddFlashCardUseCase(get()) }
    factory { DeleteFlashCardUseCase(get()) }
    factory { ValidateFlashCardUseCase() }

    viewModel { FlashCardViewModel(get(), get(), get(), get()) }
}