import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jlpc.facetimelapsemaker.mocks.mockPhotoEntityList
import com.jlpc.facetimelapsemaker.model.PhotoDao
import com.jlpc.facetimelapsemaker.model.PhotoDatabase
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class PhotoDaoTest {
    private lateinit var db: PhotoDatabase
    private lateinit var dao: PhotoDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        db = getDatabaseConfig().build()
        dao = db.photoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    private fun getDatabaseConfig(): RoomDatabase.Builder<PhotoDatabase> {
        return Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PhotoDatabase::class.java,
        )
    }

    @Test
    fun testInsertPhoto() =
        runBlocking {
            val photoEntity = PhotoEntity(uri = mockPhotoEntityList[0].uri, date = Date())
            dao.insertPhoto(photoEntity)

            val allPhotos = dao.getAllPhotos()
            Assert.assertEquals(1, allPhotos.size)
            Assert.assertEquals(photoEntity.uri, allPhotos[0].uri)
            Assert.assertEquals(photoEntity.date, allPhotos[0].date)
        }

    @Test
    fun testGetAllPhotos() =
        runBlocking {
            val photoEntity1 = PhotoEntity(uri = mockPhotoEntityList[0].uri, date = Date())
            val photoEntity2 =
                PhotoEntity(
                    uri = mockPhotoEntityList[1].uri,
                    date =
                        Date(
                            System.currentTimeMillis() + 1000,
                        ),
                )

            dao.insertPhoto(photoEntity1)
            dao.insertPhoto(photoEntity2)

            val allPhotos = dao.getAllPhotos()
            Assert.assertEquals(2, allPhotos.size)
            Assert.assertEquals(photoEntity1.uri, allPhotos[0].uri)
            Assert.assertEquals(photoEntity1.date, allPhotos[0].date)
            Assert.assertEquals(photoEntity2.uri, allPhotos[1].uri)
            Assert.assertEquals(photoEntity2.date, allPhotos[1].date)
        }

    @Test
    fun testDeletePhoto() =
        runBlocking {
            val photoEntity = PhotoEntity(uri = mockPhotoEntityList[0].uri, date = Date())
            dao.insertPhoto(photoEntity)

            // Retrieve the inserted photo to get its ID
            val insertedPhoto = dao.getAllPhotos().first()

            // Create a new PhotoEntity object with the correct ID
            val photoEntityToDelete =
                PhotoEntity(
                    id = insertedPhoto.id,
                    uri = insertedPhoto.uri,
                    date = insertedPhoto.date,
                )

            val deletedRows = dao.deletePhoto(photoEntityToDelete)
            Assert.assertEquals(1, deletedRows)

            val allPhotos = dao.getAllPhotos()
            Assert.assertEquals(0, allPhotos.size)
        }
}
