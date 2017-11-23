package shop.ineed.app.ineed.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_comment.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Comments
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.util.DateUtils

class CreateCommentActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    private lateinit var idStore:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)

        idStore = intent.getStringExtra("idStore")

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser


        btnSendCreatorComment.setOnClickListener {
            Log.i("idStore", "CreateCommentActivity: " + idStore)
            val reference = LibraryClass.getFirebase().child("stores").child(idStore).child("feedbacks").push()

            val comment = Comments()
            comment.uidAuthor = currentUser?.uid
            comment.author = currentUser?.displayName
            comment.date = DateUtils.formatDate(System.currentTimeMillis())
            comment.body = editBodyCreatorComment.text.toString()
            comment.title = editTitleCreatorComment.text.toString()
            comment.rating = ratingCreatorComment.rating.toInt()

            reference.setValue(comment).addOnCompleteListener {
                toast("Comentario enviado")
                finish()
            }

        }
    }
}
