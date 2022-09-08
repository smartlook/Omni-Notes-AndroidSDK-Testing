package it.feio.android.omninotes.smartlook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
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
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timerTask


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
        instance.preferences.renderingMode = RenderingMode.NO_RENDERING

        // Start recording
        instance.start()

        Handler(Looper.getMainLooper()).postDelayed({
            testState()
        }, 10)


        // Open new session
        //instance.user.session.openNew()

        // Set test referrer
        //instance.referrer = Referrer("test_referrer", "test_source")

        // Set sample "global" event properties
        //instance.eventProperties
//            .putString("global_property", "test")

        /**
         * User API.
         */
        with(instance.user) {

            // Set current date and time as user identifier
            identifier = "new wireframe testing"

            // Set User properties
//            email = "test@test.com"
//            name = "John Tester"
//            properties
//                .putString("custom_property", "test")

            // Listen to user URL change
//            listeners += object : User.Listener {
//                override fun onUrlChanged(url: URL) {
//                    Log.d("Smartlook", "User.Listener: onUrlChanged: $url")
//                }
//            }

            // Listen to session URL change
//            session.listeners += object : Session.Listener {
//                override fun onUrlChanged(url: URL) {
//                    Log.d("Smartlook", "Session.Listener: onUrlChanged: $url")
//                }
//            }


//            Smartlook.instance.user.listeners += object : User.Listener {
//                override fun onUrlChanged(url: URL) {
//
//                }
//            }

//            Smartlook.instance.user.session.listeners += object : Session.Listener {
//                override fun onUrlChanged(url: URL) {
//                    val a = url.toString()
//                }
//            }

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

        launchRenderingModeCycle()
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
        when (id) {

            /**
             * "Photo" item clicked.
             */
            R.id.fab_camera -> {
                // Test case MST-247
                Smartlook.instance.trackEvent("fab_camera_item_click")
                //Smartlook.instance.stop()
                //Smartlook.instance.reset()
            }

            /**
             * "Checklist" item clicked.
             */
            // Test case MST-183 for local properties
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
                    Log.d(
                        "Smartlook", "State\n" +
                                "isRecording: ${status is Status.Recording}\n" +
                                "status = ${status.javaClass.simpleName}\n" +
                                "cause = ${if (status is Status.NotRecording) (status as Status.NotRecording).cause else "-"}\n" +
                                "projectKey = $projectKey\n" +
                                "frameRate = $frameRate\n" +
                                "renderingMode = ${renderingMode.name}\n" +
                                "renderingModeOption = ${renderingModeOption?.name}\n" +
                                "isAdaptiveFrameRateEnabled = $isAdaptiveFrameRateEnabled\n" +
                                "isSurfaceCaptureEnabled = $isSurfaceRecordingEnabled\n" +
                                "isUploadUsingAndroidJobsEnabled = $isUploadUsingAndroidJobsEnabled\n" +
                                "eventTracking.navigation.isActivityEnabled: ${eventTracking.navigation.isActivityEnabled}\n" +
                                "eventTracking.navigation.isFragmentEnabled: ${eventTracking.navigation.isFragmentEnabled}\n" +
                                "eventTracking.interaction.isTouchEnabled: ${eventTracking.interaction.isTouchEnabled}\n" +
                                "eventTracking.interaction.isSelectorEnabled: ${eventTracking.interaction.isSelectorEnabled}\n" +
                                "eventTracking.interaction.isRageClickEnabled: ${eventTracking.interaction.isRageClickEnabled}"
                    )
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
            Smartlook.instance.recordingMask = RecordingMask(
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

    private val renderingModes = listOf(
        RenderingMode.WIREFRAME,
        RenderingMode.NO_RENDERING,
        RenderingMode.NATIVE,
        RenderingMode.WIREFRAME,
        RenderingMode.NATIVE,
        RenderingMode.NO_RENDERING,
        RenderingMode.WIREFRAME
    )
    private var currentIndex = 1

    private fun launchRenderingModeCycle() {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(20 * 1_000)
                val mode = renderingModes[currentIndex]
                Smartlook.instance.preferences.renderingMode = mode
                Smartlook.instance.trackEvent("renderingMode${mode.name}")
                currentIndex += 1
                if (currentIndex > renderingModes.count().dec()) {
                    currentIndex = 0
                }
            }
        }
    }

    // SMARTLOOK TESTING METHODS

    fun testState() {
        with(Smartlook.instance.state) {
            Log.d(
                "Smartlook", "State\n" +
                        "isRecording: ${status.isRecording}\n" +
                        "status = ${status.javaClass.simpleName}\n" +
                        "cause = ${if (status is Status.NotRecording) (status as Status.NotRecording).cause else "-"}\n" +
                        "projectKey = $projectKey\n" +
                        "frameRate = $frameRate\n" +
                        "renderingMode = ${renderingMode.name}\n" +
                        "renderingModeOption = ${renderingModeOption?.name}\n" +
                        "isAdaptiveFrameRateEnabled = $isAdaptiveFrameRateEnabled\n" +
                        "isSurfaceCaptureEnabled = $isSurfaceRecordingEnabled\n" +
                        "isUploadUsingAndroidJobsEnabled = $isUploadUsingAndroidJobsEnabled\n" +
                        "eventTracking.navigation.isActivityEnabled: ${eventTracking.navigation.isActivityEnabled}\n" +
                        "eventTracking.navigation.isFragmentEnabled: ${eventTracking.navigation.isFragmentEnabled}\n" +
                        "eventTracking.interaction.isTouchEnabled: ${eventTracking.interaction.isTouchEnabled}\n" +
                        "eventTracking.interaction.isSelectorEnabled: ${eventTracking.interaction.isSelectorEnabled}\n" +
                        "eventTracking.interaction.isRageClickEnabled: ${eventTracking.interaction.isRageClickEnabled}"
            )
        }
    }

    // Test cases MST-254, MST-256
    fun testUserProperties() {
        Smartlook.instance.user.identifier = "testUserProperties"

        Smartlook.instance.user.email = "john.doe@smartlook.com"
        Smartlook.instance.user.name = "John Doe"
        Smartlook.instance.user.properties.putString("gender", "male");
    }

    // Test case MST-256
    fun testUserOpenNew() {
        Smartlook.instance.user.identifier = "testUserOpenNew"

        Smartlook.instance.user.openNew()
    }

    // Test cases MST-253, MST-327, MST-318
    fun testUserIdentifier() {
        Smartlook.instance.user.identifier =
            "this string has more than one hundred and twenty characters and should not be set... but I have to make it longer to see if there is an error"

        Smartlook.instance.user.identifier = "mtestUserIdentifier"
    }

    // Test cases MST-334
    fun testPreferencesProjectKeyFailScenario() {
        Smartlook.instance.user.identifier = "testPreferencesProjectKeyFailScenario"

        Smartlook.instance.preferences.projectKey = "projectKey"
        Smartlook.instance.preferences.projectKey = "a76b285a70ecfb2b2cc13a13b0be2de6b60acf99aaa"
        Smartlook.instance.preferences.projectKey = "a76b285a70ecfb2b2cc13a13b0be2de6b60acf*9"
    }

    // Test case MST-214, MST-298
    fun testPreferencesProjectKey() {
        Smartlook.instance.user.identifier = "testPreferencesProjectKey"

        Smartlook.instance.preferences.projectKey = "187b9f5b05200261f86fddd57f27f9a38c0584d0"
        testState()
    }

    // Test case MST-264, MST-292
    fun testSetFrameRate(framerate: Int) {
        Smartlook.instance.user.identifier = "testSetFrameRate $framerate"

        Smartlook.instance.preferences.frameRate = framerate
        testState()
    }

    // Test cases MST-265, MST-266, MST-267, MST-296
    fun testRenderingModes() {
        Smartlook.instance.user.identifier = "testRenderingModes"

        data class Schedule(val time: Long = 0, val renderingMode: RenderingMode)

        arrayOf(
            Schedule(0, RenderingMode.WIREFRAME),
            Schedule(20000, RenderingMode.NO_RENDERING),
            Schedule(40000, RenderingMode.NATIVE)
        )
            .forEach {
                Timer("SettingUp", false).schedule(timerTask {
                    Smartlook.instance.preferences.renderingMode = it.renderingMode
                    Smartlook.instance.trackEvent("Rendering mode change")
                }, it.time)
                testState()
            }
    }

    // Test cases MST-202, MST-203
    fun testSessionOpenNew() {
        Smartlook.instance.user.identifier = "testSessionOpenNew"

        Smartlook.instance.user.session.openNew()
    }

    // Test case MST-183 global properties + local properties
    fun testEventProperties() {
        Smartlook.instance.user.identifier = "testEventProperties"

        val firstPropertyKey = "firstProperty"
        val firstPropertyValue = "some value"

        val secondPropertyKey = "secondProperty"
        val secondPropertyValue = "another property value"

        Smartlook.instance.eventProperties.putString(firstPropertyKey, firstPropertyValue)
        Smartlook.instance.eventProperties.putString(secondPropertyKey, secondPropertyValue)
    }

    // Test cases MST-193, MST-251, MST-252, MST-326
    fun testWorkWithGlobalProperties() {
        Smartlook.instance.user.identifier = "testWorkWithGlobalProperties"

        Smartlook.instance.eventProperties.putString("globalProperty1", "globalValue1")
        Smartlook.instance.eventProperties.putString("globalProperty2", "globalValue2")
        Smartlook.instance.trackEvent("globalProperties put")

        Smartlook.instance.eventProperties.remove("globalProperty1")
        Smartlook.instance.trackEvent("globalProperties remove")

        Smartlook.instance.eventProperties.putString(
            "globalSetByGetPlusAdd",
            Smartlook.instance.eventProperties.getString("globalProperty2")
        )
        Smartlook.instance.trackEvent("globalProperties add, get")

        Smartlook.instance.eventProperties.clear()
        Smartlook.instance.trackEvent("globalProperties clear")
    }

    // Test case MST-459
    fun testGlobalLocalPropertiesMerging() {
        Smartlook.instance.user.identifier = "testGlobalLocalPropertiesMerging"

        Smartlook.instance.eventProperties.putString("merge", "mergeGlobalValue")

        val properties = Properties()
            .putString("merge", "mergeLocalValue")
        Smartlook.instance.trackEvent("mergingEvent", properties)
    }

    // Test cases MST-315, MST-236, MST-316, MST-317, MST-319, MST-320, MST-321
    fun testWorkWithLocalProperties() {
        Smartlook.instance.user.identifier = "testWorkWithLocalProperties"

        val properties = Properties()
            .putString("localProperty1", "localValue1")
            .putString("localProperty2", "localValue2")
        Smartlook.instance.trackEvent("localProperties put", properties)

        properties.remove("localProperty1")
        Smartlook.instance.trackEvent("localProperties remove", properties)

        properties.putString("localSetByGetPlusAdd", properties.getString("localProperty2"))
        Smartlook.instance.trackEvent("localProperties add,get ", properties)

        properties.clear()
        Smartlook.instance.trackEvent("localProperties clear", properties)
    }

    // Test case MST-313
    fun testAllowedCharactersProperties() {
        Smartlook.instance.user.identifier = "testAllowedCharactersProperties"

        val properties = Properties()
            .putString("sample123", "alphanumericNumberTracked")
            .putString("sample123*", "alphanumericNumberSpecialCharacterNotTracked")
            .putString("sample.123", "alphanumericNumberSpecialCharacterDotTracked")
            .putString("sample_123", "alphanumericNumberSpecialCharacterUnderscoreTracked")
            .putString("sample 123", "alphanumericNumberSpecialCharacterSpaceTracked")
            .putString("sample-123", "alphanumericNumberSpecialCharacterHyphenTracked")
            .putString("123sample", "numberAlphanumericNotTracked")
            .putString("*sample", "notAllowedSpecialCharacterAlphanumericNotTracked")
            .putString("-sample", "allowedSpecialCharacterAlphanumericNotTracked")
        Smartlook.instance.trackEvent("properties restrictions", properties)
    }

    // Test case MST-233
    fun testNameRestrictionsProperties() {
        Smartlook.instance.user.identifier = "testNameRestrictionsProperties"

        val properties = Properties()
            .putString("o", "length 1 can be set")
            .putString(
                "lenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetyesitcan",
                "length 200 can be set"
            )
            .putString(
                "lenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetlenghttwohundredcanbesetyesitcan1",
                "length 201 cannot be set"
            )
            .putString("onetwothree", "length 11 can be set")
            .putString("", "empty cannot be set")
        Smartlook.instance.trackEvent("properties restrictions", properties)
    }

    // Test case MST-234
    fun testValueRestrictionsProperties() {
        Smartlook.instance.user.identifier = "testValueRestrictionsProperties"
        val properties = Properties()
            .putString("emptyNotTracked", "")
            .putString(
                "fiveThousandOneCharactersNotTracked",
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis. Nunc egestas, augue at pellentesque laoreet, felis eros vehicula leo, at malesuada velit leo quis pede. Donec interdum, metus et hendrerit aliquet, dolor diam sagittis ligula, eget egestas libero turpis vel mi. Nunc nulla. Fusce risus nisl, viverra et, tempor et, pretium in, sapien. Donec venenatis vulputate lorem. Morbi nec metus. Phasellus blandit leo ut odio. Maecenas ullamcorper, dui et placerat feugiat, eros pede varius nisi, condimentum viverra felis nunc et lorem. Sed magna purus, fermentum eu, tincidunt eu, varius ut, felis. In auctor lobortis lacus. Quisque libero metus, condimentum nec, tempor a, commodo mollis, magna. Vestibulum ullamcorper mauris at ligul1"
            )
            .putString(
                "fiveThousandCharactersTracked",
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam. Pellentesque ut neque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In dui magna, posuere eget, vestibulum et, tempor auctor, justo. In ac felis quis tortor malesuada pretium. Pellentesque auctor neque nec urna. Proin sapien ipsum, porta a, auctor quis, euismod ut, mi. Aenean viverra rhoncus pede. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut non enim eleifend felis pretium feugiat. Vivamus quis mi. Phasellus a est. Phasellus magna. In hac habitasse platea dictumst. Curabitur at lacus ac velit ornare lobortis. Curabitur a felis in nunc fringilla tristique. Morbi mattis ullamcorper velit. Phasellus gravida semper nisi. Nullam vel sem. Pellentesque libero tortor, tincidunt et, tincidunt eget, semper nec, quam. Sed hendrerit. Morbi ac felis. Nunc egestas, augue at pellentesque laoreet, felis eros vehicula leo, at malesuada velit leo quis pede. Donec interdum, metus et hendrerit aliquet, dolor diam sagittis ligula, eget egestas libero turpis vel mi. Nunc nulla. Fusce risus nisl, viverra et, tempor et, pretium in, sapien. Donec venenatis vulputate lorem. Morbi nec metus. Phasellus blandit leo ut odio. Maecenas ullamcorper, dui et placerat feugiat, eros pede varius nisi, condimentum viverra felis nunc et lorem. Sed magna purus, fermentum eu, tincidunt eu, varius ut, felis. In auctor lobortis lacus. Quisque libero metus, condimentum nec, tempor a, commodo mollis, magna. Vestibulum ullamcorper mauris at ligul"
            )
            .putString("twoCharactersTracked", "sa")
        Smartlook.instance.trackEvent("properties restrictions", properties)
    }

    // Test case MST-323
    fun testMultipleStartCalls() {
        Smartlook.instance.user.identifier = "testMultipleStartCalls"

        Smartlook.instance.start()
        Smartlook.instance.start()
    }

    // Test case MST-325
    fun testMultipleStopCalls() {
        Smartlook.instance.user.identifier = "testMultipleStopCalls"

        Smartlook.instance.stop()
        Smartlook.instance.stop()
    }
}