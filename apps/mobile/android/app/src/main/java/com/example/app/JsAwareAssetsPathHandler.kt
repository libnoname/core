package com.example.app

import android.content.Context
import android.os.Build
import android.webkit.MimeTypeMap
import android.webkit.WebResourceResponse
import androidx.annotation.RequiresApi
import androidx.webkit.WebViewAssetLoader
import java.io.IOException
import java.nio.file.Paths


class JsAwarePathHandler(private val context: Context, private val basePath: String?) :
    WebViewAssetLoader.PathHandler {
    override fun handle(path: String): WebResourceResponse? {
        try {
            val am = context.assets
            val assetPath = Paths.get(basePath, path).toString()
            val stream = am.open(assetPath)

            // 自动根据扩展名推断 MIME type
            val extension = MimeTypeMap.getFileExtensionFromUrl(path)
            var mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if ("js" == extension || "mjs" == extension) {
                mime = "application/javascript"
            }
            else if("html" == extension){
                mime = "text/html"
            }

            return WebResourceResponse(mime, "UTF-8", stream)
        } catch (e: IOException) {
            return null
        }
    }
}

