package com.togalugombe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

data class Stories(
    val title: String = "",
    val description: String = ""
)

@Composable
fun HomeScreen(nav: NavController) {

    var stories by remember { mutableStateOf(listOf<Stories>()) }

    val db = FirebaseFirestore.getInstance()


    LaunchedEffect(Unit) {
        db.collection("stories")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener

                stories = snapshot.documents.map { doc ->
                    Stories(
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: ""
                    )
                }
            }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(ShadowBlack)
            .padding(16.dp)
    ) {

        Text("Stories", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            nav.navigate("addStory")
        }) {
            Text("Add Story")
        }
        Text("Stories")

        Spacer(Modifier.height(10.dp))

        LazyColumn {
            items(stories) { story ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(story.title, style = MaterialTheme.typography.titleLarge)
                        Text(story.description)
                    }
                }
            }
        }
    }
}
