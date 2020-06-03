package com.labstechnology.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class DialogLogOut extends DialogFragment {
    private static final String TAG = "DialogLogOut";

    private MaterialButton btnYesLogMeOut;
    private Button btnNaah;

    private FirebaseAuth mAuth;

    private Utils utils;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_log_out, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        initViews(view);
        utils = new Utils(getActivity());
        mAuth = FirebaseAuth.getInstance();

        btnYesLogMeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.setSignedIn(false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mAuth.signOut();

            }
        });

        btnNaah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: called");

        btnNaah = (Button) view.findViewById(R.id.btnNaah);
        btnYesLogMeOut = (MaterialButton) view.findViewById(R.id.btnYesLogMeOut);
    }
}
