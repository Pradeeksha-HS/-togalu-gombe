package com.togalugombe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.togalugombe.FirebaseRepository

@Composable
fun AddStoryScreen(onBack: () -> Unit) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Add Story", fontSize = 24.sp)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {


                if (title.isBlank() || description.isBlank()) {
                    Toast.makeText(ctx, "Fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }


                FirebaseRepository.addStory(title, description)

                Toast.makeText(ctx, "Story Added", Toast.LENGTH_SHORT).show()

                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Story")
        }
    }
}