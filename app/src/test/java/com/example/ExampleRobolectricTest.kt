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
    for (id in 1..114) {
      val verses = com.example.data.repository.QuranDataSources.getSurahVerses(context, id)
      assert(verses.isNotEmpty()) { "Surah $id is empty!" }
    }
  }
}
