package com.test.kingofclick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Created by Staff2 on 18.05.2018.
 */

public class GoogleAuth extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity;
    private Context context;
    private LeaderBoard leaderBoard;

    public GoogleAuth(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        mGoogleSignInClient = GoogleSignIn.getClient(activity,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());
    }
    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }
    public void startSignInIntent() {
        activity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), 124);

    }
    public void signOut() {
        mGoogleSignInClient.signOut();
    }
    public LeaderBoard addLeaderBoard(){
        return new LeaderBoard(activity,GoogleSignIn.getLastSignedInAccount(context));
    }
    public GoogleSignInAccount getGoogleSignInAccount(){
        return GoogleSignIn.getLastSignedInAccount(context);
    }



    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }
}
