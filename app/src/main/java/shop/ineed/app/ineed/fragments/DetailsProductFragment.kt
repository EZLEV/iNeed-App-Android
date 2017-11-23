package shop.ineed.app.ineed.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.crash.FirebaseCrash

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import com.sendbird.android.GroupChannel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details_product.*
import org.jetbrains.anko.design.snackbar

import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.activity.GroupChannelActivity
import shop.ineed.app.ineed.activity.PhotoViewerActivity
import shop.ineed.app.ineed.activity.StoreActivity
import shop.ineed.app.ineed.adapter.SlideAdapter
import shop.ineed.app.ineed.domain.Product
import shop.ineed.app.ineed.domain.Store
import shop.ineed.app.ineed.domain.User
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.interfaces.RecyclerClickListener
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class DetailsProductFragment : BaseFragment(), ViewPager.OnPageChangeListener {

    private lateinit var product: Product
    private lateinit var uid: String
    private lateinit var reference: DatabaseReference
    lateinit var mStore: Store
    private val TAG = DetailsProductFragment.javaClass.simpleName
    private val PERMISSION_LOCATION = 13

    private val listener = RecyclerClickListener { _, _ ->
        activity.startActivity<PhotoViewerActivity>(
                "url" to product.pictures[sliderProductDetails.currentItem],
                "title" to product.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uid = LibraryClass.getUserLogged(context, User.PROVIDER)
        product = arguments.getParcelable("product")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_details_product, container, false)
        reference = LibraryClass.getFirebase().child("products").child(product.id)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtTitleProductDetails.text = product.name
        txtDescriptionDetails.text = product.description
        groupTags.setTags(product.categories)

        txtUpProductDetails.text = product.upVotesCount.toString()
        txtDownProductDetails.text = product.downVotesCount.toString()

        // Store
        val referenceStore = LibraryClass.getFirebase().child("stores").child(product.store)
        referenceStore.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val store = dataSnapshot.getValue(Store::class.java)
                Log.i("STORE", store!!.name)
                Picasso.with(context).load(store.pictures[0]).into(ivStoreProductDetails)
                txtNameStoreProductDetails.text = store.name
                containerStoreDetailsProduct.setBackgroundColor(Color.parseColor(store.color))
                mStore = store
            }

            override fun onCancelled(databaseError: DatabaseError) {
                FirebaseCrash.log(databaseError.message)
            }
        })

        buttonStore.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermissions()
            } else {
                activity.startActivity<StoreActivity>("store" to product.store)
            }
        }

        btnUpProductDetails.setOnClickListener {
            if (LibraryClass.isUserLogged(activity)) {
                if (product.upVotes != null) {
                    if (product.upVotes.contains(uid)) {
                        verifyUpVotes()
                    } else {
                        val value = HashMap<String, Any>()
                        product.upVotes.add(uid)
                        value.put("upVotes", product.upVotes)
                        value.put("upVotesCount", product.upVotesCount.inc())
                        if (product.downVotes != null) {
                            product.downVotes.remove(uid)
                        }
                        value.put("downVotes", product.downVotes)
                        if (product.downVotesCount > 0) {
                            value.put("downVotesCount", product.downVotesCount.dec())
                        }
                        reference.updateChildren(value).addOnCompleteListener {
                            verifyUpVotes()
                        }
                    }
                } else {
                    val value = HashMap<String, Any>()
                    if (product.upVotes != null) {
                        product.upVotes.add(uid)
                        value.put("upVotes", product.upVotes)
                    } else {
                        value.put("upVotes", mutableListOf(uid))
                    }
                    value.put("upVotesCount", product.upVotesCount.inc())
                    if (product.downVotes != null) {
                        product.downVotes.remove(uid)
                    }
                    value.put("downVotes", product.downVotes)
                    if (product.downVotesCount > 0) {
                        value.put("downVotesCount", product.downVotesCount.dec())
                    }
                    reference.updateChildren(value).addOnCompleteListener {
                        verifyUpVotes()
                    }
                }
                snackbar(detailsProductFragment, "Adicionado aos produtos marcados como \"Gostei\"")
            } else {
                activity.toast("Nao logado")
            }
        }

        btnDownProductDetails.setOnClickListener {
            if (LibraryClass.isUserLogged(activity)) {
                if (product.downVotes != null) {
                    if (product.downVotes.contains(uid)) {
                        verifyDownVotes()
                    } else {
                        val value = HashMap<String, Any>()
                        product.downVotes.add(uid)
                        value.put("downVotes", product.downVotes)
                        value.put("downVotesCount", product.downVotesCount.inc())
                        if (product.upVotes != null) {
                            product.upVotes.remove(uid)
                        }
                        value.put("upVotes", product.upVotes)
                        if (product.upVotesCount > 0) {
                            value.put("upVotesCount", product.upVotesCount.dec())
                        }
                        reference.updateChildren(value).addOnCompleteListener {
                            verifyDownVotes()
                        }
                    }
                } else {
                    val value = HashMap<String, Any>()
                    if (product.downVotes != null) {
                        product.downVotes.add(uid)
                        value.put("downVotes", product.downVotes)
                    } else {
                        value.put("downVotes", mutableListOf(uid))
                    }
                    value.put("downVotesCount", product.downVotesCount.inc())
                    if (product.upVotes != null) {
                        product.upVotes.remove(uid)
                    }
                    value.put("upVotes", product.upVotes)
                    if (product.upVotesCount > 0) {
                        value.put("upVotesCount", product.upVotesCount.dec())
                    }
                    reference.updateChildren(value).addOnCompleteListener {
                        verifyDownVotes()
                    }
                }
                snackbar(detailsProductFragment, "Removido dos produtos marcados como \"Gostei\"")
            } else {
                activity.toast("Nao logado")
            }
        }



        btnSendMessageProductDetails.setOnClickListener {
            if (LibraryClass.isUserLogged(activity)) {
                Log.d("STORE", "btnSendMessageProduct")
                val userIds = ArrayList<String>()
                userIds.add(product.store)
                userIds.add(LibraryClass.getUserLogged(activity, User.PROVIDER))

                GroupChannel.createChannelWithUserIds(userIds, true, userIds[0] + "_" + userIds[1], "", "") { groupChannel, e ->
                    if (e != null) {
                        snackbar(detailsProductFragment, "Nao foi possivel abrir o chat. Occoreu algum erro, tente mais tarde!")
                    }
                    Log.i("SEND", userIds[1] + "_" + userIds[0])
                    //Log.i("SEND", groupChannel.name)
                    activity.startActivity<GroupChannelActivity>(EXTRA_NEW_CHANNEL_URL to groupChannel.url)
                }
            } else {
                activity.toast("Nao logado")
            }
        }

        btnRouteProductDetails.setOnClickListener {
            val uri = Uri.parse("geo:0,0?z=21&q=" + mStore.location.lat + "," + mStore.location.lng + mStore.name)
            Log.i(TAG, "Location: " + mStore.location.lat + "," + mStore.location.lng)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.`package` = "com.google.android.apps.maps"
            if (intent.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(intent)
            } else {
                snackbar(detailsProductFragment, "Nao foi possivel abrir o Google Maps")
            }
        }

        initSlide()
        updateValues()
    }

    /**
     *
     */
    private fun verifyUpVotes() {
        if (product.upVotes != null) {
            if (product.upVotes.contains(uid)) {
                btnUpProductDetails.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
                btnDownProductDetails.setColorFilter(ContextCompat.getColor(context, R.color.grey_600))
            }
        }
    }

    /**
     *
     */
    private fun verifyDownVotes() {
        if (product.downVotes != null) {
            if (product.downVotes.contains(uid)) {
                btnUpProductDetails.setColorFilter(ContextCompat.getColor(context, R.color.grey_600))
                btnDownProductDetails.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }


    /**
     *
     */
    private fun updateValues() {
        reference.addValueEventListener(eventUpdateData)
    }

    /**
     *
     */
    private val eventUpdateData = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val productUpdate = dataSnapshot.getValue(Product::class.java)
            Log.i("STORE", productUpdate?.name)

            product = productUpdate!!

            txtUpProductDetails.text = productUpdate.upVotesCount.toString()
            txtDownProductDetails.text = productUpdate.downVotesCount.toString()

            verifyUpVotes()
            verifyDownVotes()

            Log.i("TAGFIREBASE", "update")
        }

        override fun onCancelled(databaseError: DatabaseError) {
            FirebaseCrash.log(databaseError.message)
        }
    }


    /**
     *
     */
    private fun initSlide() {
        sliderProductDetails.adapter = SlideAdapter(activity, product.pictures, listener)
        indicatorSlideProductDetails.setViewPager(sliderProductDetails)

        val density = resources.displayMetrics.density

        indicatorSlideProductDetails.radius = 5 * density

        NUM_PAGES = product.pictures.size

        val handler = Handler()
        val Update = Runnable {
            if (CURRENT_PAGE == NUM_PAGES) {
                CURRENT_PAGE = 0
            }
            if (sliderProductDetails != null) {
                sliderProductDetails.setCurrentItem(CURRENT_PAGE++, true)
            }
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 4000, 4000)

        indicatorSlideProductDetails.setOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        CURRENT_PAGE = position
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    companion object {
        private var CURRENT_PAGE = 0
        private var NUM_PAGES = 0
        val EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL"
        val EXTRA_GROUP_CHANNEL_URL = "GROUP_CHANNEL_URL"
    }

    override fun onDetach() {
        super.onDetach()
        reference.removeEventListener(eventUpdateData)
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Permissao")
                    .setContentText("Permissao de localizacao")
                    .setCancelText("Cancelar")
                    .setConfirmText("OK")
                    .showCancelButton(true)
                    .setCancelClickListener {
                        it.cancel()
                    }
                    .setConfirmClickListener {
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                                PERMISSION_LOCATION)
                        it.cancel()
                    }
                    .show()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_LOCATION)
        }
    }
}
