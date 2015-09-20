package co.gon_htn.gon.firebase_objects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import co.gon_htn.gon.LoginActivity;
import co.gon_htn.gon.MenuActivity;
import co.gon_htn.gon.R;

public class EventsPermissionsDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.events_permissions_header)
                .setMessage(R.string.events_permissions_details)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("user_events"));
                        LoginActivity.importFacebookEvents = true;
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra(LoginActivity.USER_ID_BUNDLE_KEY, AccessToken.getCurrentAccessToken().getUserId());
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.importFacebookEvents = false;
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra(LoginActivity.USER_ID_BUNDLE_KEY, AccessToken.getCurrentAccessToken().getUserId());
                        startActivity(intent);
                    }
                });
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_events_permissions, null);
        builder.setView(view);
        return builder.create();
    }
}
