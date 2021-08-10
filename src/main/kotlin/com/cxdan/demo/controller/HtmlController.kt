package com.cxdan.demo.controller

import com.cxdan.demo.FileUtils
import org.springframework.stereotype.Controller
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class HtmlController {
    var filePathParent = ""

    @GetMapping("/index")
    open fun home(): String {
        println("home")
        return "index"
    }

    @GetMapping("/show")
    open fun show(
        @RequestParam(name = "sourcePath", required = false) sourcePath: String,
        response: HttpServletResponse
    ): Unit {
        val sourcePath2 = checkPath(sourcePath)
        rememberDir(sourcePath2)
        println("sourcePath" + sourcePath2)
        FileUtils.readHtmlFromPath(sourcePath2, response.outputStream)
    }

    @GetMapping("/preview/**", "/assets/**", "/links/**")
    open fun pageRequest(request: HttpServletRequest, response: HttpServletResponse) {
        println(request.servletPath)
        val filePath = filePathParent + request.servletPath
        FileUtils.readFileFromPath(filePath, response.outputStream)
    }

    @GetMapping("/adb/screenCap/")
    open fun screenCap(request: HttpServletRequest, response: HttpServletResponse) {
        println(request.servletPath)
        response.outputStream.write(FileUtils.getScreen("devices"))
    }

    fun checkPath(path: String): String {
        val target: Char = '\u202A'
        if (StringUtils.isEmpty(path)) {
            return path

        } else {
            var toCharArray = path.toCharArray()
            if (toCharArray[0] == target) {
                return String(toCharArray.sliceArray(1..toCharArray.lastIndex))
            }
        }
        return path
    }

    fun rememberDir(path: String) {
        val file = File(path)
        filePathParent = if (file.isDirectory) {
            file.absolutePath
        } else {
            file.toPath().parent.toString()
        }
    }
}