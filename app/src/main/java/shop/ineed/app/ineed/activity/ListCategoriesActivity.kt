package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.view.MenuItem
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.fragments.ListCategoriesFragment

class ListCategoriesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_categories)

        enableToolbar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.all_categories)

        if (savedInstanceState == null) {
            val frag = ListCategoriesFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerListCategories, frag).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
