package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaskScreen()
            }
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var newTitle by remember { mutableStateOf("") }

    // UI state using remember to track the filter preference
    var showOnlyOpen by remember { mutableStateOf(false) }

    val displayedTasks = if (showOnlyOpen) {
        tasks.filter { !it.done }
    } else {
        tasks
    }

    Column(Modifier.padding(16.dp)) {
        // Add Task Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("New task") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    viewModel.addTask(newTitle)
                    newTitle = ""
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Add")
            }
        }

        // Filter Checkbox Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showOnlyOpen,
                onCheckedChange = { showOnlyOpen = it }
            )
            Text("Show only open tasks")
        }

        // Task List
        Column(Modifier.padding(top = 8.dp)) {
            displayedTasks.forEach { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = task.done,
                            onCheckedChange = { viewModel.toggleDone(task.id) }
                        )
                        Text(task.title)
                    }

                    // Delete Button
                    Button(
                        onClick = { viewModel.deleteTask(task.id) }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}