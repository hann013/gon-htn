package co.gon_htn.gon.firebase_objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.Collection;

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
                        CallbackManager callbackManager = CallbackManager.Factory.create();

                        // check permissions
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/{user-id}/permissions",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        int i = 0;
                                    }
                                }
                        ).executeAsync();

                        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Intent intent = new Intent(getActivity(), MenuActivity.class);
                                intent.putExtra(LoginActivity.USER_ID_BUNDLE_KEY, AccessToken.getCurrentAccessToken().getUserId());
                                startActivity(intent);
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(getActivity().getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("user_events"));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
