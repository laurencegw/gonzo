package com.binarymonks.gonzo.core.common


sealed class Credentials

class TokenCredentials(val token:String): Credentials()
class AnonymousCredentials: Credentials()

