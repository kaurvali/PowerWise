package ee.ut.cs.powerwise.widget

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class WidgetClickAction : ActionCallback {
    override suspend fun onRun(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.i("WidgetClickAction", "Clicked glance with id: $glanceId")
        WidgetWorker.enqueueWork(context)
    }
}