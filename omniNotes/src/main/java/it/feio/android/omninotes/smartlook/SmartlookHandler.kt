package it.feio.android.omninotes.smartlook

import android.app.Application
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.smartlook.sdk.smartlook.core.api.Session
import com.smartlook.sdk.smartlook.core.api.Smartlook
import com.smartlook.sdk.smartlook.core.api.User
import com.smartlook.sdk.smartlook.core.api.model.Properties
import com.smartlook.sdk.smartlook.core.api.model.RecordingMask
import com.smartlook.sdk.smartlook.core.api.model.Referrer
import com.smartlook.sdk.smartlook.util.logging.annotation.LogAspect
import it.feio.android.omninotes.R
import java.net.URL
import java.util.*

object SmartlookHandler {

    private lateinit var instance: Smartlook

    /**
     * Called in Application class [OmniNotes] in onCreate() method.
     * @param application Application context
     */
    @JvmStatic
    fun onApplicationCreate(application: Application) {

        // Setup SDK and obtain Smartlook instance
        instance = Smartlook.setup(application).apply {
            log.allowedLogAspects = LogAspect.ALL
        }

        // Set project key
        instance.preferences.projectKey = "beta_af1c6a1b02af422c51a62511ee67b867123f05b1" //FIXME

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
     * Called when one of FAB items is clicked.
     */
    @JvmStatic
    fun onFabItemClick(id: Int) {
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
            else -> {
                Smartlook.instance.trackEvent("fab_text_note_item_click")
            }
        }
    }

    /**
     * Called in [MainActivity] on UI initialization. Covers whole Toolbar.
     */
    @JvmStatic
    fun createRecordingMaskForToolbar(toolbar: Toolbar) {
        Smartlook.instance.sensitivity.recordingMask = RecordingMask(
            listOf(
                RecordingMask.Element(
                        toolbar.getRectOnScreen(),
                        RecordingMask.Element.Type.COVERING
                )
            )
        )
    }

    /**
     * Called in [SublimePickerFragment] when Ok is clicked.
     */
    @JvmStatic
    fun onDateTimePickerSet() {
        Smartlook.instance.trackEvent("set_date_time")
    }


}