package shop.ineed.app.ineed.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.fragments.DetailsProductFragment;
import shop.ineed.app.ineed.fragments.DisconnectedFromAccountFragment;
import shop.ineed.app.ineed.fragments.GroupChannelListFragment;
import shop.ineed.app.ineed.fragments.GroupChatFragment;


public class GroupChannelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String channelUrl = getIntent().getStringExtra(DetailsProductFragment.Companion.getEXTRA_NEW_CHANNEL_URL());

        if(savedInstanceState == null){
            if(LibraryClass.isUserLogged(getBaseContext())){
                Fragment fragment = GroupChannelListFragment.newInstance();

                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();

                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .commit();
            }else {
                DisconnectedFromAccountFragment fragment = new DisconnectedFromAccountFragment();

                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();

                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .commit();
            }
        }

        if(channelUrl != null) {
            // If started from notification
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
