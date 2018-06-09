package com.binarymonks.gonzo.clients

import com.binarymonks.gonzo.core.common.NotAuthentic
import com.binarymonks.gonzo.core.common.NotAuthorized
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
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
            else -> super.handleError(response)
        }
    }

}