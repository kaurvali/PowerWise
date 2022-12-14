package ee.ut.cs.powerwise.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.ColorUtils


@Preview
@Composable
fun PreviewChart() {
    val map = mutableMapOf<String, Float>()
    // peab olema protsentides
    map["00"] = 0.1F
    map["01"] = 0.2F
    map["02"] = 0.2F
    map["03"] = 0.1F
    map["04"] = 0.3F
    map["05"] = 0.4F
    map["06"] = 0.2F
    map["07"] = 0.5F
    map["08"] = 0.6F
    map["09"] = 0.7F
    map["10"] = 0.7F
    map["11"] = 0.8F
    map["12"] = 0.95F
    map["13"] = 0.9F
    map["14"] = 0.84F
    map["15"] = 0.74F
    map["16"] = 0.8F
    map["17"] = 0.7F
    map["18"] = 0.6F
    map["19"] = 0.5F
    map["20"] = 0.4F
    map["21"] = 0.45F
    map["22"] = 0.6F
    map["23"] = 1.0F

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                300.dp
            )
    ) {
        Chart(
            data = map,
            height = 200.dp
        )
    }
}

@Composable
fun Chart(
    data: MutableMap<String, Float>,
    barCornersRadius: Float = 25f,
    barColor: Color = MaterialTheme.colorScheme.tertiary,
    barWidth: Float = 30f,
    height: Dp,
    labelOffset: Float = 60f,
    labelColor: Color = Color.Black,
) {

    var screenSize by remember {
        mutableStateOf(Size.Zero)
    }

    var chosenBar by remember {
        mutableStateOf(-1)
    }
    var chosenBarKey by remember {
        mutableStateOf("")
    }
    
    Canvas(modifier = Modifier
        .fillMaxSize()
        .height(height)
        .padding(
            top = 50.dp,
            bottom = 20.dp,
            start = 30.dp,
            end = 30.dp
        )
        .pointerInput(Unit) {
            this.detectTapGestures(onPress = {
                chosenBar = detectPosition(
                    screenSize = screenSize,
                    offset = it,
                    listSize = data.size,
                    itemWidth = barWidth
                )
                if (chosenBar >= 0) {
                    chosenBarKey = data.toList()[chosenBar].first
                }
            })
        }
        .zIndex(100F),
        onDraw = {
            screenSize = size
            val spaceBetweenBars =
                (size.width - (data.size * barWidth)) / (data.size - 1)
            val maxBarHeight = data.values.maxOf { it }
            val barScale = size.height / maxBarHeight
            val paint = Paint().apply {
                this.color = labelColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = 40f
            }

            var spaceStep = 0f
            var chosenTopLeft: Offset = Offset(x=0f, y=0f)

            for (item in data) {
                val topLeft = Offset(
                    x = spaceStep,
                    y = size.height - item.value * barScale - labelOffset
                )
                //--------------------(draw bars)--------------------//
                drawRoundRect(
                    color = barColor,
                    topLeft = topLeft,
                    size = Size(
                        width = barWidth,
                        height = size.height - topLeft.y - labelOffset
                    ),
                    cornerRadius = CornerRadius(barCornersRadius, barCornersRadius)
                )
                //--------------------(showing the x axis labels)--------------------//
                if (Integer.parseInt(item.key) % 2 == 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        item.key.toString(),
                        spaceStep + barWidth / 2,
                        size.height,
                        paint
                    )
                }
                //--------------------(showing the bar label)--------------------//
                if (chosenBarKey == item.key) {
                    chosenTopLeft = topLeft
                }

                spaceStep += spaceBetweenBars + barWidth
            }

            for (item in data) {
                //--------------------(showing the bar label)--------------------//
                if (chosenBarKey == item.key) {
                    val localLabelColor = Color(
                        ColorUtils.blendARGB(
                            Color.White.toArgb(), barColor.toArgb(), 0.4f
                        )
                    )
                    drawRoundRect(
                        color = localLabelColor,
                        topLeft = Offset(x = chosenTopLeft.x - 40f, y = chosenTopLeft.y - 100),
                        size = Size(140f, 80f),
                        cornerRadius = CornerRadius(15f, 15f),
                    )
                    val path = Path().apply {
                        moveTo(chosenTopLeft.x + 50f, chosenTopLeft.y - 20)
                        lineTo(chosenTopLeft.x + 25f, chosenTopLeft.y)
                        lineTo(chosenTopLeft.x, chosenTopLeft.y - 20)
                        lineTo(chosenTopLeft.x + 50f, chosenTopLeft.y - 20)
                    }
                    drawIntoCanvas { canvas ->
                        canvas.drawOutline(
                            outline = Outline.Generic(path = path),
                            paint = androidx.compose.ui.graphics.Paint().apply {
                                color = localLabelColor
                            })
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        item.value.toString(),
                        chosenTopLeft.x + 25,
                        chosenTopLeft.y - 50,
                        paint
                    )
                }
            }


        })
}


private fun detectPosition(screenSize: Size, offset: Offset, listSize: Int, itemWidth: Float): Int {
    val spaceBetweenBars =
        (screenSize.width - (listSize * itemWidth)) / (listSize - 1)
    var spaceStep = 0f
    for (i in 0 until listSize) {
        if (offset.x in spaceStep..(spaceStep + itemWidth)) {
            return i
        }
        spaceStep += spaceBetweenBars + itemWidth
    }
    return -1
}