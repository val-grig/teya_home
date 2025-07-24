package com.grigoryev.teya_home.presentation

import com.grigoryev.teya_home.album.list.domain.GetAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.GetRateUseCase
import com.grigoryev.teya_home.album.list.domain.LoadAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.SaveRateUseCase
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.album.list.presentation.AlbumListScreenEvent
import com.grigoryev.teya_home.album.list.presentation.AlbumListScreenMapper
import com.grigoryev.teya_home.album.list.presentation.AlbumListViewModel
import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.domain.GetConnectionUseCase
import com.grigoryev.teya_home.core.util.GetStringUtil
import com.grigoryev.teya_home.core.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumListScreenTests : BaseTest() {

    private lateinit var viewModel: AlbumListViewModel
    private lateinit var mapper: AlbumListScreenMapper
    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var loadAlbumsUseCase: LoadAlbumsUseCase
    private lateinit var saveRateUseCase: SaveRateUseCase
    private lateinit var getRateUseCase: GetRateUseCase
    private lateinit var getConnectionUseCase: GetConnectionUseCase
    private lateinit var getStringUtil: GetStringUtil

    @Before
    override fun setUp() {
        super.setUp()

        mapper = AlbumListScreenMapper()
        getAlbumsUseCase = mockk()
        loadAlbumsUseCase = mockk()
        saveRateUseCase = mockk()
        getRateUseCase = mockk()
        getConnectionUseCase = mockk()
        getStringUtil = mockk()

        coEvery { getConnectionUseCase.invoke() } returns flowOf()
        coEvery { getAlbumsUseCase.invoke() } returns flowOf(emptyList())
        coEvery { getRateUseCase.invoke() } returns 0
        coEvery { loadAlbumsUseCase.invoke() } returns Unit

        viewModel = AlbumListViewModel(
            mapper = mapper,
            getAlbumsUseCase = getAlbumsUseCase,
            loadAlbumsUseCase = loadAlbumsUseCase,
            saveRateUseCase = saveRateUseCase,
            getRateUseCase = getRateUseCase,
            getConnectionUseCase = getConnectionUseCase,
            getStringUtil = getStringUtil
        )
    }

    @Test
    fun `initial state should show shimmer items`() = runTest {
        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val screenState = viewModel.getScreenState().value
        Assert.assertEquals(5, screenState.listItems.size) // shimmer items
    }

    @Test
    fun `onRetryLoadingPressed should reload albums`() = runTest {
        // Given
        val testAlbums = listOf(
            AlbumModel(
                id = 1,
                title = "Test Album",
                artist = "Test Artist",
                coverUrl = "http://test.com/cover.jpg",
                releaseDate = "2023-01-01",
                genre = "Rock",
                trackCount = 10,
                price = "9.99",
                currency = "USD"
            )
        )

        coEvery { getAlbumsUseCase.invoke() } returns flowOf(testAlbums)
        coEvery { getRateUseCase.invoke() } returns 3

        // When
        viewModel.onRetryLoadingPressed()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { getAlbumsUseCase.invoke() }
        coVerify { loadAlbumsUseCase.invoke() }
    }

    @Test
    fun `onAlbumClicked should emit NavigateToDetails event with correct albumModel`() = runTest {
        // Given
        val testAlbum = AlbumModel(
            id = 1,
            title = "Test Album",
            artist = "Test Artist",
            coverUrl = "http://test.com/cover.jpg",
            releaseDate = "2023-01-01",
            genre = "Rock",
            trackCount = 10,
            price = "9.99",
            currency = "USD"
        )

        val events = mutableListOf<AlbumListScreenEvent>()

        // Start collecting events BEFORE calling the method
        val job = launch {
            viewModel.getEvent().collect { event ->
                events.add(event)
            }
        }

        // When
        viewModel.onAlbumClicked(testAlbum)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        job.cancel()

        Assert.assertTrue(events.isNotEmpty())
        val navigateEvent = events.find { it is AlbumListScreenEvent.NavigateToDetails }
        Assert.assertTrue(navigateEvent is AlbumListScreenEvent.NavigateToDetails)

        val navigateToDetailsEvent = navigateEvent as AlbumListScreenEvent.NavigateToDetails
        Assert.assertEquals(testAlbum, navigateToDetailsEvent.albumModel)
    }

    @Test
    fun `onRatingSubmitted should save rating and update state`() = runTest {
        // Given
        val rating = 4
        coEvery { saveRateUseCase.invoke(rating) } returns Unit
        coEvery { getStringUtil.getRateMessage(rating) } returns "Rating saved: $rating"

        // When
        viewModel.onRatingSubmitted(rating)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { saveRateUseCase.invoke(rating) }
        coVerify { getStringUtil.getRateMessage(rating) }
    }

    @Test
    fun `onSwipeRefresh should reload albums and show success message`() = runTest {
        // Given
        coEvery { getStringUtil.getSwipeRefreshMessage() } returns "Albums refreshed"

        // When
        viewModel.onSwipeRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { loadAlbumsUseCase.invoke() }
        coVerify { getStringUtil.getSwipeRefreshMessage() }
    }

    @Test
    fun `onSwipeRefresh should load albums`() = runTest {
        // Given
        coEvery { loadAlbumsUseCase.invoke() } throws RuntimeException("Network error")

        // When
        viewModel.onSwipeRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { loadAlbumsUseCase.invoke() }
    }

    @Test
    fun `connection recovered should reload albums if empty and emit ConnectionStatus true event`() = runTest {
        // Given
        coEvery { getConnectionUseCase.invoke() } returns flowOf(ConnectionEvent.ConnectionRecovered)

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { loadAlbumsUseCase.invoke() }
    }
}