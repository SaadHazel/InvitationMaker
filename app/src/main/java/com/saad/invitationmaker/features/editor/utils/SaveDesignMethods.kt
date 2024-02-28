package com.saad.invitationmaker.features.editor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import com.saad.invitationmaker.app.utils.log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SaveDesignMethods {
    fun getSaveImageFilePath(context: Context, view: View, folderName: String): String {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            folderName
        )
        // Create a storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                log("SavePdf", "Failed to create directory")
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageName = "IMG_$timeStamp.jpg"
        val selectedOutputPath = "${mediaStorageDir.path}${File.separator}$imageName"
        log("SavePdf", "selected camera path $selectedOutputPath\"")

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        val maxSize = 1080
        val bWidth = bitmap.width
        val bHeight = bitmap.height
        val scaledBitmap = if (bWidth > bHeight) {
            val imageHeight = (maxSize * (bWidth.toFloat() / bHeight)).toInt()
            Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true)
        } else {
            val imageWidth = (maxSize * (bWidth.toFloat() / bHeight)).toInt()
            Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true)
        }
        var fOut: FileOutputStream? = null
        try {
            val file = File(selectedOutputPath)
            fOut = FileOutputStream(file)
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fOut?.close()
        }
        return selectedOutputPath
    }


    fun getHighQualitySaveImageFilePath(context: Context, view: View): String? {
        val invitationMaker = "Invitation Maker" // Change this to your folder name

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            invitationMaker
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                log("SavePdf", "Failed to create directory")
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageName = "IMG_$timeStamp.jpg"
        val selectedOutputPath = mediaStorageDir.path + File.separator + imageName
        log("SavePdf", "selected camera path $selectedOutputPath\"")

        val bitmap = createBitmapFromView(view)
        var fos: FileOutputStream? = null
        try {
            val file = File(selectedOutputPath)
            fos = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            fos?.close()
            bitmap.recycle()
        }
        return selectedOutputPath
    }

    fun saveAsPDF(context: Context, view: View): String? {
        val YOUR_FOLDER_NAME = "Invitation Maker" // Change this to your folder name

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            YOUR_FOLDER_NAME
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                log("SavePdf", "Failed to create directory")
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val pdfName = "PDF_$timeStamp.pdf"
        val pdfFile = File(mediaStorageDir, pdfName)
        log("SavePdf", "Selected PDF path: ${pdfFile.absolutePath}")

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val bitmap = createBitmapFromView(view)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        return try {
            val fos = FileOutputStream(pdfFile)
            document.writeTo(fos)
            fos.close()
            document.close()
            pdfFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            bitmap.recycle()
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        // Create a bitmap with the same dimensions as the view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // Draw the view onto the canvas
        view.draw(canvas)
        return bitmap
    }


}