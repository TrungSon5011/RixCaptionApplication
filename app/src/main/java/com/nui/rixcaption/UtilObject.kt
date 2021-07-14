package com.nui.rixcaption

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

object UtilObject {
    fun copyFontToInternalStorage(resourceId: Int, resourceName: String, context: Context): File {
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "RixC" + File.separator
        val folder = File(path)
        if (!folder.exists())
            folder.mkdirs()

        val dataPath = "$path$resourceName.ttf"
        Log.v("OptiUtils", "path: $dataPath")
        try {

            val inputStream = context.resources.openRawResource(resourceId)
            inputStream.toFile(dataPath)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return File(dataPath)
    }
    private fun InputStream.toFile(path: String) {
        File(path).outputStream().use {
            this.copyTo(it) }
    }


}