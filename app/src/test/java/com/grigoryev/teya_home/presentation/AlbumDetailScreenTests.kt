package com.grigoryev.teya_home.presentation

import androidx.lifecycle.SavedStateHandle
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailFragment
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailScreenEvent
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailScreenMapper
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailViewModel
import com.grigoryev.teya_home.album.detail.presentation.FormatDateUtil
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.domain.GetConnectionUseCase
import com.grigoryev.teya_home.core.BaseTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumDetailScreenTests : BaseTest() {

    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var mapper: AlbumDetailScreenMapper
    private lateinit var formatDateUtil: FormatDateUtil
    private lateinit var getConnectionUseCase: GetConnectionUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val testAlbum = AlbumModel(
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

    @Before
    override fun setUp() {
        super.setUp()

        formatDateUtil = mockk()
        getConnectionUseCase = mockk()
        savedStateHandle = mockk()

        // Mock SavedStateHandle behavior
        coEvery { savedStateHandle.get<AlbumDetailFragment.NavigationParams>(any()) } returns
            AlbumDetailFragment.NavigationParams(testAlbum)

        // Mock FormatDateUtil behavior
        coEvery { formatDateUtil.invoke(any()) } returns "January 1, 2023"

        // Mock connection use case with empty flow by default
        coEvery { getConnectionUseCase.invoke() } returns flowOf()

        mapper = AlbumDetailScreenMapper(formatDateUtil)
    }

    private fun createViewModel(): AlbumDetailViewModel {
        return AlbumDetailViewModel(
            savedStateHandle = savedStateHandle,
            mapper = mapper,
            getConnectionUseCase = getConnectionUseCase
        )
    }

    @Test
    fun `initial state should map album data correctly`() = runTest {
        // Given
        viewModel = createViewModel()

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val screenState = viewModel.getScreenState().value
        Assert.assertEquals("Test Album", screenState.title)
        Assert.assertEquals("Test Artist", screenState.artist)
        Assert.assertEquals("http://test.com/cover.jpg", screenState.coverUrl)
        Assert.assertEquals("January 1, 2023", screenState.releaseDate)
        Assert.assertEquals("Rock", screenState.genre)
        Assert.assertEquals("10", screenState.trackCount)
        Assert.assertEquals("9.99 USD", screenState.price)
    }

    @Test
    fun `initial state should handle null values correctly`() = runTest {
        // Given
        val albumWithNulls = AlbumModel(
            id = 1,
            title = "Test Album",
            artist = "Test Artist",
            coverUrl = "http://test.com/cover.jpg",
            releaseDate = null,
            genre = null,
            trackCount = null,
            price = null,
            currency = null
        )

        coEvery { savedStateHandle.get<AlbumDetailFragment.NavigationParams>(any()) } returns
            AlbumDetailFragment.NavigationParams(albumWithNulls)
        coEvery { formatDateUtil.invoke(null) } returns ""

        val newMapper = AlbumDetailScreenMapper(formatDateUtil)
        val newViewModel = AlbumDetailViewModel(
            savedStateHandle = savedStateHandle,
            mapper = newMapper,
            getConnectionUseCase = getConnectionUseCase
        )

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val screenState = newViewModel.getScreenState().value
        Assert.assertEquals("Test Album", screenState.title)
        Assert.assertEquals("Test Artist", screenState.artist)
        Assert.assertEquals("http://test.com/cover.jpg", screenState.coverUrl)
        Assert.assertEquals("", screenState.releaseDate)
        Assert.assertEquals("", screenState.genre)
        Assert.assertEquals("", screenState.trackCount)
        Assert.assertEquals("", screenState.price)
    }

    @Test
    fun `connection lost should emit ConnectionStatus false event`() = runTest {
        // Given
        coEvery { getConnectionUseCase.invoke() } returns flowOf(ConnectionEvent.ConnectionLost)
        viewModel = createViewModel()

        val events = mutableListOf<AlbumDetailScreenEvent>()

        val job = launch {
            viewModel.getEvent().collect { event ->
                events.add(event)
            }
        }

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        job.cancel()

        Assert.assertTrue(events.isNotEmpty())
        val connectionEvent = events.find { it is AlbumDetailScreenEvent.ConnectionStatus }
        Assert.assertTrue(connectionEvent is AlbumDetailScreenEvent.ConnectionStatus)

        val connectionStatusEvent = connectionEvent as AlbumDetailScreenEvent.ConnectionStatus
        Assert.assertEquals(false, connectionStatusEvent.isConnected)
    }

    @Test
    fun `connection recovered should emit ConnectionStatus true event`() = runTest {
        // Given
        coEvery { getConnectionUseCase.invoke() } returns flowOf(ConnectionEvent.ConnectionRecovered)
        viewModel = createViewModel()

        val events = mutableListOf<AlbumDetailScreenEvent>()

        val job = launch {
            viewModel.getEvent().collect { event ->
                events.add(event)
            }
        }

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        job.cancel()

        Assert.assertTrue(events.isNotEmpty())
        val connectionEvent = events.find { it is AlbumDetailScreenEvent.ConnectionStatus }
        Assert.assertTrue(connectionEvent is AlbumDetailScreenEvent.ConnectionStatus)

        val connectionStatusEvent = connectionEvent as AlbumDetailScreenEvent.ConnectionStatus
        Assert.assertEquals(true, connectionStatusEvent.isConnected)
    }

    @Test
    fun `price formatting should handle different price and currency combinations`() = runTest {
        // Given
        val testCases = listOf(
            Triple("9.99", "USD", "9.99 USD"),
            Triple("9.99", null, "9.99"),
            Triple(null, "USD", ""),
            Triple(null, null, "")
        )

        testCases.forEach { (price, currency, expected) ->
            val testAlbumWithPrice = testAlbum.copy(price = price, currency = currency)
            
            // Create a fresh SavedStateHandle mock for each test case
            val freshSavedStateHandle: SavedStateHandle = mockk()
            coEvery { freshSavedStateHandle.get<AlbumDetailFragment.NavigationParams>(any()) } returns
                AlbumDetailFragment.NavigationParams(testAlbumWithPrice)

            val newMapper = AlbumDetailScreenMapper(formatDateUtil)
            val newViewModel = AlbumDetailViewModel(
                savedStateHandle = freshSavedStateHandle,
                mapper = newMapper,
                getConnectionUseCase = getConnectionUseCase
            )

            // When
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val screenState = newViewModel.getScreenState().value
            Assert.assertEquals("Expected: $expected, but was: ${screenState.price}", expected, screenState.price)
        }
    }

    @Test
    fun `date formatting should use FormatDateUtil`() = runTest {
        // Given
        coEvery { formatDateUtil.invoke("2023-01-01") } returns "January 1, 2023"
        viewModel = createViewModel()

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val screenState = viewModel.getScreenState().value
        Assert.assertEquals("January 1, 2023", screenState.releaseDate)
    }
}