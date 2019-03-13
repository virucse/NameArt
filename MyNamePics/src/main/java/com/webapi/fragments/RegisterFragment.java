package com.webapi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.formationapps.nameart.App;
import com.formationapps.nameart.R;
import com.webapi.model.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caliber fashion on 8/27/2017.
 */

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText name = (EditText) view.findViewById(R.id.name_edit_register);
        final EditText email = (EditText) view.findViewById(R.id.email_edit_register);
        final EditText password = (EditText) view.findViewById(R.id.password_edit_register);
        EditText confirmPass = (EditText) view.findViewById(R.id.confirm_password_edit_register);
        Button register = (Button) view.findViewById(R.id.register_button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://13.126.217.142/auth/register";
                StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("registrationTAG", "onResponse: " + response);
                        TextView tv = (TextView) view.findViewById(R.id.responseText);
                        parseResponseJson(response, tv);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap();
                        params.put("name", "" + name.getText().toString());
                        params.put("email", "" + email.getText().toString());
                        params.put("password", "" + password.getText().toString());
                        UserProfile up =UserProfile.getInstance();
                        up.setUserName(name.getText().toString());
                        up.setUserId(email.getText().toString());
                        up.setPassword(password.getText().toString());
                        return params;
                    }
                };
                App.getInstance().addToRequestQueue(stringReq, "USERLOGINREGISTRATION");
            }
        });
    }

    private void parseResponseJson(String response, TextView tv) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
            if (status.equals("success")) {
                String token = jsonObject.getString("token");
                int userId = jsonObject.getInt("userid");
                UserProfile up = UserProfile.getInstance();
                up.setUserIdValue(userId);
                up.setToken(token);
            } else {

            }
            tv.setText(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
