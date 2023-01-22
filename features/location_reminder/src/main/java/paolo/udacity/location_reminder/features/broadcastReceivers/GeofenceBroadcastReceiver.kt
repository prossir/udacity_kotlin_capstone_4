package paolo.udacity.location_reminder.features.broadcastReceivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber


class GeofenceBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent?.hasError() == true) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Timber.tag(TAG).e(errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent?.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
            val geofenceTransitionDetails = getGeofenceTransitionDetails(
                geofenceTransition,
                triggeringGeofences
            )

            // Send notification and log the transition details.
            context?.let {
                sendNotification(it, geofenceTransitionDetails)
            }
            Timber.tag(TAG).i(geofenceTransitionDetails)
        }
        else {
            // Log the error.
            Timber.tag(TAG).e("geofence_transition_invalid_type %s", geofenceTransition)
        }
    }

    private fun getGeofenceTransitionDetails(
        geofenceTransition: Int,
        triggeringGeofences: List<Geofence>?
    ): String {
        var triggered = ""
        triggeringGeofences?.map {
            triggered += "${it.requestId} $geofenceTransition"
        }
        return triggered
    }

    private fun sendNotification(
        context: Context,
        geofenceTransitionDetails: String
    ) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?


    }

    companion object {
        private const val TAG = "TAG_GEOFENCE_BROADCAST_RECEIVER"
    }
}