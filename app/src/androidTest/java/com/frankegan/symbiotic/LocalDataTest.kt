package com.frankegan.symbiotic

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.frankegan.symbiotic.data.*
import com.frankegan.symbiotic.data.local.SymbioticDatabase
import com.frankegan.symbiotic.data.local.SymbioticLocalDataSource
import com.frankegan.symbiotic.data.units.VolumeDisplayUnit
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {

    companion object {
        private lateinit var db: SymbioticDatabase
        private lateinit var localDataSource: SymbioticLocalDataSource

        @BeforeClass
        @JvmStatic
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(context, SymbioticDatabase::class.java).build()
            localDataSource = SymbioticLocalDataSource(db)
        }

        @AfterClass
        @JvmStatic
        @Throws(IOException::class)
        fun closeDb() {
            db.close()
        }
    }

    @Test
    fun createFermentation() = runBlocking {
        val f = Fermentation(
            title = "Aaa",
            headerUrl = "",
            startDate = LocalDateTime.now(),
            firstEndDate = LocalDateTime.now(),
            secondEndDate = LocalDateTime.now()
        )
        val n = Note(
            content = "foo",
            fermentation = f.id
        )
        val ingredient = Ingredient(
            name = "lemon",
            quantity = 1.0,
            unit = VolumeDisplayUnit.Tablespoon,
            fermentation = f.id
        )
        val image = Image(
            fileUri = "",
            caption = "foo",
            fermentation = f.id
        )
        val writeResult = localDataSource.saveFermentation(
            fermentation = f,
            note = n,
            ingredients = listOf(ingredient),
            images = listOf(image)
        )
        Assert.assertTrue(writeResult is Result.Success)

        val readResult = localDataSource.getIngredients(f.id)
        Assert.assertTrue(readResult is Result.Success)
        Assert.assertTrue(readResult.getOrNull()?.isNotEmpty() ?: false)
    }
}