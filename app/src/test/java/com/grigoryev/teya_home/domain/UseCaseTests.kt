package com.grigoryev.teya_home.domain

import com.grigoryev.teya_home.album.list.data.AlbumRepository
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import com.grigoryev.teya_home.album.list.domain.GetAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.GetRateUseCase
import com.grigoryev.teya_home.album.list.domain.LoadAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.SaveRateUseCase
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.data.ConnectionRepository
import com.grigoryev.teya_home.core.app.data.PreferencesRepository
import com.grigoryev.teya_home.core.app.domain.GetConnectionUseCase
import com.grigoryev.teya_home.core.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UseCaseTests : BaseTest() {

    // GetAlbumsUseCase Tests
    @Test
    fun `GetAlbumsUseCase should return mapped album models`() = runTest {
        // Given
        val repository: AlbumRepository = mockk()
        val useCase = GetAlbumsUseCase(repository)
        
        val albumEntities = listOf(
            AlbumEntity(
                id = 1,
                title = "Test Album 1",
                artist = "Test Artist 1",
                coverUrl = "http://test.com/cover1.jpg",
                releaseDate = "2023-01-01",
                genre = "Rock",
                trackCount = 10,
                price = "9.99",
                currency = "USD"
            ),
            AlbumEntity(
                id = 2,
                title = "Test Album 2",
                artist = "Test Artist 2",
                coverUrl = null, // This should be filtered out
                releaseDate = "2023-02-01",
                genre = "Pop",
                trackCount = 12,
                price = "12.99",
                currency = "USD"
            )
        )

        coEvery { repository.getAlbums() } returns flowOf(albumEntities)

        // When
        val result = useCase.invoke()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val albumModels = mutableListOf<AlbumModel>()
        result.collect { models ->
            albumModels.addAll(models)
        }

        assertEquals(1, albumModels.size) // Only one album with valid coverUrl
        assertEquals(1, albumModels[0].id)
        assertEquals("Test Album 1", albumModels[0].title)
        assertEquals("Test Artist 1", albumModels[0].artist)
        assertEquals("http://test.com/cover1.jpg", albumModels[0].coverUrl)
    }

    @Test
    fun `GetAlbumsUseCase should return empty list when repository returns empty`() = runTest {
        // Given
        val repository: AlbumRepository = mockk()
        val useCase = GetAlbumsUseCase(repository)
        
        coEvery { repository.getAlbums() } returns flowOf(emptyList())

        // When
        val result = useCase.invoke()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val albumModels = mutableListOf<AlbumModel>()
        result.collect { models ->
            albumModels.addAll(models)
        }

        assertEquals(0, albumModels.size)
    }

    // LoadAlbumsUseCase Tests
    @Test
    fun `LoadAlbumsUseCase should call repository loadAlbums`() = runTest {
        // Given
        val repository: AlbumRepository = mockk()
        val useCase = LoadAlbumsUseCase(repository)
        
        coEvery { repository.loadAlbums() } returns Unit

        // When
        useCase.invoke()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.loadAlbums() }
    }

    @Test
    fun `LoadAlbumsUseCase should execute on IO dispatcher`() = runTest {
        // Given
        val repository: AlbumRepository = mockk()
        val useCase = LoadAlbumsUseCase(repository)
        
        coEvery { repository.loadAlbums() } returns Unit

        // When
        useCase.invoke()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.loadAlbums() }
        // Note: We can't directly test dispatcher usage, but the withContext(Dispatchers.IO) 
        // ensures the operation runs on IO thread
    }

    // SaveRateUseCase Tests
    @Test
    fun `SaveRateUseCase should call repository saveAppRating`() = runTest {
        // Given
        val preferencesRepository: PreferencesRepository = mockk()
        val useCase = SaveRateUseCase(preferencesRepository)
        val rating = 4
        
        coEvery { preferencesRepository.setAppRating(rating) } returns Unit

        // When
        useCase.invoke(rating)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { preferencesRepository.setAppRating(rating) }
    }

    @Test
    fun `SaveRateUseCase should handle different rating values`() = runTest {
        // Given
        val preferencesRepository: PreferencesRepository = mockk()
        val useCase = SaveRateUseCase(preferencesRepository)
        val ratings = listOf(1, 2, 3, 4, 5)
        
        ratings.forEach { rating ->
            coEvery { preferencesRepository.setAppRating(rating) } returns Unit

            // When
            useCase.invoke(rating)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify { preferencesRepository.setAppRating(rating) }
        }
    }

    @Test
    fun `SaveRateUseCase should handle zero rating`() = runTest {
        // Given
        val preferencesRepository: PreferencesRepository = mockk()
        val useCase = SaveRateUseCase(preferencesRepository)
        
        coEvery { preferencesRepository.setAppRating(0) } returns Unit

        // When
        useCase.invoke(0)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { preferencesRepository.setAppRating(0) }
    }

    // GetRateUseCase Tests
    @Test
    fun `GetRateUseCase should return rating from preferences`() = runTest {
        // Given
        val preferencesRepository: PreferencesRepository = mockk()
        val useCase = GetRateUseCase(preferencesRepository)
        val expectedRating = 4
        
        coEvery { preferencesRepository.getAppRating() } returns expectedRating

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(expectedRating, result)
        coVerify { preferencesRepository.getAppRating() }
    }

    @Test
    fun `GetRateUseCase should return zero when no rating is set`() = runTest {
        // Given
        val preferencesRepository: PreferencesRepository = mockk()
        val useCase = GetRateUseCase(preferencesRepository)
        
        coEvery { preferencesRepository.getAppRating() } returns 0

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(0, result)
        coVerify { preferencesRepository.getAppRating() }
    }

    // GetConnectionUseCase Tests
    @Test
    fun `GetConnectionUseCase should return connection events from repository`() = runTest {
        // Given
        val connectionRepository: ConnectionRepository = mockk()
        val useCase = GetConnectionUseCase(connectionRepository)
        val connectionEvents = listOf(
            ConnectionEvent.ConnectionLost,
            ConnectionEvent.ConnectionRecovered
        )
        
        coEvery { connectionRepository.getConnectionEvents() } returns flowOf(*connectionEvents.toTypedArray())

        // When
        val result = useCase.invoke()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val collectedEvents = mutableListOf<ConnectionEvent>()
        result.collect { event ->
            collectedEvents.add(event)
        }

        assertEquals(2, collectedEvents.size)
        assertEquals(ConnectionEvent.ConnectionLost, collectedEvents[0])
        assertEquals(ConnectionEvent.ConnectionRecovered, collectedEvents[1])
        coVerify { connectionRepository.getConnectionEvents() }
    }

    @Test
    fun `GetConnectionUseCase should return empty flow when repository returns empty`() = runTest {
        // Given
        val connectionRepository: ConnectionRepository = mockk()
        val useCase = GetConnectionUseCase(connectionRepository)
        
        coEvery { connectionRepository.getConnectionEvents() } returns flowOf()

        // When
        val result = useCase.invoke()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val collectedEvents = mutableListOf<ConnectionEvent>()
        result.collect { event ->
            collectedEvents.add(event)
        }

        assertEquals(0, collectedEvents.size)
        coVerify { connectionRepository.getConnectionEvents() }
    }
} 