package shop.ineed.app.ineed.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener
import com.sendbird.android.GroupChannel

import java.util.Timer
import java.util.TimerTask

import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.content_store.*
import kotlinx.android.synthetic.main.fragment_details_product.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.adapter.SlideAdapter
import shop.ineed.app.ineed.domain.Store
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.interfaces.RecyclerClickListener
import shop.ineed.app.ineed.adapter.BusinessTimesAdapter
import shop.ineed.app.ineed.adapter.PaymentMethodsAdapter
import shop.ineed.app.ineed.domain.PaymentMethods
import shop.ineed.app.ineed.domain.User
import shop.ineed.app.ineed.fragments.DetailsProductFragment
import shop.ineed.app.ineed.fragments.DetailsProductFragment.Companion.EXTRA_NEW_CHANNEL_URL
import shop.ineed.app.ineed.util.ImageUtils
import java.util.ArrayList

class StoreActivity : BaseActivity(), ViewPager.OnPageChangeListener, OnMapReadyCallback {

    private var store: Store? = null
    private var googleMap: GoogleMap? = null
    private lateinit var uid: String

    companion object {
        private var CURRENT_PAGE = 0
        private var NUM_PAGES = 0
        private val TAG = DetailsProductFragment.javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        enableToolbar()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val storeIntent = intent.getStringExtra("store")
        uid = LibraryClass.getUserLogged(baseContext, User.PROVIDER)

        val reference = LibraryClass.getFirebase().child("stores").child(storeIntent)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val storeCurrent = dataSnapshot.getValue(Store::class.java)
                storeCurrent?.id = dataSnapshot.key
                store = storeCurrent!!

                initSlide()

                ImageUtils.displayImageFromUrl(baseContext, storeCurrent.pictures[0], ivStoreDetails)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = Color.parseColor(storeCurrent.color)
                }
                // Update Values
                txtNameStore.text = storeCurrent.name
                txtDescriptionStore.text = storeCurrent.description
                txtNameStoreToolbar.text = storeCurrent.name
                appBar.setBackgroundColor(Color.parseColor(storeCurrent.color))
                toolbarLayout.setContentScrimColor(Color.parseColor(storeCurrent.color))
                toolbarLayout.setStatusBarScrimColor(Color.parseColor(storeCurrent.color))

                initValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                FirebaseCrash.log(databaseError.message)
            }
        })

        btnSendMessageStore.setOnClickListener {
            if (LibraryClass.isUserLogged(this)) {
                Log.d("STORE", "btnSendMessageProduct")
                val userIds = ArrayList<String>()
                userIds.add(LibraryClass.getUserLogged(baseContext, User.PROVIDER))
                userIds.add(store?.id!!)

                GroupChannel.createChannelWithUserIds(userIds, true, userIds[0] + "_" + userIds[1], "", "") { groupChannel, e ->
                    if (e != null) {
                        snackbar(detailsProductFragment, "Nao foi possivel abrir o chat. Occoreu algum erro, tente mais tarde!")
                    }
                    Log.i("SEND", userIds[1] + "_" + userIds[0])
                    Log.i("SEND", groupChannel.name)
                    startActivity<GroupChannelActivity>(EXTRA_NEW_CHANNEL_URL to groupChannel.url)
                }
            } else {
                toast("Nao logado")
            }
        }

        btnRouteStore.setOnClickListener {
            val uri = Uri.parse("geo:0,0?z=21&q=" + store?.location?.lat + "," + store?.location?.lng + store?.name)
            Log.i(TAG, "Location: " + store?.location?.lat + "," + store?.location?.lng)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.`package` = "com.google.android.apps.maps"
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                snackbar(detailsProductFragment, "Nao foi possivel abrir o Google Maps")
            }
        }

        btnRatingsStore.setOnClickListener {
            if (LibraryClass.isUserLogged(this)) {
                Log.i("idStore", "CommentsActivity: " + store?.id)
                startActivity<CommentsActivity>("idStore" to store!!.id)
            } else {
                toast("Nao logado")
            }
        }

        addReviewsContainerStore.setOnClickListener {
            if (LibraryClass.isUserLogged(this)) {
                Log.i("idStore", "CommentsActivity: " + store?.id)
                startActivity<CreateCommentActivity>("idStore" to store!!.id)
            } else {
                toast("Nao logado")
            }
        }

        swipeRefreshContainerStore.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        )
        swipeRefreshContainerStore.isRefreshing = true
    }

    private fun disableToRefresh() {
        swipeRefreshContainerStore.isRefreshing = false
        swipeRefreshContainerStore.isEnabled = false
    }

    fun initValue() {
        // Valores
        txtNameContainerStore.text = store?.name
        txtDescriptionContainerStore.text = store?.description
        txtAddressContainerStore.text = store?.location?.address
        txtPhoneContainerStore.text = store?.phone
        txtCellPhoneContainerStore.text = store?.cellphone

        // Lista de pagamento
        val listPayment = PaymentMethods.getPaymentMethods(store?.paymentWays)
        listViewContainerStore.layoutManager = LinearLayoutManager(baseContext)
        val paymentMethodsAdapter = PaymentMethodsAdapter(listPayment)
        listViewContainerStore.setHasFixedSize(true)
        listViewContainerStore.adapter = paymentMethodsAdapter

        // Lista de horarios
        listOperatingHoursContainerStore.layoutManager = LinearLayoutManager(baseContext)
        val businessTimesAdapter = BusinessTimesAdapter(store!!.businessTimes)
        listOperatingHoursContainerStore.setHasFixedSize(true)
        listOperatingHoursContainerStore.adapter = businessTimesAdapter

        disableToRefresh()
    }

    override fun onResume() {
        super.onResume()
        val mapContainerStore = supportFragmentManager.findFragmentById(R.id.mapContainerStore) as SupportMapFragment
        mapContainerStore.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.isMyLocationEnabled = true

        if (store != null) {
            val location = LatLng(store!!.location.lat!!, store!!.location.lng!!)

            val update = CameraUpdateFactory.newLatLngZoom(location, 13f)
            googleMap.moveCamera(update)

            googleMap.addMarker(MarkerOptions()
                    .title(store!!.name)
                    .snippet(store!!.location.address)
                    .position(location))

            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private val listener = RecyclerClickListener { _, _ ->
        startActivity<PhotoViewerActivity>(
                "url" to store!!.pictures[sliderDetailsStore.currentItem],
                "title" to store!!.name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSlide() {
        sliderDetailsStore.adapter = SlideAdapter(baseContext, store!!.pictures, listener)
        indicatorDetailsSlide.setViewPager(sliderDetailsStore)
        val density = resources.displayMetrics.density
        indicatorDetailsSlide.radius = 5 * density

        NUM_PAGES = store?.pictures!!.size

        val handler = Handler()
        val update = {
            if (CURRENT_PAGE == NUM_PAGES) {
                CURRENT_PAGE = 0
            }
            sliderDetailsStore.setCurrentItem(CURRENT_PAGE++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
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
}