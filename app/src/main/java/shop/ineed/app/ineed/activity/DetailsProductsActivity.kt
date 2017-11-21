package shop.ineed.app.ineed.activity

import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details_products.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Product
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.fragments.DetailsProductFragment

class DetailsProductsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    private var mImageProduct: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_details_products)

        enableToolbar()

        val product = intent.getParcelableExtra<Product>("product")
        mImageProduct = product.pictures[0]

        supportActionBar?.setTitle(product.name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appbarDetailsProduct.addOnOffsetChangedListener(this)

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

            }
        })

        initViews()
    }

    private fun initViews() {
        Picasso.with(this).load(mImageProduct).into(ivProductDetailsProduct)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getExtras().getString(ProductsActivity.EXTRA_PRODUCT_IMAGE_TRANSITION_NAME);
            ivProduct.setTransitionName(imageTransitionName);
        }*/
    }

    private fun loadProduct() {

    }

    private fun openFragment(){

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        var scrollRange = -1
        //Initialize the size of the scroll
        if (scrollRange == -1) {
            scrollRange = appBarLayout.totalScrollRange
        }
        //Check if the view is collapsed
        if (scrollRange + verticalOffset == 0) {
            toolbar!!.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        } else {
            toolbar!!.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
