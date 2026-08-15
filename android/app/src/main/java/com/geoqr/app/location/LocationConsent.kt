package com.geoqr.app.location

/**
 * Explicit consent state for scanner-location collection.
 * The app must not submit scanner coordinates unless consent == Granted.
 */
enum class LocationConsent {
    NotAsked,
    Granted,
    Denied
}
