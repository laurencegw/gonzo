package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.common.NotAuthentic
import com.binarymonks.gonzo.core.common.NotAuthorized
import com.binarymonks.gonzo.core.common.ValidationException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate


fun restTemplateWithErrorHandler(): RestTemplate{
    val restTemplate = RestTemplate()
    restTemplate.errorHandler = CustomErrorHandler()
//    val converter = MappingJackson2HttpMessageConverter()
//    converter.supportedMediaTypes = mutableListOf(MediaType.APPLICATION_JSON)
//    restTemplate.messageConverters.add(converter)
    return restTemplate
}

class CustomErrorHandler: DefaultResponseErrorHandler() {

    override fun handleError(response: ClientHttpResponse?) {
        val statusCode = response!!.rawStatusCode
        when(statusCode){
            401 -> {throw NotAuthentic()}
            403 -> {throw NotAuthorized()}
            422 -> {
                val body = response.body
                val string = body.bufferedReader().use { it.readText() }
                val mapper = ObjectMapper()
                val validationException = mapper.readValue(string, ValidationException::class.java)
                throw validationException
            }
            else -> super.handleError(response)
        }
    }

}