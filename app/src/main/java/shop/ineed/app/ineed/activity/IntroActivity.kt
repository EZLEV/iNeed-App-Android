package shop.ineed.app.ineed.activity

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.MessageButtonBehaviour
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.Manifest
import android.os.Bundle
import android.view.View
import shop.ineed.app.ineed.R

class IntroActivity : MaterialIntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val neededPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION )

        val possiblePermissions = arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE )

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.secondaryDarkColor)
                .buttonsColor(R.color.blue_btn_bg_color)
                .image(R.drawable.ic_google)
                .title("ola mundo")
                .description("ola mundo")
                .build(),
                MessageButtonBehaviour(View.OnClickListener { showMessage("ok") }, ""
                ))

        addSlide( SlideFragmentBuilder()
                .backgroundColor( R.color.secondaryDarkColor)
                .buttonsColor( R.color.secondaryDarkColor)
                .neededPermissions( neededPermissions )
                .image( R.drawable.ic_google )
                .title("" )
                .description( "" )
                .build() )

        addSlide( SlideFragmentBuilder()
                .backgroundColor(R.color.secondaryDarkColor )
                .buttonsColor( R.color.secondaryDarkColor )
                .possiblePermissions( possiblePermissions )
                .image( R.drawable.ic_google )
                .title( "" )
                .description( "" )
                .build() )

    }
}
