package com.frankegan.symbiotic

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.frankegan.symbiotic.data.*
import com.frankegan.symbiotic.data.local.SymbioticDatabase
import com.frankegan.symbiotic.data.local.SymbioticLocalDataSource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var db: SymbioticDatabase
    private lateinit var localDataSource: SymbioticLocalDataSource

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SymbioticDatabase::class.java).build()
        localDataSource = SymbioticLocalDataSource(db)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    fun createFermentation() = runBlocking {
        val f = Fermentation(
            title = "Aaa",
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
            unit = "tbsp",
            fermentation = f.id
        )
        val image = Image(
            fileUri = "",
            caption = "foo",
            fermentation = f.id
        )
        val result = localDataSource.saveFermentation(
            fermentation = f,
            note = n,
            ingredients = listOf(ingredient),
            images = listOf(image)
        )
        Assert.assertTrue(result is Result.Success)
    }
}