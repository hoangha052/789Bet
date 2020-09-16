package com.app.bet789

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.app.bet789.databinding.ActivityInappBrowserBinding

class InAppBrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInappBrowserBinding
    private var currUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInappBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val url: String? = intent?.getStringExtra("link")
        if (!url.isNullOrEmpty()) {
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressBar.visibility = View.VISIBLE
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.visibility = View.GONE
                    super.onPageFinished(view, url)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?)
                        = handleFallbackUrl(request?.url?.toString())

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?) = handleFallbackUrl(url)
            }
            binding.webView.settings.setAppCacheEnabled(true)
            binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
            binding.webView.loadUrl(url)
        }

        binding.imgBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            }
        }
        binding.imgRefresh.setOnClickListener {
            if (!currUrl.isNullOrEmpty()) {
                binding.webView.reload()
            }
        }
        binding.imgForward.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        }
    }

    private fun handleFallbackUrl(url: String?): Boolean {
        currUrl = url

        if (url != null && url.startsWith("intent://")) {
            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            if (intent != null) {
                val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                if (fallbackUrl != null) {
                    binding.webView.loadUrl(fallbackUrl)
                    return true
                }
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_inapp, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.info -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://zalo.me/lucas8686"))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        fun getIntent(context: Context, link: String): Intent {
            val intent = Intent(context, InAppBrowserActivity::class.java)
            intent.putExtra("link", link)
            return intent
        }
    }
}