package com.home.smarthomeserver

import com.home.smarthomeserver.awsconfig.AwsS3Client
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ImagesController {

    @ResponseBody
    @GetMapping(path = ["images"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getImage(req: HttpServletRequest, res: HttpServletResponse): String {
        res.characterEncoding = Charsets.UTF_8.name()
        return AwsS3Client.getSignedUrl().toString()
    }
}