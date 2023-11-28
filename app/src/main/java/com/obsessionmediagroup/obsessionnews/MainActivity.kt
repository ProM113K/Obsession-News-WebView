package com.obsessionmediagroup.obsessionnews

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat

@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowInsetsCompat.Type.navigationBars()

        webView = findViewById(R.id.webView)

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = MyWebViewClient()

        // this will load the url of the website
        webView.loadUrl(WEB_URL)

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)
    }

    inner class MyWebViewClient : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (URLUtil.isNetworkUrl(url)) {
                return false
            } else if (url != null) {
                if (url.startsWith(prefix = "whatsapp:") || url.startsWith(prefix = "mailto:")) {
                    try {
                        val sendIntent = Intent().apply {
                            this.action = Intent.ACTION_SEND
                            this.putExtra(Intent.EXTRA_TEXT, url)
                            this.type = "text/plain"
                        }
                        startActivity(sendIntent)
                        Toast.makeText(this@MainActivity, "Mengirim berita", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Aplikasi belum terpasang",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(view, url)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val WEB_URL = "https://www.obsessionnews.com/"
    }
}