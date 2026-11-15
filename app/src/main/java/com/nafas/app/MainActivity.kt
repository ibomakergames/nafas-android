package com.nafas.app
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
class MainActivity : Activity() {
 private lateinit var webView: WebView
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
   ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
  }
  webView = WebView(this)
  webView.settings.javaScriptEnabled = true
  webView.settings.domStorageEnabled = true
  webView.settings.allowFileAccess = true
  webView.webViewClient = WebViewClient()
  webView.webChromeClient = WebChromeClient()
  webView.addJavascriptInterface(NafasBridge(this), "Nafas")
  setContentView(webView)
  webView.loadUrl("file:///android_asset/index.html")
 }
 @Deprecated("Deprecated in Android API, retained for compatibility")
 override fun onBackPressed() {
  if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
 }
}
class NafasBridge(private val context: Context) {
 @JavascriptInterface fun setWaterReminder(enabled: Boolean, intervalMinutes: Int) {
  val wm = WorkManager.getInstance(context)
  if (!enabled) { wm.cancelUniqueWork("nafas_water_reminder"); return }
  val minutes = intervalMinutes.coerceIn(15, 240)
  val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(minutes.toLong(), TimeUnit.MINUTES).build()
  wm.enqueueUniquePeriodicWork("nafas_water_reminder", ExistingPeriodicWorkPolicy.UPDATE, request)
 }
}
