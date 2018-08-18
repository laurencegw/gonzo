package com.binarymonks.gonzo.web.controllers

import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class SPAProxyController {

    @RequestMapping(
            "/home/**",
            "/article/**"
    )
    fun forward(model: ModelMap): ModelAndView {
        return ModelAndView("forward:/")
    }
}