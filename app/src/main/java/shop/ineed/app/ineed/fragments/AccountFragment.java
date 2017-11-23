package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.GroupChannelActivity;
import shop.ineed.app.ineed.activity.HomeActivity;
import shop.ineed.app.ineed.activity.FavoritesActivity;
import shop.ineed.app.ineed.activity.ResetPasswordActivity;
import shop.ineed.app.ineed.activity.UpdatePersonalDataActivity;
import shop.ineed.app.ineed.adapter.AdapterSettingsAccount;
import shop.ineed.app.ineed.domain.Settings;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.util.Base64;


/**
 *
 */
public class AccountFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private String uid;
    private User user;
    private ImageView ivProfileUser;
    private List<Settings> settingsList;
    private AdapterSettingsAccount settingsAdapter;
    private AVLoadingIndicatorView progressAccount;
    private ListView listSettings;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        uid = LibraryClass.getUserLogged(getActivity(), User.PROVIDER);

        // Configuraçao do Google Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        view.findViewById(R.id.ivLogOut).setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();

            if(mGoogleApiClient.isConnected()){
                // Google sign out
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }

            LibraryClass.saveUserLogged(getActivity(), User.PROVIDER, "");

            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);

        });

        view.findViewById(R.id.ivSettings).setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), UpdatePersonalDataActivity.class);

            Log.d("TGA", uid);

            intent.putExtra("profile", uid);

            getActivity().startActivity(intent);
        });

        ivProfileUser = view.findViewById(R.id.ivProfileUser);

        progressAccount = view.findViewById(R.id.progressAccount);

        listSettings = (ListView) view.findViewById(R.id.listSettingsAccount);
        ViewCompat.setNestedScrollingEnabled(listSettings, true);
        settingsAdapter = new AdapterSettingsAccount(getContext(), settingsList);
        listSettings.setAdapter(settingsAdapter);
        listSettings.setOnItemClickListener(onClickSettings);


        return view;
    }


    private AdapterView.OnItemClickListener onClickSettings = (adapterView, view, position, l) -> {

        switch (position) {
            case 3:
                getActivity().startActivity(new Intent(getContext(), GroupChannelActivity.class));
                break;
            case 4:
                getActivity().startActivity(new Intent(getContext(), FavoritesActivity.class));
                break;
            case 5:
                getActivity().startActivity(new Intent(getContext(), ResetPasswordActivity.class));
                break;
        }

    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseReference reference = LibraryClass.getFirebase().child("consumers").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (user.getImage() != null) {
                    ivProfileUser.setImageBitmap(Base64.convertToBitmap(user.getImage()));
                }

                settingsList.clear();

                settingsList.add(new Settings(user.getName(), R.drawable.ic_account_circle, 0));
                settingsList.add(new Settings(user.getEmail(), R.drawable.ic_email, 0));
                settingsList.add(new Settings("Mensagens", R.drawable.ic_message, R.drawable.ic_chevron_right));
                settingsList.add(new Settings(getActivity().getResources().getString(R.string.password_recovery), R.drawable.ic_lock, R.drawable.ic_chevron_right));
                settingsList.add(new Settings(getActivity().getResources().getString(R.string.help), R.drawable.ic_help_circle, R.drawable.ic_chevron_right));

                settingsAdapter.notifyDataSetChanged();

                if (progressAccount.getVisibility() == View.VISIBLE) {
                    progressAccount.setVisibility(View.GONE);
                    listSettings.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.log(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
