package com.togalugombe

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addStory(title: String, description: String) {
        val story = hashMapOf(
            "title" to title,
            "description" to description
        )

        db.collection("stories")
            .document(title)
            .set(story)
    }

    fun getStories(onResult: (List<Story>) -> Unit) {
        db.collection("stories")
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents?.map {
                    Story(
                        title = it.getString("title") ?: "",
                        description = it.getString("description") ?: ""
                    )
                } ?: emptyList()

                onResult(list)
            }
    }
}