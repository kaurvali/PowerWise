package ee.ut.cs.powerwise.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import ee.ut.cs.powerwise.widget.ui.PowerWiseWidget

class PowerWiseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = PowerWiseWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            WidgetWorker.enqueueWork(it)
        }
    }
}