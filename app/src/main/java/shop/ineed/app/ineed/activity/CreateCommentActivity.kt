package shop.ineed.app.ineed.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import org.jetbrains.anko.toast
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Comments
import shop.ineed.app.ineed.domain.util.LibraryClass
import shop.ineed.app.ineed.util.DateUtils

class CreateCommentActivity : BaseActivity(), Validator.ValidationListener {

    @NotEmpty(message = "Conteúdo não pode ser vazio")
    private lateinit var editBody: EditText
    @NotEmpty(message = "Título inválido")
    private lateinit var editTitle: EditText
    private lateinit var rating: RatingBar

    private lateinit var validator: Validator
    private lateinit var auth: FirebaseAuth
    private lateinit var idStore:String
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)

        enableToolbar()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idStore = intent.getStringExtra("idStore")

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        initValues()

        validator = Validator(this)
        validator.setValidationListener(this)
    }

    private fun initValues(){
        editBody = findViewById(R.id.editBodyCreatorComment)
        editTitle = findViewById(R.id.editTitleCreatorComment)
        rating = findViewById(R.id.ratingCreatorComment)
    }

    fun onClickSendComment(view: View){
        validator.validate()
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error in errors!!) {
            val view = error.view
            val message = error.getCollatedErrorMessage(this)

            // Display error messages ;)
            if (view is EditText) {
                (view as EditText).error = message
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onValidationSucceeded() {
        Log.i("idStore", "CreateCommentActivity: " + idStore)
        val reference = LibraryClass.getFirebase().child("stores").child(idStore).child("feedbacks").push()

        val comment = Comments()
        comment.uidAuthor = currentUser.uid
        comment.author = currentUser.displayName
        comment.date = DateUtils.formatDate(System.currentTimeMillis())
        comment.body = editBody.text.toString()
        comment.title = editTitle.text.toString()
        comment.rating = rating.rating.toInt()

        reference.setValue(comment).addOnCompleteListener {
            toast(resources.getString(R.string.send_comment_toast))
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
