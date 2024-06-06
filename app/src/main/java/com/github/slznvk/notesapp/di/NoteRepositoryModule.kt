package com.github.slznvk.notesapp.di

import com.github.slznvk.notesapp.data.repository.NoteRepositoryImpl
import com.github.slznvk.notesapp.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface PostRepositoryModule {

    @Binds
    fun bindsPostRepository(impl: NoteRepositoryImpl): NoteRepository
}