package com.binarymonks.gonzo.core.time

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


fun nowUTC(clock: Clock):ZonedDateTime{
    return Instant.ofEpochMilli(clock.instant().toEpochMilli()).atZone(ZoneId.of("UTC"))
}