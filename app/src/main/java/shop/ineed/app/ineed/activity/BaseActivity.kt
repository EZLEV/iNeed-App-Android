package shop.ineed.app.ineed.activity

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.browse
import org.jetbrains.anko.startActivity

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.util.PreferenceUtils

/**
 * Created by jose on 9/8/17.
 *
 * Class comum para todas as Activities
 */

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = this@BaseActivity.javaClass.simpleName

    var toolbar: Toolbar? = null
        private set
    private var drawerLayout: DrawerLayout? = null


    protected fun enableToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
    }

    protected fun setupNavigationDrawer() {
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.about, R.string.about)

        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        setNavViewValues(navigationView)

        navigationView.setCheckedItem(R.id.navigation_home)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
            }
            R.id.navigation_navigation -> {
                startActivity<ListCategoriesActivity>()
            }
            R.id.navigation_search -> {
                startActivity<SearchActivity>()
            }
            R.id.navigation_message -> {
                startActivity<GroupChannelActivity>()
            }
            R.id.navigation_help ->{
                browse("http://www.2need.store")
            }
            R.id.navigation_about -> {
                browse("http://www.2need.store")
            }
        }

        drawerLayout?.closeDrawer(GravityCompat.START)
        return false
    }


    // Atualiza os dados do header do Navigation Viewpublic
    private fun setNavViewValues(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)

        val txt = headerView.findViewById<TextView>(R.id.txtHeard)
        val iv = headerView.findViewById<AppCompatImageButton>(R.id.ivHeard)

        if(LibraryClass.isUserLogged(baseContext)){
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            txt.text = user?.displayName
            iv.setOnClickListener {
                startActivity<AccountActivity>()
            }
        }else{
            iv.setImageResource(R.drawable.ic_login)
            iv.setOnClickListener {
                startActivity<ChooseInputMethodActivity>()
            }
        }
    }

    override fun onBackPressed() {
        if(drawerLayout != null){
            if(drawerLayout!!.isDrawerOpen(GravityCompat.START)){
                drawerLayout!!.closeDrawer(GravityCompat.START)
            }else{
                super.onBackPressed()
            }
        }else{
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (PreferenceUtils.getConnected(applicationContext)) {
            CommonSubscriberActivity.connectToSendBird(PreferenceUtils.getUserId(applicationContext), PreferenceUtils.getNickname(applicationContext), applicationContext)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransitionEnter()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}
