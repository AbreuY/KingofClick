package com.test.kingofclick;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton signInButton;
    private Button btnLeaderBoard;
    private Button btnPlus;
    private Button btnVideo;
    private TextView txtCount;
    private TextView txtPassiveCount;
    private Button btnShopMenu;
    private TextView txtMoney;


    private VideoAdvertising videoAdvertising;
    private LeaderBoard leaderBoard;
    private GoogleAuth googleAuth;

    private double count;
    private double countPerClick = 1.0;
    private double passiveCount = 0;
    private double countFromDate;

    private boolean bonusX2PerClick = false;

//всплывающее меню
    private BottomSheetBehavior mBottomSheetBehavior;
    private boolean isClickOnShopMenu = false;
    private Button btnCloseShopMenu;

    //лист с магазином
    private RecyclerView rvShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //авторизация Google
        googleAuth = new GoogleAuth(this, this);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        //добавление доски лидеров
        btnLeaderBoard = findViewById(R.id.btnLeaderBoard);
        btnLeaderBoard.setOnClickListener(this);

        //инит кнопок
        btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(this);
        btnVideo = findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(this);

        //инит текстовых полей
        txtCount = findViewById(R.id.txtCount);
        txtPassiveCount = findViewById(R.id.txtPassiveCount);
        txtMoney = findViewById(R.id.txtMoney);

        // инициализация рекламы и таблицы рекордов
        if (googleAuth.isSignedIn()) {
            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
            leaderBoard = new LeaderBoard(this, googleSignInAccount);
            signInButton.setVisibility(View.INVISIBLE);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            btnLeaderBoard.setEnabled(false);
        }
        videoAdvertising = new VideoAdvertising(this, getString(R.string.ads_id));

        //test utility
        findViewById(R.id.btnClear).setOnClickListener(this);

        //всплывающее меню



        btnCloseShopMenu = findViewById(R.id.btnAdd);
        btnCloseShopMenu.setOnClickListener(this);

        //нахождение rv
        ItemShop itemShop = new ItemShop();
        rvShop = findViewById(R.id.rvShop);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvShop.setLayoutManager(llm);
        ItemShopAdapter itemShopAdapter = new ItemShopAdapter(itemShop.getItems());
        rvShop.setAdapter(itemShopAdapter);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        btnShopMenu = findViewById(R.id.btnShop);
        btnShopMenu.setOnClickListener(this);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                googleAuth.startSignInIntent();
                btnLeaderBoard.setEnabled(true);
                signInButton.setVisibility(View.INVISIBLE);
                break;

            case R.id.btnLeaderBoard:
                leaderBoard.onShowLeaderboardsRequested();
                break;

            case R.id.btnPlus:

                if (bonusX2PerClick) {
                    count += countPerClick * 2;
                    String s2 = String.format(Locale.ENGLISH, "+%.2f", (countPerClick * 2));
                    btnPlus.setText(s2);
                    txtMoney.setText(String.format(Locale.ENGLISH, "+%.2f", (count)));
                } else {
                    count += countPerClick;
                    String s2 = String.format(Locale.ENGLISH, "+%.2f", (countPerClick));
                    btnPlus.setText(s2);
                    txtMoney.setText(String.format(Locale.ENGLISH, "+%.2f", (count)));
                }
                txtCount.setText(String.format(Locale.ENGLISH, "%.2f", count));

                if (googleAuth.isSignedIn()) {
                    leaderBoard.sumbitScore(getString(R.string.leaderboard_id), (long) count);
                }

                break;
            case R.id.btnVideo:

                videoAdvertising.show(new OnVideoEndListener() {
                    @Override
                    public void onVideoEnd() {
                        btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", (countPerClick * 2)));
                        new CountDownTimer(30000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                bonusX2PerClick = true;
                            }
                            @Override
                            public void onFinish() {
                                bonusX2PerClick = false;
                                videoAdvertising.setVideoOver(false);
                                btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", (countPerClick)));
                            }
                        }.start();

                    }
                });


                break;
            case R.id.btnClear:
                count = 0.0;
                countPerClick = 1.0;
                passiveCount = 0.0;
                break;
            case R.id.btnShop:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btnAdd:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 124) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                leaderBoard = new LeaderBoard(this, account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void passiveCounter() {

        new AsyncTask<Void, Double, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        count += passiveCount;
                        publishProgress(count, countPerClick, passiveCount);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onProgressUpdate(Double... values) {
                super.onProgressUpdate(values);
                String s2 = String.format(Locale.ENGLISH, "%.2f", values[0]);
                txtCount.setText(s2);
                txtMoney.setText(String.format(Locale.ENGLISH, "%.2f", values[0]));
            }
        }.execute();
    }


    @Override
    protected void onStart() {
        super.onStart();
        load();
        passiveCounter();
        if (passiveCount != 0.0) {
//            passiveCounter(passiveCount);
            if (countFromDate > 50) {
                buildAlertDialogCountFromDate();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Consts.COUNT.toString(), (float) count);
        editor.putFloat(Consts.COUNT_PER_CLICK.toString(), (float) countPerClick);
        editor.putFloat(Consts.PASSIVE_COUNT.toString(), (float) passiveCount);
        editor.putLong(Consts.START_DATE.toString(), Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        count = sharedPreferences.getFloat(Consts.COUNT.toString(), 0.f);
        countPerClick = sharedPreferences.getFloat(Consts.COUNT_PER_CLICK.toString(), 1.f);
        passiveCount = sharedPreferences.getFloat(Consts.PASSIVE_COUNT.toString(), 0.f);
        long dateStart = sharedPreferences.getLong(Consts.START_DATE.toString(), 0);
        long dateNow = Calendar.getInstance().getTimeInMillis();
        double betweenDate = (dateNow - dateStart) / 1000;
        if (betweenDate > 5.0) {
            countFromDate = (double) betweenDate * passiveCount;
        }
        String s2 = String.format(Locale.ENGLISH, "%.2f", (count));
        txtCount.setText(s2);
        String s = String.format(Locale.ENGLISH, "+%.2f", (passiveCount));
        txtPassiveCount.setText(s);
        txtMoney.setText(String.format(Locale.ENGLISH, "Ваши деньги: %.2f", (count)));
        btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", (countPerClick)));
    }

    public void buildAlertDialogCountFromDate() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Важное сообщение!")
                .setMessage("Пока вас не было, вы заработали: " + String.format(Locale.ENGLISH, "%.2f", countFromDate))
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                count += countFromDate;
                                countFromDate = 0;
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Удвоить?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoAdvertising.show(new OnVideoEndListener() {
                            @Override
                            public void onVideoEnd() {
                                countFromDate *= 2;
                                count += countFromDate;

                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setMessage("А вот теперь смотрите сколько вы заработали : " + String.format(Locale.ENGLISH, "%.2f", countFromDate));
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("ОГО!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        countFromDate = 0;
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog alert = builder1.create();
                                alert.show();
                            }
                        });


                    }

                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
