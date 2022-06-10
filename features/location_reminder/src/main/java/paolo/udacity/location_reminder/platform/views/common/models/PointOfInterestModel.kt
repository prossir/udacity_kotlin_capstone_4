package paolo.udacity.location_reminder.platform.views.common.models

import androidx.databinding.ObservableField
import com.google.android.gms.maps.model.LatLng
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.constants.FoundationConstants


data class PointOfInterestModel(
    var id: Long,
    var latitude: Double,
    var longitude: Double,
    var rangeRadius: Double,
    var status: Int,
    var createdAt: OffsetDateTime?,
    var updatedAt: OffsetDateTime?
) {
    // Function
    fun reset() {
        id = FoundationConstants.EMPTY_LONG
        latitude = FoundationConstants.EMPTY_DOUBLE
        longitude = FoundationConstants.EMPTY_DOUBLE
        rangeRadius = FoundationConstants.ONE_DOUBLE
    }

    fun setFromPointOfInterest(pointOfInterest: PointOfInterestModel) {
        id = pointOfInterest.id
        latitude = pointOfInterest.latitude
        longitude = pointOfInterest.longitude
        rangeRadius = pointOfInterest.rangeRadius
        status = pointOfInterest.status
        createdAt = pointOfInterest.createdAt
        updatedAt = pointOfInterest.updatedAt
    }

    fun setFromLatLng(latLng: LatLng) {
        latitude = latLng.latitude
        longitude = latLng.longitude
        rangeRadius = 3.0
    }

    // Accessors
    val locationAsString : String
        get() = "$latitude, $longitude"

    // Transformations
    var rangeRadiusAsString = object: ObservableField<String>(rangeRadius.toString()) {
            override fun set(value: String?) {
                super.set(value)
                rangeRadius = value?.toDoubleOrNull() ?: rangeRadius
            }
        }

    // Static
    companion object {
        fun new() = PointOfInterestModel(FoundationConstants.EMPTY_LONG,
            FoundationConstants.EMPTY_DOUBLE,
            FoundationConstants.EMPTY_DOUBLE,
            FoundationConstants.ONE_DOUBLE,
            FoundationConstants.EMPTY_INT,
            null,
            null
        )
    }

}