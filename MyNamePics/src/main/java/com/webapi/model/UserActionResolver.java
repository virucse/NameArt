package com.webapi.model;

import android.view.View;

/**
 * Created by caliber fashion on 9/1/2017.
 */

public interface UserActionResolver {
    public void onRegistrationSuccess();

    public void onLoginSuccess();

    public void onRegistrationLayoutVisible(View view);
}
