package shop.ineed.app.ineed.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.fragments.HomeFragment

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        enableToolbar()
        setupNavigationDrawer()

        toolbar!!.navigationIcon!!.setColorFilter(ContextCompat.getColor(baseContext, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            val frag = HomeFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerHome, frag).commit()
        }
    }
}
