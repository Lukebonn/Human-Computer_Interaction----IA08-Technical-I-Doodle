package com.example.ia08technicali__doodle__lukebonniwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.*
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ia08technicali__doodle__lukebonniwell.ui.theme.IA08TechnicalIDoodleLukeBonniwellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IA08TechnicalIDoodleLukeBonniwellTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DrawingCanvas()
                    FilledButtonExample(onClick = {
                        println("Button Clicked!")
                    })
                }
            }
        }
    }
}
@Composable
fun FilledButtonExample(onClick: () -> Unit) {
    Button(onClick = {
        println("button Clicked!")
    }) {
        Text(
            "Filled"
        )
    }
}
@Composable
fun DrawingCanvas() {
    val paths = remember { mutableStateListOf<MutableList<Offset>>() }
    var currentPath = remember { mutableStateListOf<Offset>() }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    currentPath = mutableStateListOf<Offset>()
                    paths.add(currentPath)
                },
                onDrag = { change, _ ->
                    currentPath.add(change.position)
                }
            )
        }
    ) {
        paths.forEach { path ->
            for (i in 0 until path.size - 1) {
                drawLine(
                    start = path[i],
                    end = path[i + 1],
                    strokeWidth = 10f,
                    color = Color.Black
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    IA08TechnicalIDoodleLukeBonniwellTheme {
//        Greeting("Android")
//    }
//}