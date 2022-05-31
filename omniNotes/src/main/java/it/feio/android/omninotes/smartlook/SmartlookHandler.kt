package it.feio.android.omninotes.smartlook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.smartlook.android.core.api.Session
import com.smartlook.android.core.api.Smartlook
import com.smartlook.android.core.api.User
import com.smartlook.android.core.api.enumeration.Status
import com.smartlook.android.core.api.extension.isSensitive
import com.smartlook.android.core.api.model.Properties
import com.smartlook.android.core.api.model.RecordingMask
import com.smartlook.android.core.api.model.Referrer
import com.smartlook.android.util.logging.annotation.LogAspect
import it.feio.android.omninotes.R
import it.feio.android.omninotes.SensitivityPlayground
import java.net.URL


object SmartlookHandler {

    private lateinit var instance: Smartlook

    /**
     * Called in Application class [OmniNotes] in onCreate() method.
     * @param application Application context
     */
    @JvmStatic
    fun onApplicationCreate() {

        // Obtain Smartlook instance
        instance = Smartlook.instance

        // Enable all logging
        instance.log.allowedLogAspects = LogAspect.ALL

        // Set project key
        instance.preferences.projectKey = "187b9f5b05200261f86fddd57f27f9a38c0584d0"

        // Start recording
        instance.start()

        // Open new session
        instance.user.session.openNew()

        // Set test referrer
        instance.referrer = Referrer("test_referrer", "test_source")

        // Set sample "global" event properties
        instance.eventProperties
                .putString("global_property", "test")

        /**
         * User API.
         */
        with(instance.user) {

            // Set current date and time as user identifier
            identifier = DateUtil.currentDateTimeString()

            // Set User properties
            email = "test@test.com"
            name = "John Tester"
            properties
                    .putString("custom_property", "test")

            // Listen to user URL change
            listeners += object : User.Listener {
                override fun onUrlChanged(url: URL) {
                    Log.d("Smartlook", "User.Listener: onUrlChanged: $url")
                }
            }

            // Listen to session URL change
            session.listeners += object : Session.Listener {
                override fun onUrlChanged(url: URL) {
                    Log.d("Smartlook", "Session.Listener: onUrlChanged: $url")
                }
            }


            Smartlook.instance.user.listeners += object : User.Listener {
                override fun onUrlChanged(url: URL) {

                }
            }

            Smartlook.instance.user.session.listeners += object : Session.Listener {
                override fun onUrlChanged(url: URL) {
                    val a = url.toString()
                }
            }

        }

        /**
         * Preferences API.
         */
        with(instance.preferences) {
            //framerate = 2
            //renderingMode = RenderingMode.NATIVE
            //renderingModeOption = RenderingModeOption.WIREFRAME
            //isAdaptiveFramerateEnabled = true
            //isSurfaceCaptureEnabled = true
            //isUploadUsingAndroidJobsEnabled = true
        }

        /**
         * Event tracking API.
         */
        with(instance.preferences.eventTracking) {

            // Global
            //enableAll()
            //disableAll()
            //default()

            // Navigation
            //navigation.enableAll()
            //navigation.disableAll()
            //navigation.isActivityEnabled = true
            //navigation.isFragmentEnabled = true

            // Navigation - activities/fragments list
            //navigation.disabledActivityClasses += PasswordActivity::class
            //navigation.disabledFragmentClasses += DetailFragment::class

            // Interaction
            //interaction.enableAll()
            //interaction.disableAll()
            //interaction.isRageClickEnabled = true
            //interaction.isSelectorEnabled = true
            //interaction.isTouchEnabled = true
        }
    }

    /**
     * Called when FAB itself is clicked.
     */
    @JvmStatic
    fun onFabClick() {
        //instance.preferences.projectKey = "187b9f5b05200261f86fddd57f27f9a38c0584d0"
        Smartlook.instance.trackEvent("fab_click")
    }

    /**
     * Called when one of FAB items is clicked.
     */
    @JvmStatic
    fun onFabItemClick(context: Context?, id: Int) {
        when(id) {

            /**
             * "Photo" item clicked.
             */
            R.id.fab_camera -> {
                Smartlook.instance.trackEvent("fab_camera_item_click")
                //Smartlook.instance.stop()
                //Smartlook.instance.reset()
            }

            /**
             * "Checklist" item clicked.
             */
            R.id.fab_checklist -> {
                val props = Properties()
                        .putString("id", id.toString())
                        .putString("test", "test")

                Smartlook.instance.trackEvent("fab_checklist_item_click", props)
            }

            /**
             * "Text note" item clicked.
             */
            R.id.fab_note -> {
                Smartlook.instance.trackEvent("fab_text_note_item_click")
            }

            /**
             * Smartlook custom. List all preferences.
             */
            R.id.fab_all_preferences -> {
                with(Smartlook.instance.state) {
                    Log.d("Smartlook", "State\n" +
                            "isRecording: ${status is Status.Recording}\n" +
                            "status = ${status.javaClass.simpleName}\n" +
                            "cause = ${if (status is Status.NotRecording) (status as Status.NotRecording).cause else "-"}\n" +
                            "projectKey = $projectKey\n" +
                            "frameRate = $frameRate\n" +
                            "renderingMode = ${renderingMode.name}\n" +
                            "renderingModeOption = ${renderingModeOption?.name}\n" +
                            "isAdaptiveFrameRateEnabled = $isAdaptiveFrameRateEnabled\n" +
                            "isSurfaceCaptureEnabled = $isSurfaceCaptureEnabled\n" +
                            "isUploadUsingAndroidJobsEnabled = $isUploadUsingAndroidJobsEnabled\n" +
                            "eventTracking.navigation.isActivityEnabled: ${eventTracking.navigation.isActivityEnabled}\n" +
                            "eventTracking.navigation.isFragmentEnabled: ${eventTracking.navigation.isFragmentEnabled}\n" +
                            "eventTracking.interaction.isTouchEnabled: ${eventTracking.interaction.isTouchEnabled}\n" +
                            "eventTracking.interaction.isSelectorEnabled: ${eventTracking.interaction.isSelectorEnabled}\n" +
                            "eventTracking.interaction.isRageClickEnabled: ${eventTracking.interaction.isRageClickEnabled}" )
                }
            }
            R.id.fab_sensitivity_playground -> {
                context?.let {
                    it.startActivity(Intent(it, SensitivityPlayground::class.java))
                }
            }
        }
    }

    /**
     * Called in [MainActivity] on UI initialization. Covers whole Toolbar.
     */
    @JvmStatic
    fun onMainActivityUIInit(activity: Activity, toolbar: Toolbar) {
        toolbar.run {
            Smartlook.instance.sensitivity.recordingMask = RecordingMask(
                listOf(
                    RecordingMask.Element(
                        activity.window.decorView.getRectOnScreen(),
                        RecordingMask.Element.Type.COVERING
                    ),
                    RecordingMask.Element(
                        toolbar.getRectOnScreen(),
                        RecordingMask.Element.Type.ERASING
                    )
                )
            )
        }
    }

    /**
     * Called in [SublimePickerFragment] when Ok is clicked.
     */
    @JvmStatic
    fun onDateTimePickerSet() {
        Smartlook.instance.trackEvent("set_date_time")
    }

    /**
     * Called when user clicks "Empty trash" in menu.
     */
    @JvmStatic
    fun onEmptyTrash() {
        //Smartlook.instance.user.session.openNew()
    }

    /**
     * Called when SensitivityPlayground is entered.
     */
    @SuppressLint("SetJavaScriptEnabled")
    @JvmStatic
    fun onSensitivityPlayground(context: Activity) {

        // Views binding
        val buttonA = context.findViewById<Button>(R.id.buttonA)
        val buttonB = context.findViewById<Button>(R.id.buttonB)
        val imageViewA = context.findViewById<ImageView>(R.id.imageViewA)
        val imageViewB = context.findViewById<ImageView>(R.id.imageViewB)
        val textView = context.findViewById<TextView>(R.id.textView)
        val editText = context.findViewById<EditText>(R.id.editText)
        val webView = context.findViewById<WebView>(R.id.webView)

        // Remove edit text from sensitive classes
        EditText::class.isSensitive = false

        // Mark imageViewA as sensitive
        imageViewA.isSensitive = true

        // Mark all buttons to be sensitive but "whitelist" buttonA
        Button::class.isSensitive = true
        buttonA.isSensitive = false

        // Load WebView with html that has sensitive elements
        val html = """
            <!DOCTYPE html>
            <html>
            
            <head>
              <title>Our Company</title>
            </head>
            
            <body>
            
              <h1>Welcome to Our Company</h1>
              <h2>Web Site Main Ingredients:</h2>
            
              <p class='smartlook-hide'>Pages (HTML)</p>
              <p>Style Sheets (CSS)</p>
              <p class='smartlook-hide'>Computer Code (JavaScript)</p>
              <p>Live Data (Files and Databases)</p>
            
            </body>
            </html>
        """.trimIndent()

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadData(html, "text/html", "UTF-8");
    }
}