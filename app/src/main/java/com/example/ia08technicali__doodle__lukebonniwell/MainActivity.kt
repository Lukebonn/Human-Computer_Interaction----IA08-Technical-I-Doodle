package com.example.ia08technicali__doodle__lukebonniwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Button
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ia08technicali__doodle__lukebonniwell.ui.theme.IA08TechnicalIDoodleLukeBonniwellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IA08TechnicalIDoodleLukeBonniwellTheme {
                var brushSize by remember { mutableStateOf(10f) }
                var brushColor by remember { mutableStateOf(Color.Black) }
                var ClearCanvas by remember { mutableStateOf(false) }

                Scaffold { innerPadding ->
                    DrawingCanvas(
                        modifier = Modifier,
                        brushSize = brushSize,
                        brushColor = brushColor,
                        clearCanvas = ClearCanvas
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        FilledButtonExample(onClick = {
                            println("Button Clicked!")
                        })
                        IncreaseBrushSize(onClick = {
                            brushSize += 5f
                        })
                        DecreaseBrushSize(onClick = {
                            brushSize -= 5f
                        })
                    }
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
fun IncreaseBrushSize(onClick: () -> Unit) {
    Button(onClick = {
        println("Increase Clicked!")
        onClick()
    }) {
        Text(
            "Increase"
        )
    }
}
@Composable
fun DecreaseBrushSize(onClick: () -> Unit) {
    Button(onClick = {
        println("Decrease Clicked!")
        onClick()
    }) {
        Text(
            "Decrease"
        )
    }
}

data class DrawPath(
    val points: SnapshotStateList<Offset> = mutableStateListOf(),
    val size: Float,
    val color: Color
)
@Composable
fun DrawingCanvas(modifier: Modifier = Modifier, brushSize: Float, brushColor: Color, clearCanvas: Boolean) {
    val paths = remember { mutableStateListOf<DrawPath>() }
    var currentPath by remember { mutableStateOf<DrawPath?>(null) }
    val brushSizeState by rememberUpdatedState(brushSize)
    val brushColorState by rememberUpdatedState(brushColor)

    Canvas(
        modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    val newPath = DrawPath(
                        points = mutableStateListOf(),
                        size = brushSizeState,
                        color = brushColorState
                    )
                    currentPath = newPath
                    paths.add(newPath)
                },
                onDrag = { change, _ ->
                    currentPath?.points?.add(change.position)
                },
                onDragEnd = {
                    currentPath = null
                }
            )
        }
    ) {
        paths.forEach { path ->
            for (i in 0 until path.points.size - 1) {
                drawLine(
                    start = path.points[i],
                    end = path.points[i + 1],
                    strokeWidth = path.size,
                    color = path.color
                )
            }
        }
    }
}