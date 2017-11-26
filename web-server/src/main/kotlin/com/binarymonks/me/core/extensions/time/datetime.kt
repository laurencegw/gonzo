package com.binarymonks.me.core.extensions.time

import java.time.ZoneOffset
import java.time.ZonedDateTime


fun ZonedDateTime.normalise():ZonedDateTime = this.toInstant().atZone(ZoneOffset.UTC)
