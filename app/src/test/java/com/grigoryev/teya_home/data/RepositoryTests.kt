package com.grigoryev.teya_home.data

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.grigoryev.teya_home.album.list.data.AlbumRepository
import com.grigoryev.teya_home.album.list.data.AlbumRepositoryImpl
import com.grigoryev.teya_home.album.list.data.dao.AlbumDao
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import com.grigoryev.teya_home.album.list.data.network.*
import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.data.ConnectionRepository
import com.grigoryev.teya_home.core.app.data.ConnectionRepositoryImpl
import com.grigoryev.teya_home.core.app.data.PreferencesRepository
import com.grigoryev.teya_home.core.app.data.PreferencesRepositoryImpl
import com.grigoryev.teya_home.core.BaseTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import net.datafaker.Faker

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTests : BaseTest() {

    // AlbumRepository dependencies
    private lateinit var albumDao: AlbumDao
    private lateinit var itunesApiService: ItunesApiService
    private lateinit var albumRepository: AlbumRepository

    // PreferencesRepository dependencies
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferencesRepository: PreferencesRepository

    // ConnectionRepository dependencies
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionRepository: ConnectionRepository

    private val faker = Faker()

    @Before
    override fun setUp() {
        super.setUp()
        setupAlbumRepository()
        setupPreferencesRepository()
        setupConnectionRepository()
    }

    private fun setupAlbumRepository() {
        albumDao = mockk()
        itunesApiService = mockk()
        albumRepository = AlbumRepositoryImpl(albumDao, itunesApiService)
    }

    private fun setupPreferencesRepository() {
        context = mockk()
        sharedPreferences = mockk()
        every { context.getSharedPreferences(any(), any()) } returns sharedPreferences
        preferencesRepository = PreferencesRepositoryImpl(context)
    }

    private fun setupConnectionRepository() {
        connectivityManager = mockk()
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        connectionRepository = ConnectionRepositoryImpl(context)
    }

    // ==================== AlbumRepository Tests ====================

    @Test
    fun `AlbumRepository getAlbums should return flow from dao`() = runTest {
        // Given
        val expectedAlbums = listOf(
            AlbumEntity(
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
        coEvery { albumDao.getAllAlbums() } returns flowOf(expectedAlbums)

        // When
        val result = albumRepository.getAlbums()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val albums = mutableListOf<AlbumEntity>()
        result.collect { albums.addAll(it) }
        assertEquals(expectedAlbums, albums)
        coVerify { albumDao.getAllAlbums() }
    }

    private fun createRandomItunesResponse(): ItunesResponse {
        return ItunesResponse(
            feed = Feed(
                entries = listOf(
                    Entry(
                        id = Id(
                            label = faker.number().digits(3),
                            attributes = IdAttributes(imId = faker.number().digits(3))
                        ),
                        name = Label(label = faker.music().genre()),
                        artist = Artist(label = faker.music().key()),
                        images = listOf(Image(
                            label = faker.internet().image(), 
                            attributes = ImageAttributes("100")
                        )),
                        releaseDate = ReleaseDate(
                            label = faker.date().birthday().toString(), 
                            attributes = ReleaseDateAttributes(faker.date().birthday().toString())
                        ),
                        category = Category(attributes = CategoryAttributes(
                            term = faker.music().genre(), 
                            label = faker.music().genre()
                        )),
                        price = Price(
                            label = faker.number().randomDouble(2, 1, 20).toString(),
                            attributes = PriceAttributes(
                                amount = faker.number().randomDouble(2, 1, 20).toString(),
                                currency = "USD"
                            )
                        ),
                        contentType = ContentType(
                            label = "Album", 
                            attributes = ContentTypeAttributes("Album", "Album")
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `AlbumRepository loadAlbums should fetch from API and save to database`() = runTest {
        // Given
        val testResponse = createRandomItunesResponse()
        coEvery { itunesApiService.getTopAlbums() } returns testResponse
        coEvery { albumDao.insertAlbums(any()) } returns Unit

        // When
        albumRepository.loadAlbums()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { 
            itunesApiService.getTopAlbums()
            albumDao.insertAlbums(any())
        }
    }

    // ==================== PreferencesRepository Tests ====================
    @Test
    fun `PreferencesRepository getAppRating should return stored rating`() {
        // Given
        val expectedRating = 4
        every { sharedPreferences.getInt("app_rating", 0) } returns expectedRating

        // When
        val result = preferencesRepository.getAppRating()

        // Then
        assertEquals(expectedRating, result)
        verify { sharedPreferences.getInt("app_rating", 0) }
    }

    @Test
    fun `PreferencesRepository getAppRating should return zero when no rating stored`() {
        // Given
        every { sharedPreferences.getInt("app_rating", 0) } returns 0

        // When
        val result = preferencesRepository.getAppRating()

        // Then
        assertEquals(0, result)
        verify { sharedPreferences.getInt("app_rating", 0) }
    }

    @Test
    fun `PreferencesRepository setAppRating should save rating to preferences`() {
        // Given
        val rating = 5
        val editor = mockk<SharedPreferences.Editor>()
        every { sharedPreferences.edit() } returns editor
        every { editor.putInt("app_rating", rating) } returns editor
        every { editor.apply() } returns Unit

        // When
        preferencesRepository.setAppRating(rating)

        // Then
        verify { 
            sharedPreferences.edit()
            editor.putInt("app_rating", rating)
            editor.apply()
        }
    }

    @Test
    fun `PreferencesRepository shouldShowRatingRequest should return true when rating is not 5`() {
        // Given
        every { sharedPreferences.getInt("app_rating", 0) } returns 3

        // When
        val result = preferencesRepository.shouldShowRatingRequest()

        // Then
        assertTrue(result)
        verify { sharedPreferences.getInt("app_rating", 0) }
    }

    @Test
    fun `PreferencesRepository shouldShowRatingRequest should return false when rating is 5`() {
        // Given
        every { sharedPreferences.getInt("app_rating", 0) } returns 5

        // When
        val result = preferencesRepository.shouldShowRatingRequest()

        // Then
        assertFalse(result)
        verify { sharedPreferences.getInt("app_rating", 0) }
    }

    // ==================== ConnectionRepository Tests ====================

    @Test
    fun `ConnectionRepository should initialize with correct preferences name`() {
        // Given & When
        val repository = PreferencesRepositoryImpl(context)

        // Then
        verify { context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE) }
    }
}