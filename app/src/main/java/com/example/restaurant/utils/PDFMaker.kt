package com.example.restaurant.utils

import com.itextpdf.html2pdf.HtmlConverter
import com.itextpdf.io.IOException
import com.itextpdf.io.source.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

object PDFMaker {
    fun generatePdf(content: String, outputPath: String) {
        try {
            val outputStream = ByteArrayOutputStream()
            HtmlConverter.convertToPdf(content, outputStream)

            savePdf(outputStream.toByteArray(), outputPath)

            println("PDF успешно создан по пути: $outputPath")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

   private fun savePdf(pdfBytes: ByteArray, outputPath: String) {
        try {
            val fileOutputStream = FileOutputStream(File(outputPath))
            fileOutputStream.write(pdfBytes)
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}