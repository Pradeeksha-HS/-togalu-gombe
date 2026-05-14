package com.togalugombe

import com.google.firebase.firestore.FirebaseFirestore

fun getStories(onResult: (List<Story>) -> Unit) {

    val db = FirebaseFirestore.getInstance()

    db.collection("stories")
        .get()
        .addOnSuccessListener { result ->

            val list = mutableListOf<Story>()

            for (doc in result) {
                val story = doc.toObject(Story::class.java)
                list.add(story)
            }

            onResult(list)
        }
}

fun saveProgress(scene: Int) {

    val db = FirebaseFirestore.getInstance()

    val story1 = mapOf(
        "title" to "Mahabharata",
        "desc" to "Traditional puppet story"
    )

    db.collection("stories")
        .document("Mahabharata")
        .set(story1)
}



