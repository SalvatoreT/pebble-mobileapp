package dev.sal.libpebble3consumer

import io.rebble.libpebblecommon.connection.PebbleConnectionEvent
import io.rebble.libpebblecommon.connection.PebbleDevice
import io.rebble.libpebblecommon.connection.PebbleIdentifier
import io.rebble.libpebblecommon.connection.Scanning
import io.rebble.libpebblecommon.connection.Watches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

object LibPebbleUsage {
    private val apiTypes: List<String> = listOf(
        Watches::class.simpleName,
        Scanning::class.simpleName,
        PebbleDevice::class.simpleName,
        PebbleConnectionEvent::class.simpleName,
        PebbleIdentifier::class.simpleName,
    ).map { it ?: "unknown" }

    fun describe(): String = "Consuming libpebble3 connection API: ${apiTypes.joinToString()}"

    @Suppress("unused")
    fun touchMembers(watches: Watches, scanning: Scanning): String {
        val devices: StateFlow<List<PebbleDevice>> = watches.watches
        val events: Flow<PebbleConnectionEvent> = watches.connectionEvents
        val scanningBle: StateFlow<Boolean> = scanning.isScanningBle
        val id: PebbleIdentifier? = devices.value.firstOrNull()?.identifier
        scanning.startBleScan()
        return "watches=${devices.value.size} scanningBle=${scanningBle.value} " +
            "firstId=${id?.asString} events=$events"
    }
}
