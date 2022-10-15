package com.unity3d.player

import com.unity3d.player.IUnityPlayerLifecycleEvents
import com.unity3d.player.UnityPlayer
import android.os.Bundle
import android.content.Intent
import com.unity3d.player.MultiWindowSupport
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class UnityPlayerActivity : AppCompatActivity(), IUnityPlayerLifecycleEvents {
    protected var mUnityPlayer // don't change the name of this variable; referenced from native code
            : UnityPlayer? = null

    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected fun updateUnityCommandLineArguments(cmdLine: String?): String? {
        return cmdLine
    }

    // Setup activity layout
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        val cmdLine = updateUnityCommandLineArguments(intent.getStringExtra("unity"))
        intent.putExtra("unity", cmdLine)
        mUnityPlayer = UnityPlayer(this, this)
        setContentView(mUnityPlayer)
        mUnityPlayer!!.requestFocus()
    }

    // When Unity player unloaded move task to background
    override fun onUnityPlayerUnloaded() {
        moveTaskToBack(true)
    }

    // Callback before Unity player process is killed
    override fun onUnityPlayerQuitted() {}
    override fun onNewIntent(intent: Intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent)
        mUnityPlayer!!.newIntent(intent)
    }

    // Quit Unity
    override fun onDestroy() {
        mUnityPlayer!!.destroy()
        super.onDestroy()
    }

    // If the activity is in multi window mode or resizing the activity is allowed we will use
    // onStart/onStop (the visibility callbacks) to determine when to pause/resume.
    // Otherwise it will be done in onPause/onResume as Unity has done historically to preserve
    // existing behavior.
    override fun onStop() {
        super.onStop()
        if (!MultiWindowSupport.getAllowResizableWindow(this)) return
        mUnityPlayer!!.pause()
    }

    override fun onStart() {
        super.onStart()
        if (!MultiWindowSupport.getAllowResizableWindow(this)) return
        mUnityPlayer!!.resume()
    }

    // Pause Unity
    override fun onPause() {
        super.onPause()
        MultiWindowSupport.saveMultiWindowMode(this)
        if (MultiWindowSupport.getAllowResizableWindow(this)) return
        mUnityPlayer!!.pause()
    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        if (MultiWindowSupport.getAllowResizableWindow(this) && !MultiWindowSupport.isMultiWindowModeChangedToTrue(
                this
            )
        ) return
        mUnityPlayer!!.resume()
    }

    // Low Memory Unity
    override fun onLowMemory() {
        super.onLowMemory()
        mUnityPlayer!!.lowMemory()
    }

    // Trim Memory Unity
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer!!.lowMemory()
        }
    }

    // This ensures the layout will be correct.
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mUnityPlayer!!.configurationChanged(newConfig)
    }

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        mUnityPlayer!!.windowFocusChanged(hasFocus)
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_MULTIPLE) mUnityPlayer!!.injectEvent(
            event
        ) else super.dispatchKeyEvent(event)
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }

    /*API12*/
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return mUnityPlayer!!.injectEvent(event)
    }
}