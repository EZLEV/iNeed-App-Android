package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.fragments.ListCategoriesFragment

class ListCategoriesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_categories)

        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            val frag = ListCategoriesFragment()
            supportFragmentManager.beginTransaction().replace(R.id.containerListCategories, frag).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
