package com.example.taskawaitissue

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class Repository {
    fun getThings() = flow<List<Thing>> {
        println("Attempting to get things...")

        /**
         * Using `Tasks.await(...)` here results in an Android Studio warning regarding "Inappropriate blocking method call"
         * and a runtime error: "Must not be called on the main application thread"
         *
         * Adding a `.flowOn(Dispatchers.IO)` to the flow chain removes the runtime error and makes the test pass,
         * but the ugly Android Studio warning regarding "Inappropriate blocking method call" remains.
         */
        val things = FirebaseFirestore.getInstance().collection("things").get().await()
        println("Got things!")

        emit(things.toObjects(Thing::class.java))
    }

    data class Thing(
        val id: String = "",
        val title: String = ""
    )
}
