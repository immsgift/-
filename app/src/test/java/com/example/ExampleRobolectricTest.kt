package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("IMANI | إِيمٓانِي", appName)
  }

  @Test
  fun `verify offline surah loading`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val verses1 = com.example.data.repository.QuranDataSources.getSurahVerses(context, 1)
    assertEquals(7, verses1.size)
    
    val verses2 = com.example.data.repository.QuranDataSources.getSurahVerses(context, 2)
    assertEquals(286, verses2.size)

    val verses114 = com.example.data.repository.QuranDataSources.getSurahVerses(context, 114)
    assertEquals(6, verses114.size)
  }
}
