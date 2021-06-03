package com.challenge.drawer

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setupNavMenu()
        setUpCloseButton()
        setUpBadge(AD_TYPE)
        setUpBadge(MSG_TYPE)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupNavMenu() {
        navView.setNavigationItemSelectedListener {
            drawer.closeDrawers()
            Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_LONG).show()
            true
        }
    }

    private fun setUpCloseButton() {
        val closeButton = navView.getHeaderView(0).findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener { drawer.closeDrawer(GravityCompat.START) }
    }

    private fun setUpBadge(type: Int) {
        val navResId = when(type) {
            AD_TYPE -> R.id.nav_ads
            MSG_TYPE -> R.id.nav_msg
            else -> throw RuntimeException(getString(R.string.expected_ad_or_msg_type))
        }
        val menuItem = navView.menu.findItem(navResId)
        val badgeTextView = menuItem.actionView.findViewById<TextView>(R.id.badge_text)
        var count = 0
        setBadgeCount(badgeTextView, count, type)
        val counterResId = when(type) {
            AD_TYPE -> R.id.my_ads_counter
            MSG_TYPE -> R.id.messages_counter
            else -> throw RuntimeException(getString(R.string.expected_ad_or_msg_type))
        }
        val counterLayout = findViewById<View>(counterResId)
        val labelTextView = counterLayout.findViewById<TextView>(R.id.label_text)
        labelTextView.text = when(type) {
            AD_TYPE -> getString(R.string.my_ads)
            MSG_TYPE -> getString(R.string.my_messages)
            else -> throw RuntimeException(getString(R.string.expected_ad_or_msg_type))
        }
        val incrementButton = counterLayout.findViewById<Button>(R.id.increment_button)
        incrementButton.setOnClickListener {
            count++
            setBadgeCount(badgeTextView, count, type)
            if(badgeTextView.isVisible.not()) {
                badgeTextView.isVisible = true
            }
        }
        val resetButton = counterLayout.findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            count = 0
            setBadgeCount(badgeTextView, count, type)
            badgeTextView.isVisible = false
        }
    }

    private fun setBadgeCount(badgeTextView: TextView, count: Int, type: Int) {
        badgeTextView.text = when(type) {
            AD_TYPE -> resources.getQuantityString(
                R.plurals.ad_views_count,
                count,
                count
            )
            MSG_TYPE -> count.toString()
            else -> ""
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    companion object {
        private const val AD_TYPE = 1
        private const val MSG_TYPE = 2
    }

}