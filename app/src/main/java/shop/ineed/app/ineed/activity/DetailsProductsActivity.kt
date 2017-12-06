package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_details_products.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Product
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.fragments.DetailsProductFragment
import shop.ineed.app.ineed.util.ImageUtils

class DetailsProductsActivity : BaseActivity(){

    private var imageProduct: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_products)

        // Toolbar
        enableToolbar()

        val product = intent.getParcelableExtra<Product>("product")
        imageProduct = product.pictures[0]

        supportActionBar?.title = product.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reference = LibraryClass.getFirebase().child("products").child(product.id)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val product = dataSnapshot.getValue(Product::class.java)
                Log.i("STORE", product!!.name)

                if(savedInstanceState == null){
                    val productFragment = DetailsProductFragment()
                    val bundle = Bundle()
                    bundle.putParcelable("product", product)
                    productFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.detailsProductFragment, productFragment).commit()
                    Log.i("SAVED", "OK")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                FirebaseCrash.log(databaseError.message)
            }
        })

        // Loading
        swipeRefreshProduct.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        )
        swipeRefreshProduct.isRefreshing = true

        initViews()
    }

    fun disableToRefresh(){
        swipeRefreshProduct.isRefreshing = false
        swipeRefreshProduct.isEnabled = false
    }

    private fun initViews() {
        ImageUtils.displayImageFromUrl(this, imageProduct, ivProductDetailsProduct)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
