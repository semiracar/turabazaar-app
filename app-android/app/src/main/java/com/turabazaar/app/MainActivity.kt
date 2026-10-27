package com.turabazaar.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.turabazaar.app.push.TurabazaarMessagingService

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar
    private lateinit var refresh: SwipeRefreshLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Turabazaar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        progress = findViewById(R.id.progress)
        refresh = findViewById(R.id.refresh)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.userAgentString = settings.userAgentString + " TurabazaarApp/1.0"

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                progress.visibility = View.GONE
                refresh.isRefreshing = false
                // Push-Token an die Webseite melden (wird beim Login vom Server gebraucht)
                TurabazaarMessagingService.pushToken?.let { token ->
                    view?.evaluateJavascript(
                        "window.dispatchEvent(new CustomEvent('tura-push-token',{detail:'$token'}));" +
                        "if(window.TuraPush&&window.TuraPush.setToken)window.TuraPush.setToken('$token');", null)
                }
            }
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                return when {
                    url.startsWith("https://turabazaar.com") || url.startsWith("http://turabazaar.com") -> false
                    url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("whatsapp:") -> {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))); true
                    }
                    else -> { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))); true }
                }
            }
        }
        webView.webChromeClient = WebChromeClient()

        refresh.setOnRefreshListener { webView.reload() }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val url = intent?.dataString ?: getString(R.string.web_url)
        if (webView.url != url) webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
