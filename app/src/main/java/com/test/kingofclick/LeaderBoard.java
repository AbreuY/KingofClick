package com.test.kingofclick;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Staff2 on 18.05.2018.
 */

public class LeaderBoard extends AppCompatActivity {
    private LeaderboardsClient mLeaderboardsClient;
    private GoogleSignInAccount googleSignInAccount;
    private Activity activity;
    private int RC_UNUSED=1994;

    public LeaderBoard(Activity activity,GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
        this.activity = activity;
        mLeaderboardsClient = Games.getLeaderboardsClient(activity, googleSignInAccount);
    }

    public void onShowLeaderboardsRequested() {
        mLeaderboardsClient.getLeaderboardIntent("CgkI87Thi4gCEAIQAg")
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        activity.startActivityForResult(intent,1994);
                        mLeaderboardsClient = Games.getLeaderboardsClient(activity, googleSignInAccount);
                    }
                });
    }

    public void setmLeaderboardsClient(GoogleSignInAccount googleSignInAccount) {
        this.mLeaderboardsClient =Games.getLeaderboardsClient(activity, googleSignInAccount);
    }
    public void sumbitScore(String id,long i){
        mLeaderboardsClient.submitScore(id,i);
    }


}
