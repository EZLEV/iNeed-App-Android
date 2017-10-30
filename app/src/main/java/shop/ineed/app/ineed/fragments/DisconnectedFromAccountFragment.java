package shop.ineed.app.ineed.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.SignInActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisconnectedFromAccountFragment extends Fragment {


    public DisconnectedFromAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_disconnected_from_account, container, false);
        viewRoot.findViewById(R.id.btnSignInAndSignUp).setOnClickListener(view -> getActivity().startActivity(new Intent(getActivity(), SignInActivity.class)));
        return viewRoot;
    }

}
