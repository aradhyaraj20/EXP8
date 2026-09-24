package com.example.exp8

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.LinearProgressIndicator

class WebViewFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        webView = view.findViewById(R.id.webView)

        swipeRefresh.setColorSchemeResources(R.color.primary, R.color.secondary)
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                } else {
                    progressBar.visibility = View.GONE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                // Keep toolbar clean
                (activity as? AppCompatActivity)?.supportActionBar?.title = "Web & Media Explorer"
                (activity as? AppCompatActivity)?.supportActionBar?.subtitle = ""
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                (activity as? AppCompatActivity)?.supportActionBar?.title = "Web & Media Explorer"
                (activity as? AppCompatActivity)?.supportActionBar?.subtitle = ""
                activity?.invalidateOptionsMenu()
            }
        }

        if (savedInstanceState == null) {
            loadLocalHtml()
        }

        return view
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    fun loadLocalHtml() {
        webView.loadUrl("file:///android_asset/sample.html")
    }

    fun reload() {
        webView.reload()
    }

    fun canGoBack(): Boolean = ::webView.isInitialized && webView.canGoBack()

    fun goBack() {
        if (canGoBack()) webView.goBack()
    }

    fun canGoForward(): Boolean = ::webView.isInitialized && webView.canGoForward()

    fun goForward() {
        if (canGoForward()) webView.goForward()
    }
}
