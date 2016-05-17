package com.andreapivetta.twitterloginview;

import twitter4j.auth.AccessToken;

public interface TwitterLoginListener {

    void onSuccess(AccessToken accessToken);

    void onFailure(int resultCode);

}
