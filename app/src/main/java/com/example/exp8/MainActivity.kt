package com.example.exp8

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private var activeFragmentTag = TAG_WEB_VIEW

    companion object {
        private const val TAG_WEB_VIEW = "web_view_fragment"
        private const val TAG_IMAGE_GRID = "image_grid_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        val bottomNav = findViewById<NavigationBarView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_webview -> {
                    showFragment(TAG_WEB_VIEW)
                    true
                }
                R.id.nav_gridview -> {
                    showFragment(TAG_IMAGE_GRID)
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            val webViewFragment = WebViewFragment()
            val imageGridFragment = ImageGridFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, webViewFragment, TAG_WEB_VIEW)
                .add(R.id.fragmentContainer, imageGridFragment, TAG_IMAGE_GRID)
                .hide(imageGridFragment)
                .commit()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val webViewFrag = supportFragmentManager.findFragmentByTag(TAG_WEB_VIEW) as? WebViewFragment
                if (activeFragmentTag == TAG_WEB_VIEW && webViewFrag?.canGoBack() == true) {
                    webViewFrag.goBack()
                } else if (activeFragmentTag != TAG_WEB_VIEW) {
                    bottomNav.selectedItemId = R.id.nav_webview
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun showFragment(tag: String) {
        val webViewFrag = supportFragmentManager.findFragmentByTag(TAG_WEB_VIEW)
        val imageGridFrag = supportFragmentManager.findFragmentByTag(TAG_IMAGE_GRID)

        val transaction = supportFragmentManager.beginTransaction()

        if (tag == TAG_WEB_VIEW) {
            if (webViewFrag != null) transaction.show(webViewFrag)
            if (imageGridFrag != null) transaction.hide(imageGridFrag)
        } else {
            if (imageGridFrag != null) transaction.show(imageGridFrag)
            if (webViewFrag != null) transaction.hide(webViewFrag)
        }

        transaction.commit()
        activeFragmentTag = tag
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isWebViewActive = activeFragmentTag == TAG_WEB_VIEW
        menu?.findItem(R.id.menu_back)?.isVisible = isWebViewActive
        menu?.findItem(R.id.menu_forward)?.isVisible = isWebViewActive
        menu?.findItem(R.id.menu_refresh)?.isVisible = isWebViewActive
        menu?.findItem(R.id.menu_websites)?.isVisible = isWebViewActive

        val webViewFrag = supportFragmentManager.findFragmentByTag(TAG_WEB_VIEW) as? WebViewFragment
        if (isWebViewActive && webViewFrag != null) {
            menu?.findItem(R.id.menu_back)?.isEnabled = webViewFrag.canGoBack()
            menu?.findItem(R.id.menu_forward)?.isEnabled = webViewFrag.canGoForward()
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val webViewFrag = supportFragmentManager.findFragmentByTag(TAG_WEB_VIEW) as? WebViewFragment
        val imageGridFrag = supportFragmentManager.findFragmentByTag(TAG_IMAGE_GRID) as? ImageGridFragment

        return when (item.itemId) {
            R.id.menu_google -> {
                switchToWebView()
                webViewFrag?.loadUrl("https://www.google.com")
                true
            }
            R.id.menu_wikipedia -> {
                switchToWebView()
                webViewFrag?.loadUrl("https://www.wikipedia.org")
                true
            }
            R.id.menu_youtube -> {
                switchToWebView()
                webViewFrag?.loadUrl("https://www.youtube.com")
                true
            }
            R.id.menu_github -> {
                switchToWebView()
                webViewFrag?.loadUrl("https://www.github.com")
                true
            }
            R.id.menu_offline_html -> {
                switchToWebView()
                webViewFrag?.loadLocalHtml()
                true
            }
            R.id.menu_refresh -> {
                webViewFrag?.reload()
                true
            }
            R.id.menu_back -> {
                if (webViewFrag?.canGoBack() == true) webViewFrag.goBack()
                true
            }
            R.id.menu_forward -> {
                if (webViewFrag?.canGoForward() == true) webViewFrag.goForward()
                true
            }
            R.id.action_select_all -> {
                imageGridFrag?.selectAll()
                Toast.makeText(this, "All images selected!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_edit -> {
                Toast.makeText(this, "Action: Edit triggered", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share -> {
                Toast.makeText(this, "Action: Share triggered", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_dns_help -> {
                showDnsHelpDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchToWebView() {
        if (activeFragmentTag != TAG_WEB_VIEW) {
            findViewById<NavigationBarView>(R.id.bottomNavigation).selectedItemId = R.id.nav_webview
        }
    }

    private fun showDnsHelpDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Emulator Network Access & DNS")
            .setMessage("If your Android Emulator cannot connect to external web URLs:\n\n1. Go to Emulator Extended Controls (...).\n2. Select Settings -> Network.\n3. Set DNS Servers to 8.8.8.8 or 8.8.4.4.\n\nAlternatively, use the 'Local HTML (Offline)' option in the Bookmarks menu to test offline web view rendering!")
            .setPositiveButton("OK", null)
            .show()
    }
}
