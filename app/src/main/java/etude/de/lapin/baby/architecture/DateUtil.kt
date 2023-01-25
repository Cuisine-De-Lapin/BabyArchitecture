package etude.de.lapin.baby.architecture

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toTimestamp() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId())