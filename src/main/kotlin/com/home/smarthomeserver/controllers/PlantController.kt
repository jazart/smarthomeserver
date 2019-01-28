package com.home.smarthomeserver.controllers

import com.home.smarthomeserver.MoistureReading
import com.home.smarthomeserver.PlantMoisture
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController class PlantController {

    @RequestMapping("/plant/{id}", method = [RequestMethod.GET])
    fun sendPlantStatus(@PathVariable("id") id: String) = PlantMoisture()
}