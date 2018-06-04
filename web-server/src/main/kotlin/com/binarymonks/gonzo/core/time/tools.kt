package com.binarymonks.gonzo.core.time

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

var clock: Clock = java.time.Clock.systemUTC()

fun nowUTC():ZonedDateTime{
    return Instant.ofEpochMilli(clock.instant().toEpochMilli()).atZone(ZoneId.of("UTC"))
}