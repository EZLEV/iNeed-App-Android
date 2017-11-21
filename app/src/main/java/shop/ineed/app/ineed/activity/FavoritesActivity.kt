package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.view.MenuItem

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.fragments.DisconnectedFromAccountFragment
import shop.ineed.app.ineed.fragments.FavoritesFragment

class FavoritesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        enableToolbar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.favorites)

        if (savedInstanceState == null && LibraryClass.isUserLogged(baseContext)) {
            val frag = FavoritesFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerFavorites, frag).commit()
        } else if (savedInstanceState == null) {
            val frag = DisconnectedFromAccountFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerFavorites, frag).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
