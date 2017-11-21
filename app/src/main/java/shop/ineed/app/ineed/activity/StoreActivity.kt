package shop.ineed.app.ineed.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.Window

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener
import com.sendbird.android.GroupChannel
import com.squareup.picasso.Picasso
import com.viewpagerindicator.CirclePageIndicator

import java.util.Timer
import java.util.TimerTask

import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.content_store.*
import kotlinx.android.synthetic.main.fragment_details_product.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.adapter.SlideAdapter
import shop.ineed.app.ineed.domain.Store
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.interfaces.RecyclerClickListener
import shop.ineed.app.ineed.activity.PhotoViewerActivity
import shop.ineed.app.ineed.adapter.BusinessTimesAdapter
import shop.ineed.app.ineed.adapter.PaymentMethodsAdapter
import shop.ineed.app.ineed.domain.PaymentMethods
import shop.ineed.app.ineed.domain.User
import shop.ineed.app.ineed.fragments.DetailsProductFragment
import java.util.ArrayList

class StoreActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, OnMapReadyCallback {

    private lateinit var mStore: Store
    private var googleMap: GoogleMap? = null
    private lateinit var uid: String
    private val TAG = DetailsProductFragment.javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_store)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val store = intent.getStringExtra("store")
        uid = LibraryClass.getUserLogged(baseContext, User.PROVIDER)

        val reference = LibraryClass.getFirebase().child("stores").child(store)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val store = dataSnapshot.getValue(Store::class.java)

                store?.id = dataSnapshot.key

                mStore = store!!
                initSlide()
                Log.i("STORE", store.name)
                Picasso.with(baseContext).load(store.pictures[0]).into(ivStoreDetails)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = Color.parseColor(store.color)
                }

                txtNameStore.text = store.name
                txtDescriptionStore.text = store.description
                txtNameStoreToolbar.text = store.name
                appBar.setBackgroundColor(Color.parseColor(store.color))
                toolbarLayout.setContentScrimColor(Color.parseColor(store.color))
                toolbarLayout.setStatusBarScrimColor(Color.parseColor(store.color))

                initValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        btnSendMessageStore.setOnClickListener {
            Log.d("STORE", "btnSendMessageProduct")
            val userIds = ArrayList<String>()
            userIds.add(LibraryClass.getUserLogged(baseContext, User.PROVIDER))
            userIds.add(mStore.id)

            GroupChannel.createChannelWithUserIds(userIds, true) { groupChannel, e ->
                if (e != null) {
                    snackbar(detailsProductFragment, "Nao foi possivel abrir o chat. Occoreu algum erro, tente mais tarde!")
                }
                startActivity<GroupChannelActivity>(DetailsProductFragment.EXTRA_NEW_CHANNEL_URL to groupChannel.url)
            }
        }

        btnRouteStore.setOnClickListener {
            val uri = Uri.parse("geo:0,0?z=21&q=" + mStore.location.lat + "," + mStore.location.lng + mStore.name)
            Log.i(TAG, "Location: " + mStore.location.lat + "," + mStore.location.lng)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.`package` = "com.google.android.apps.maps"
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                snackbar(detailsProductFragment, "Nao foi possivel abrir o Google Maps")
            }
        }

        btnRatingsStore.setOnClickListener {
            Log.i("idStore", "CommentsActivity: " + mStore.id)
            startActivity<CommentsActivity>("idStore" to mStore.id)
        }


    }

    fun initValue() {
        txtNameContainerStore.text = mStore.name
        txtDescriptionContainerStore.text = mStore.description
        txtAddressContainerStore.text = mStore.location.address
        txtPhoneContainerStore.text = mStore.phone
        txtCellPhoneContainerStore.text = mStore.cellphone


        val listPayment = PaymentMethods.getPaymentMethods(mStore.paymentWays)
        listViewContainerStore.layoutManager = LinearLayoutManager(baseContext)
        val paymentMethodsAdapter = PaymentMethodsAdapter(listPayment)
        listViewContainerStore.setHasFixedSize(true)
        listViewContainerStore.adapter = paymentMethodsAdapter


        listOperatingHoursContainerStore.layoutManager = LinearLayoutManager(baseContext)
        val businessTimesAdapter = BusinessTimesAdapter(mStore.businessTimes)
        listOperatingHoursContainerStore.setHasFixedSize(true)
        listOperatingHoursContainerStore.adapter = businessTimesAdapter
    }

    override fun onResume() {
        super.onResume()

        val mapContainerStore = supportFragmentManager.findFragmentById(R.id.mapContainerStore) as SupportMapFragment
        mapContainerStore.getMapAsync(this)
    }

    private fun initSlide() {
        sliderDetailsStore.adapter = SlideAdapter(baseContext, mStore.pictures, listener)
        indicatorDetailsSlide.setViewPager(sliderDetailsStore)
        val density = resources.displayMetrics.density
        indicatorDetailsSlide.radius = 5 * density

        NUM_PAGES = mStore.pictures.size

        val handler = Handler()
        val Update = {
            if (CURRENT_PAGE == NUM_PAGES) {
                CURRENT_PAGE = 0
            }
            sliderDetailsStore.setCurrentItem(CURRENT_PAGE++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 4000, 4000)

        indicatorDetailsSlide.setOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        CURRENT_PAGE = position
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.isMyLocationEnabled = true

        val location = LatLng(mStore!!.location.lat!!, mStore!!.location.lng!!)

        val update = CameraUpdateFactory.newLatLngZoom(location, 13f)
        googleMap.moveCamera(update)

        googleMap.addMarker(MarkerOptions()
                .title(mStore.name)
                .snippet(mStore.location.address)
                .position(location))

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private val listener = RecyclerClickListener { view, position ->
        startActivity<PhotoViewerActivity>(
                "url" to mStore.pictures[sliderDetailsStore.currentItem],
                "title" to mStore.name)
    }

    companion object {
        private var CURRENT_PAGE = 0
        private var NUM_PAGES = 0
    }
}
