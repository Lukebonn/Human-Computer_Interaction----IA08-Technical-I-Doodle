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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.example.ia08technicali__doodle__lukebonniwell.ui.theme.IA08TechnicalIDoodleLukeBonniwellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IA08TechnicalIDoodleLukeBonniwellTheme {
                var brushSize by remember { mutableStateOf(10f) }
                var brushColor by remember { mutableStateOf(Color.Black) }
                var clearCanvas by remember { mutableStateOf(false) }
                var backgroundColor by remember { mutableStateOf(Color.White)}

                Scaffold { innerPadding ->
                    DrawingCanvas(
                        modifier = Modifier,
                        brushSize = brushSize,
                        brushColor = brushColor,
                        clearCanvas = clearCanvas,
                        onClear = { clearCanvas = false },
                        backgroundColor = backgroundColor
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(

                        ) {
                            Text(text = "Brush Size: ${brushSize.toInt()}")
                            Row {
                                IncreaseBrushSize(onClick = {
                                    brushSize += 5f
                                })
                                DecreaseBrushSize(onClick = {
                                    brushSize -= 5f
                                })
                            }
                        }
                        ClearCanvasButton(onClick = {
                            clearCanvas = true;
                        })
                        BrushColorDropdown(
//                            selectedColor = brushColor,
                            onColorSelected = { brushColor = it }
                        )
                        BackgroundColorDropdown(
                            backgroundSelectedColor = backgroundColor,
                            onColorSelected = { backgroundColor = it }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun IncreaseBrushSize(onClick: () -> Unit) {
    Button(onClick = {
        println("Increase Clicked!")
        onClick()
    }) {
        Text(
            "+"
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
            "-"
        )
    }
}
@Composable
fun ClearCanvasButton(onClick: () -> Unit) {
    Button(onClick = {
        println("Clear Clicked!")
        onClick()
    }) {
        Text(
            "Clear"
        )
    }
}

val brushColors = listOf(
    Color.Black,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow
)

val brushColorNames = listOf(
    "Black",
    "Red",
    "Green",
    "Blue",
    "Yellow"
)
@Composable
fun BrushColorDropdown(
//    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Column {
        Button(onClick={expanded = true }) {
            Text(text = "Color")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            brushColors.forEachIndexed { index, color ->
                DropdownMenuItem(
                    text = { Text(brushColorNames[index]) },
                    onClick = {
                        selectedIndex = index
                        onColorSelected(color)
                        expanded = false;
                    }
                )
            }
        }
    }
}
@Composable
fun BackgroundColorDropdown(
    backgroundSelectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var dropdownMenu by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Column {
        Button(onClick = { dropdownMenu = true }) {
            Text(text = "Background")
        }
        DropdownMenu(
            expanded = dropdownMenu,
            onDismissRequest = { dropdownMenu = false }
        ) {
            brushColors.forEachIndexed { index, color ->
                DropdownMenuItem(
                    text = { Text(brushColorNames[index]) },
                    onClick = {
                        selectedIndex = index
                        onColorSelected(color)
                        dropdownMenu = false
                    }
                )
            }
        }
    }
}


data class DrawPath(
    val points: SnapshotStateList<Offset> = mutableStateListOf(),
    val size: Float,
    val color: Color
)
@Composable
fun DrawingCanvas(modifier: Modifier = Modifier, brushSize: Float, brushColor: Color, clearCanvas: Boolean, onClear: () -> Unit, backgroundColor: Color) {
    val paths = remember { mutableStateListOf<DrawPath>() }
    var currentPath by remember { mutableStateOf<DrawPath?>(null) }
    val brushSizeState by rememberUpdatedState(brushSize)
    val brushColorState by rememberUpdatedState(brushColor)

    if (clearCanvas) {
        paths.clear()
        onClear()
    }

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
        drawRect(color = backgroundColor);
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