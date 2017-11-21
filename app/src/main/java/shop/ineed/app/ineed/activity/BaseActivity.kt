package shop.ineed.app.ineed.activity

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
import org.jetbrains.anko.startActivity

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.User
import shop.ineed.app.ineed.domain.util.LibraryClass

/**
 * Created by jose on 9/8/17.
 *
 * Class comum para todas as Activities
 */

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = this@BaseActivity.javaClass.simpleName

    var toolbar: Toolbar? = null
        private set
    private var mDrawerLayout: DrawerLayout? = null

    protected fun enableToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
    }

    protected fun setupNavigationDrawer() {
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView


        val toggle = ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.about, R.string.about)

        mDrawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        if (navigationView != null && mDrawerLayout != null) {
            setNavViewValues(navigationView)

            navigationView.setNavigationItemSelectedListener { item ->
               // item.isChecked = true
                mDrawerLayout!!.closeDrawers()
                onNavDrawerItemSelected(item)
                false
            }
        }

        navigationView.setCheckedItem(R.id.navigation_home)
    }

    private fun onNavDrawerItemSelected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.navigation_home -> {
            }
            R.id.navigation_navigation -> {
                startActivity<ListCategoriesActivity>()
            }
            R.id.navigation_search -> {
                startActivity<SearchActivity>()
            }
            R.id.navigation_favorites -> {
                startActivity<FavoritesActivity>()
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                // Trata o clique no botão que abre o menu
                if (mDrawerLayout != null) {
                    openDrawer()
                    return true
                }
        }
        return super.onOptionsItemSelected(item)
    }

    // Abre o menu lateral
    protected fun openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout!!.openDrawer(GravityCompat.START)
        }
    }

    //Fecha o menu lateral
    protected fun closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
        }
    }
}
