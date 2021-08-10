package com.cxdan.demo

import java.io.*
import javax.servlet.ServletOutputStream


object FileUtils {
    fun readHtmlFromPath(path: String, outputStream: ServletOutputStream) {
        val file = File(path)
        var br: BufferedReader? = null
        br = BufferedReader(FileReader(file))
        var content: String? = null
        var i = 1
        try {
            while (true) {
                content = br.readLine()
                if (content == null) {
                    break
                }
                outputStream.write(content.toString().toByteArray())
                outputStream.flush()
                if (content.contains("<body id=\"app\"></body>")) {
                    outputStream.write("<h1 id=\"test\"></h1>".toByteArray())
                    outputStream.flush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream.let {
                outputStream.close()
            }
        }
    }

    fun readFileFromPath(path: String, outputStream: ServletOutputStream) {
        val file = File(path)
        var br: DataInputStream = DataInputStream(FileInputStream(file))
        try {
            val buffer = ByteArray(br.available())
            br.read(buffer)
            br.close()
            outputStream.write(buffer)
            outputStream.flush()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream.let {
                outputStream.close()
            }
        }
    }

    fun getScreen(device: String?): ByteArray? {
        var imageBytes: ByteArray? = null
        try {
            val out = ByteArrayOutputStream()
            val process = Runtime.getRuntime().exec("adb exec-out screencap -p")
            val inputStream = process.inputStream
            val buffer = ByteArray(1048576)
            val start = System.currentTimeMillis()
            var length: Int
            var totalSize: Long = 0L
            while (inputStream.read(buffer).also { length = it } != -1) {
                out.write(buffer, 0, length)
                totalSize += length.toLong()
            }
            imageBytes = out.toByteArray()
            val time = System.currentTimeMillis() - start
            println("截图 图片大小：$totalSize 耗时：$time")
            inputStream.close()
            out.close()
            process.destroy()
        } catch (var14: IOException) {
            var14.printStackTrace()
        }
        return imageBytes
    }

}