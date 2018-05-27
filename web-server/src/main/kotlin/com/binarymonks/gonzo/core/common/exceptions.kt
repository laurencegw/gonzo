package com.binarymonks.gonzo.core.common


class InvalidCredentials:Exception()
class ExpiredToken:Exception()
class UniqueConstraintException(val attributeName: String): Exception()
class NotAuthentic:Exception()
class NotAuthorized:Exception()