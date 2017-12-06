package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import shop.ineed.app.ineed.R

import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.content_comments.*
import livroandroid.lib.utils.AndroidUtils
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import shop.ineed.app.ineed.adapter.CommentsAdapter
import shop.ineed.app.ineed.domain.Comments
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.interfaces.RecyclerClickListener

class CommentsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {


    private val idStore by lazy { intent.getStringExtra("idStore") }
    private var comments = ArrayList<Comments>()
    lateinit var commentsAdapter: CommentsAdapter
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        enableToolbar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Log.i("idStore", "CommentsActivity: " + idStore)
            startActivity<CreateCommentActivity>("idStore" to idStore)
        }

        database = LibraryClass.getFirebase().child("stores").child(idStore).child("feedbacks")

        listComments.layoutManager = LinearLayoutManager(baseContext)
        commentsAdapter = CommentsAdapter(comments, listener)
        listComments.setHasFixedSize(true)
        listComments.adapter = commentsAdapter

        loadComments()

        // Swipe to Refresh
        swipeToRefreshComments.setOnRefreshListener(this)
        swipeToRefreshComments.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        )
        swipeToRefreshComments.isRefreshing = true
    }

    private val listener = RecyclerClickListener { _, _ ->

    }

    override fun onRefresh() {
        // Valida se existe conexão ao fazer o gesto de Pull to Refresh
        if (AndroidUtils.isNetworkAvailable(baseContext)) {
            loadComments()
            swipeToRefreshComments.isRefreshing = false
        } else {
            swipeToRefreshComments.isRefreshing = false
            toast("Não foi possivel acessar a internet")
        }
    }

    private fun loadComments() {
        database.addValueEventListener(eventUpdateData)
    }

    private val eventUpdateData = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            comments.clear()
            for (c in dataSnapshot.children) {
                val comment = c.getValue(Comments::class.java)

                if (comment != null) {
                    comments.add(comment)
                    commentsAdapter.notifyDataSetChanged()
                    swipeToRefreshComments.isRefreshing = false
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            FirebaseCrash.log(databaseError.message)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.removeEventListener(eventUpdateData)
    }
}
