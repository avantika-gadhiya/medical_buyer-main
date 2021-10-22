package bluesharklabs.com.medical.activities

import android.os.Bundle
import android.webkit.WebViewClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import bluesharklabs.com.medical.R
import kotlinx.android.synthetic.main.content_webview_payment.*

class WebviewPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_payment)


        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.google.com")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

    }
}