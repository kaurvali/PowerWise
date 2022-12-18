package ee.ut.cs.powerwise.widget.ui


import androidx.compose.runtime.Composable

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.text.Text
import androidx.glance.GlanceModifier

import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator

import androidx.glance.appwidget.action.actionRunCallback

import androidx.glance.currentState
import androidx.glance.layout.*


import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import ee.ut.cs.powerwise.widget.WidgetClickAction
import ee.ut.cs.powerwise.widget.WidgetState
import ee.ut.cs.powerwise.widget.WidgetStateDefinition

class PowerWiseWidget : GlanceAppWidget() {

    override val stateDefinition = WidgetStateDefinition

    @Composable
    override fun Content() {
        val state = currentState<WidgetState>()

        GlanceTheme {
            when (state) {
                is WidgetState.Data -> WidgetContent(state)
                is WidgetState.DataLoading -> WidgetLoading()
                is WidgetState.DataLoadingFailed -> WidgetLoadingFailed()
            }
        }
    }

    @Composable
    private fun WidgetLoading() {
        ColumnBaseLayout(
            modifier = GlanceModifier.clickable(actionRunCallback<WidgetClickAction>()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun WidgetLoadingFailed() {
        ColumnBaseLayout(
            modifier = GlanceModifier.clickable(actionRunCallback<WidgetClickAction>()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryText(text = "Data loading failed!")
        }
    }

    @Composable
    private fun WidgetContent(data: WidgetState.Data) {
        ColumnBaseLayout(modifier = GlanceModifier.clickable(actionRunCallback<WidgetClickAction>())) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                PrimaryText(
                    text = data.currentPrice,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
                PrimaryText(text = "s/kWh", fontSize = 18.sp)
            }
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                    horizontalAlignment = Alignment.Start
                ) {
                    PrimaryText(text = "Päeva keskmine")
                }
                Row(
                    modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                    horizontalAlignment = Alignment.End
                ) {
                    PriceText(
                        text = data.nextHourPrice1,
                        unit = "s",
                        horizontalAlignment = Alignment.End,
                        modifier = GlanceModifier.defaultWeight()
                    )
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    PrimaryText(text = "${data.nextHourPrice1Hour}:00")
                }
            }
            Row(modifier = GlanceModifier.fillMaxWidth().defaultWeight()) {
                PriceText(
                    text = data.averagePrice,
                    unit = "s",
                    fontSize = 18.sp,
                    modifier = GlanceModifier.defaultWeight()
                )
                Row(
                    modifier = GlanceModifier.fillMaxSize().defaultWeight(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    PriceText(
                        text = data.nextHourPrice2,
                        unit = "s",
                        horizontalAlignment = Alignment.End,
                        modifier = GlanceModifier.defaultWeight()
                    )
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    PrimaryText(text = "${data.nextHourPrice2Hour}:00")
                }
            }
        }
    }
}


@Composable
fun PrimaryText(
    text: String,
    fontSize: TextUnit? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = TextStyle(
            color = GlanceTheme.colors.onPrimaryContainer,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign
        )
    )
}

@Composable
fun PriceText(
    modifier: GlanceModifier = GlanceModifier,
    text: String,
    unit: String,
    fontSize: TextUnit? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Bottom
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth().then(modifier),
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment
    ) {
        PrimaryText(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
        PrimaryText(text = unit)
    }
}