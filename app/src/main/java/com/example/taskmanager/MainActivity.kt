package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                MainScreen(Modifier)
            }
        }
    }
}

// honestly quite unnecessary, there's not much here since I did stuff
// under task list and task input field instead, so it's like...
// five lines of non-default code
@Composable
fun MainScreen(modifier: Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            val taskList = remember { mutableStateListOf<String>() }
            TaskInputField(Modifier, taskList)
            TaskList(Modifier, taskList)
        }
    }
}

@Composable
fun TaskInputField(modifier: Modifier, tasks: MutableList<String>) {
    var taskName by remember { mutableStateOf("") }
    Row(
        // Adds to children's padding to be a full 16dp padding on outside.
        modifier = modifier.fillMaxWidth().padding(Dp(8f))
    ) {
        TextField(
            value = taskName,
            // it is... a text box.
            onValueChange = { taskName = it },
            label = { Text("Enter task") },
            // Padding adds with the button padding to yield 16dp padding
            modifier = Modifier.align(Alignment.CenterVertically)
                .padding(Dp(8f)).weight(1f),
        )
        Button(
            onClick = {
                // gotta love passing by reference
                tasks.add(taskName)
                taskName = ""
                      },
            // see above
            modifier = Modifier.requiredWidth(intrinsicSize = IntrinsicSize.Max)
                .align(Alignment.CenterVertically).padding(Dp(8f)),
            colors = ButtonColors(
                containerColor = Color(244, 67, 54, 255),
                contentColor = Color(255,255,255),
                // these should never appear so I won't change them
                disabledContentColor = ButtonDefaults.buttonColors().disabledContentColor,
                disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor
            )
        ) {
            Text("Add Task", maxLines = 1)
        }
    }
}

@Composable
fun TaskList(modifier: Modifier, taskList: MutableList<String>) {
    // actually brain-dead easy to make this part
    LazyColumn(
        // shouldn't hide modifiers unnecessarily
        modifier = modifier,
        ) {
        // I favor passing index and list, instead of the task directly.
        // Is this technically bad?
        // Maybe, but I haven't fully figured out custom events.
        // Or if they'd even be applicable here.
        itemsIndexed(taskList) { index: Int, _ ->
            TaskItem(
                modifier = Modifier,
                taskList = taskList,
                index = index
                )
        }
    }
}

@Composable
fun TaskItem(modifier: Modifier, taskList: MutableList<String>, index: Int) {
    var finished by remember { mutableStateOf(false) }
    val task = taskList[index]
    Row(modifier.padding(Dp(16f))) {
        Checkbox(checked = finished, onCheckedChange = {
            // this should be a safe assumption...
            finished = !finished
        },
        )
        if (finished) {
            Text(
                text = task,
                style = TextStyle(textDecoration = TextDecoration.LineThrough,
                    color = Color.DarkGray, fontSize = TextUnit(16f, TextUnitType.Sp)
                ),
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
        } else {
            Text(
                text = task,
                style = TextStyle(fontSize = TextUnit(16f, TextUnitType.Sp)),
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
        }
        Button(
            shape = CircleShape,
            onClick = {
                // Deletes the item.
                taskList.removeAt(index)
            },
            // No internal padding on the other elements since it looked weird
            // and there's already external padding...
            modifier = Modifier.padding(start = Dp(16f))
        ) {
            Text("X")
        }
    }
}