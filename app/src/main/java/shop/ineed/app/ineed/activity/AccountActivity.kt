package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.view.MenuItem
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.fragments.AccountFragment

class AccountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        enableToolbar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "EU"

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null)
        {
            val frag = AccountFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerAccount, frag).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
