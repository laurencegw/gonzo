package com.binarymonks.gonzo.core.common

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


class InvalidCredentials:Exception()
class ExpiredToken:Exception()
class UniqueConstraintException(val attributeName: String): Exception()
class NotAuthentic:Exception()
class NotAuthorized:Exception()
class NotFound: Exception()

data class ValidationMessages @JsonCreator constructor(
        @JsonProperty("validationMessages")
        var messages: MutableList<ValidationMessage>
)
data class ValidationMessage @JsonCreator constructor(
        @JsonProperty("path")
        var path: String,
        @JsonProperty("message")
        var message: String
)
class ValidationException @JsonCreator constructor(
        var validationMessages:ValidationMessages
): Exception()