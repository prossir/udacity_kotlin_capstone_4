package paolo.udacity.location_reminder.features.delegates.geofenceManagement

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import paolo.udacity.location_reminder.features.broadcastReceivers.GeofenceBroadcastReceiver
import paolo.udacity.location_reminder.features.views.common.models.ReminderModel


class GeofenceManagementDelegateImpl: GeofenceManagementDelegate {
    private lateinit var context: Context
    private lateinit var geofencingClient: GeofencingClient
    private val geofences: ArrayList<Geofence> = arrayListOf()

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun initGeofenceManager(context: Context) {
        this.context = context
        geofencingClient = LocationServices.getGeofencingClient(context)
    }

    override fun createGeofenceFromReminder(reminder: ReminderModel) {
        Geofence.Builder()
            // Set the request ID of the geofence. This is a string to identify this
            // geofence.
            .setRequestId(reminder.id.toString())

            // Set the circular region of this geofence.
            .setCircularRegion(
                reminder.pointOfInterest.latitude,
                reminder.pointOfInterest.longitude,
                reminder.pointOfInterest.rangeRadius.toFloat()
            )
            // Set the expiration duration of the geofence. This geofence gets automatically removed after this period of time.
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            // Set the transition types of interest. Alerts are only generated for these
            // transition. We track entry and exit transitions in this sample.
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            // Create the geofence.
            .build().apply {
                geofences.add(this)
            }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
            addOnSuccessListener {
                // Geofences added
                // ...
            }
            addOnFailureListener {
                // Failed to add geofences
                // ...
            }
        }
    }

    override fun removeGeofenceOfReminder(reminderId: Long) {
        val reminderTag = reminderId.toString()
        geofences.filter {
            it.requestId == reminderTag
        }.forEach {
            geofences.remove(it)
            removeGeofenceFromClient(reminderTag)
        }
    }

    private fun removeGeofenceFromClient(reminderTag: String) {
        geofencingClient.removeGeofences(listOf(reminderTag)).run {
            addOnSuccessListener {
                // Geofences removed
                // ...
            }
            addOnFailureListener {
                // Failed to remove geofences
                // ...
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)//INITIAL_TRIGGER_ENTER)
            addGeofences(geofences)
        }.build()
    }


}