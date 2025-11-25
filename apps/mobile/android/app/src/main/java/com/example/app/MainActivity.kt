package com.example.app

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import com.getcapacitor.BridgeActivity

class MainActivity : BridgeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = bridge.webView

        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("localhost")
            .addPathHandler("/", JsAwarePathHandler(this, "public"))
            .build()

        webView.webViewClient = LocalContentWebViewClient(assetLoader)

        webView.loadUrl("https://localhost/index.html")
    }

    private class LocalContentWebViewClient(
        private val assetLoader: WebViewAssetLoader
    ) : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            return if (request.url.host == "localhost") {
                assetLoader.shouldInterceptRequest(request.url) ?: super.shouldInterceptRequest(view, request)
            } else {
                super.shouldInterceptRequest(view, request)
            }
        }
    }
}

