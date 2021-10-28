package com.example.taskawaitissue

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private lateinit var firestoreInstance: FirebaseFirestore

    @Before
    fun beforeEach() {
        val config =
            FirebaseOptions.Builder().setApiKey("blah").setApplicationId("bogus")
                .setProjectId("demo-await-issue")
                .build()

        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext(), config)

        firestoreInstance = FirebaseFirestore.getInstance().apply {
            useEmulator("localhost", 8080)
            firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
        }
    }

    @Test
    fun testGetThings() = runBlocking {
        mutableListOf<Repository.Thing>().apply {
            add(Repository.Thing("a", "thing A"))
            add(Repository.Thing("b", "thing B"))
            add(Repository.Thing("c", "thing C"))
        }.forEachIndexed { index, thing ->
            println("adding thing #${index + 1}")

            /**
             * Using the `.await()` extension function here will add the first `Thing` to the database
             * (verified via UI console @ localhost:4000, but then block the thread.
             *
             * Using `Tasks.await(...)` here will add all three values to the database, but must be wrapped in
             * a surrounding `withContext(Dispatchers.IO) {...}` block to avoid an error message about running on the main thread/
             */
            firestoreInstance.collection("things").add(thing).await()
        }


        Repository().getThings().take(1).collect {
            MatcherAssert.assertThat(it.size, CoreMatchers.`is`(3))
        }
    }
}
