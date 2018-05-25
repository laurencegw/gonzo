package com.binarymonks.gonzo.core.common


class InvalidCredentials:Exception()
class ExpiredToken:Exception()
class UniqueConstrainteException(val attributeName: String): Exception()