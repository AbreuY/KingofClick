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
    private TextView txtA1, txtP01, txtP1;
    private TextView txtA2, txtP2;
    private TextView txtA3, txtP3;
    private TextView txtA5, txtP5;
    ;

    private VideoAdvertising videoAdvertising;
    private LeaderBoard leaderBoard;
    private GoogleAuth googleAuth;

    private double count;
    private double countPerClick = 1.0;
    private double passiveCount = 0;
    private double countFromDate;

    private boolean bonusX2PerClick = false;

    private BottomSheetBehavior mBottomSheetBehavior;
    private boolean isClickOnShopMenu = false;

    //shop button
    private Button btnActive10, btnActive20, btnActive30, btnActive50,
            btnPassive01, btnPassive10, btnPassive20, btnPassive30, btnPassive50, btnCloseShopMenu;

    //item object
    private ItemShop iSA1, iSA2, iSA3, iSA5, iSP01, iSP1, iSP2, iSP3, iSP5;


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


        //shop Button init
        btnActive10 = findViewById(R.id.btnActive10);
        btnActive20 = findViewById(R.id.btnActive20);
        btnActive30 = findViewById(R.id.btnActive30);
        btnActive50 = findViewById(R.id.btnActive50);

        btnPassive01 = findViewById(R.id.btnPassive01);
        btnPassive10 = findViewById(R.id.btnPassive10);
        btnPassive20 = findViewById(R.id.btnPassive20);
        btnPassive30 = findViewById(R.id.btnPassive30);
        btnPassive50 = findViewById(R.id.btnPassive50);

        btnCloseShopMenu = findViewById(R.id.btnAdd);

        btnActive10.setOnClickListener(this);
        btnActive20.setOnClickListener(this);
        btnActive30.setOnClickListener(this);
        btnActive50.setOnClickListener(this);

        btnPassive01.setOnClickListener(this);
        btnPassive10.setOnClickListener(this);
        btnPassive20.setOnClickListener(this);
        btnPassive30.setOnClickListener(this);
        btnPassive50.setOnClickListener(this);
        btnCloseShopMenu.setOnClickListener(this);

        txtA1 = findViewById(R.id.txtA1);
        txtA2 = findViewById(R.id.txtA2);
        txtA3 = findViewById(R.id.txtA3);
        txtA5 = findViewById(R.id.txtA5);
        txtP01 = findViewById(R.id.txtP01);
        txtP1 = findViewById(R.id.txtP1);
        txtP2 = findViewById(R.id.txtP2);
        txtP3 = findViewById(R.id.txtP3);
        txtP5 = findViewById(R.id.txtP5);


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
                iSA1 = new ItemShop(50,10);
                iSA2 = new ItemShop(100,20);
                iSA3 = new ItemShop(200,30);
                iSA5 = new ItemShop(500,50);
                iSP01 = new ItemShop(50,100);
                iSP1 = new ItemShop(50,10);
                iSP2 = new ItemShop(100,20);
                iSP3 = new ItemShop(200,30);
                iSP5 = new ItemShop(500,50);
                refreshItemShop();
                txtCount.setText(String.format(Locale.ENGLISH, "+%.2f", (count)));
                txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", (passiveCount)));
                btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", (countPerClick)));
                break;
            case R.id.btnShop:

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


                break;
            case R.id.btnActive10:
                if (count >= iSA1.getCost()) {
                    double cost = iSA1.getCost();
                    double procent = (countPerClick / 100) * iSA1.getProcent();
                    count -= cost;
                    countPerClick += procent;
                    txtCount.setText(String.format(Locale.ENGLISH, "%.2f", count));
                    btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", countPerClick));

                    iSA1.setCost(cost);
                    iSA1.setProcent(iSA1.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSA1.getProcent());
                    s += "%";
                    txtA1.setText(s);
                    btnActive10.setText(String.format(Locale.ENGLISH, "%.2f", iSA1.getCost()));

                }
                if (count < iSA1.getCost()) {
                    btnActive10.setEnabled(false);
                }
                break;
            case R.id.btnActive20:
                if (count >= iSA2.getCost()) {
                    double cost = iSA2.getCost();
                    double procent = (countPerClick / 100) * iSA2.getProcent();
                    count -= cost;
                    countPerClick += procent;
                    txtCount.setText(String.format(Locale.ENGLISH, "%.2f", count));
                    btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", countPerClick));

                    iSA2.setCost(cost);
                    iSA2.setProcent(iSA2.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSA2.getProcent());
                    s += "%";
                    txtA2.setText(s);
                    btnActive20.setText(String.format(Locale.ENGLISH, "%.2f", iSA2.getCost()));
                }
                if (count < iSA2.getCost()) {
                    btnActive20.setEnabled(false);
                }
                break;
            case R.id.btnActive30:
                if (count >= iSA3.getCost()) {
                    double cost = iSA3.getCost();
                    double procent = (countPerClick / 100) * iSA3.getProcent();
                    count -= cost;
                    countPerClick += procent;
                    txtCount.setText(String.format(Locale.ENGLISH, "%.2f", count));
                    btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", countPerClick));

                    iSA3.setCost(cost);
                    iSA3.setProcent(iSA3.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSA3.getProcent());
                    s += "%";
                    txtA3.setText(s);
                    btnActive30.setText(String.format(Locale.ENGLISH, "%.2f", iSA3.getCost()));
                }
                if (count < iSA3.getCost()) {
                    btnActive30.setEnabled(false);
                }
                break;
            case R.id.btnActive50:

                if (count >= iSA5.getCost()) {
                    double cost = iSA5.getCost();
                    double procent = (countPerClick / 100) * iSA5.getProcent();
                    count -= cost;
                    countPerClick += procent;
                    txtCount.setText(String.format(Locale.ENGLISH, "%.2f", count));
                    btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", countPerClick));

                    iSA5.setCost(cost);
                    iSA5.setProcent(iSA5.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSA5.getProcent());
                    s += "%";
                    txtA5.setText(s);
                    btnActive50.setText(String.format(Locale.ENGLISH, "%.2f", iSA5.getCost()));
                }
                if (count < iSA5.getCost()) {
                    btnActive50.setEnabled(false);
                }
                break;
            case R.id.btnPassive01:
                if (count >= iSP01.getCost()) {
                    double cost = iSP01.getCost();
                    double procent = (0.1 / 100) * iSP01.getProcent();
                    count -= cost;
                    passiveCount += iSP01.getPlus();
                    txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", passiveCount));

                    iSP01.setPlus(iSP01.getPlus());
                    iSP01.setCost(cost);
                    iSP01.setProcent(iSP01.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSP01.getPlus());
                    txtP01.setText(s);
                    btnPassive01.setText(String.format(Locale.ENGLISH, "%.2f", iSP01.getCost()));
                }


                if (count < 50) {
                    btnPassive01.setEnabled(false);
                }
                break;
            case R.id.btnPassive10:
                if (count >= iSP1.getCost()) {
                    double cost = iSP1.getCost();
                    double procent = (passiveCount / 100) * iSP1.getProcent();
                    count -= cost;
                    passiveCount += procent;
                    txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", passiveCount));

                    iSP1.setCost(cost);
                    iSP1.setProcent(iSP1.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSP1.getProcent());
                    s += "%";
                    txtP1.setText(s);
                    btnPassive10.setText(String.format(Locale.ENGLISH, "%.2f", iSP1.getCost()));
                }
                if (count < iSP1.getCost()) {
                    btnPassive10.setEnabled(false);
                }
                break;
            case R.id.btnPassive20:
                if (count > iSP2.getCost()) {
                    double cost = iSP2.getCost();
                    double procent = (passiveCount / 100) * iSP2.getProcent();
                    count -= cost;
                    passiveCount += procent;
                    txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", passiveCount));

                    iSP2.setCost(cost);
                    iSP2.setProcent(iSP2.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSP2.getProcent());
                    s += "%";
                    txtP2.setText(s);
                    btnPassive20.setText(String.format(Locale.ENGLISH, "%.2f", iSP2.getCost()));
                }
                if (count < iSP2.getCost()) {
                    btnPassive20.setEnabled(false);
                }
                break;
            case R.id.btnPassive30:
                if (count > iSP3.getCost()) {
                    double cost = iSP3.getCost();
                    double procent = (passiveCount / 100) * iSP3.getProcent();
                    count -= cost;
                    passiveCount += procent;
                    txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", passiveCount));

                    iSP3.setCost(cost);
                    iSP3.setProcent(iSP3.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSP3.getProcent());
                    s += "%";
                    txtP3.setText(s);
                    btnPassive30.setText(String.format(Locale.ENGLISH, "%.2f", iSP3.getCost()));
                }


                if (count < iSP3.getCost()) {
                    btnPassive30.setEnabled(false);
                }
                break;
            case R.id.btnPassive50:
                if (count > iSP5.getCost()) {
                    double cost = iSP3.getCost();
                    double procent = (passiveCount / 100) * iSP3.getProcent();
                    count -= cost;
                    passiveCount += procent;
                    txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", passiveCount));

                    iSP5.setCost(cost);
                    iSP5.setProcent(iSP5.getProcent());
                    String s = String.format(Locale.ENGLISH, "+%.2f", iSP5.getProcent());
                    s += "%";
                    txtP5.setText(s);
                    btnPassive50.setText(String.format(Locale.ENGLISH, "%.2f", iSP5.getCost()));
                }
                if (count < iSP5.getCost()) {
                    btnPassive50.setEnabled(false);
                }

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
                if (values[0] >= iSA5.getCost()) {
                    btnActive50.setEnabled(true);
                }else {
                    btnActive50.setEnabled(false);
                }
                if (values[0] >= iSP5.getCost()) {
                    btnPassive50.setEnabled(true);
                }else{
                    btnPassive50.setEnabled(false);
                }

                if (values[0] >= iSA3.getCost()) {
                    btnActive30.setEnabled(true);
                }else{
                    btnActive30.setEnabled(false);
                }
                if (values[0] >= iSP3.getCost()) {
                    btnPassive30.setEnabled(true);
                }else{
                    btnPassive30.setEnabled(false);
                }

                if (values[0] >= iSA2.getCost()) {
                    btnActive20.setEnabled(true);
                }else{
                    btnActive20.setEnabled(false);
                }

                if (values[0] >= iSP2.getCost()) {
                    btnPassive20.setEnabled(true);
                }else{
                    btnPassive20.setEnabled(false);
                }

                if (values[0] >= iSA1.getCost()) {
                    btnActive10.setEnabled(true);
                }else{
                    btnActive10.setEnabled(false);
                }

                if (values[0] >= iSP1.getCost()&& passiveCount>0.d) {
                    btnPassive10.setEnabled(true);
                }else{
                    btnPassive10.setEnabled(false);
                }

                if (values[0] >= iSP01.getCost() ) {
                    btnPassive01.setEnabled(true);
                }else{
                    btnPassive01.setEnabled(false);
                }


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

        Gson gson = new Gson();
        String jsonA1 = gson.toJson(iSA1);
        editor.putString("iSA1", jsonA1);

        String jsonA2 = gson.toJson(iSA2);
        editor.putString("iSA2", jsonA2);

        String jsonA3 = gson.toJson(iSA3);
        editor.putString("iSA3", jsonA3);

        String jsonA5 = gson.toJson(iSA5);
        editor.putString("iSA5", jsonA5);


        String jsonP01 = gson.toJson(iSP01);
        editor.putString("iSP01", jsonP01);

        String jsonP1 = gson.toJson(iSP1);
        editor.putString("iSP1", jsonP1);

        String jsonP2 = gson.toJson(iSP2);
        editor.putString("iSP2", jsonP2);

        String jsonP3 = gson.toJson(iSP3);
        editor.putString("iSP3", jsonP3);

        String jsonP5 = gson.toJson(iSP5);
        editor.putString("iSP5", jsonP5);


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


        //загрузка объектов ItemShop
        Gson gson = new Gson();
        String jsonA1 = sharedPreferences.getString("iSA1", firstTimeInnObj(50, 10));
        iSA1 = gson.fromJson(jsonA1, ItemShop.class);

        String jsonA2 = sharedPreferences.getString("iSA2", firstTimeInnObj(100, 20));
        iSA2 = gson.fromJson(jsonA2, ItemShop.class);

        String jsonA3 = sharedPreferences.getString("iSA3", firstTimeInnObj(200, 30));
        iSA3 = gson.fromJson(jsonA3, ItemShop.class);

        String jsonA5 = sharedPreferences.getString("iSA5", firstTimeInnObj(500, 50));
        iSA5 = gson.fromJson(jsonA5, ItemShop.class);


        String jsonP01 = sharedPreferences.getString("iSP01", firstTimeInnObj(50, 100));
        iSP01 = gson.fromJson(jsonP01, ItemShop.class);

        String jsonP1 = sharedPreferences.getString("iSP1", firstTimeInnObj(50, 10));
        iSP1 = gson.fromJson(jsonP1, ItemShop.class);

        String jsonP2 = sharedPreferences.getString("iSP2", firstTimeInnObj(100, 20));
        iSP2 = gson.fromJson(jsonP2, ItemShop.class);

        String jsonP3 = sharedPreferences.getString("iSP3", firstTimeInnObj(200, 30));
        iSP3 = gson.fromJson(jsonP3, ItemShop.class);

        String jsonP5 = sharedPreferences.getString("iSP5", firstTimeInnObj(500, 50));
        iSP5 = gson.fromJson(jsonP5, ItemShop.class);

        refreshItemShop();

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


    public String firstTimeInnObj(double cost, double procent) {
        Gson gson = new Gson();
        ItemShop itemShop = new ItemShop(cost, procent);
        String json = gson.toJson(itemShop);
        return json;
    }

    public void refreshItemShop() {
        String s = String.format(Locale.ENGLISH, "+%.2f", iSA1.getProcent());
        s += "%";
        txtA1.setText(s);
        btnActive10.setText(String.format(Locale.ENGLISH, "%.2f", iSA1.getCost()));

        String s2 = String.format(Locale.ENGLISH, "+%.2f", iSA2.getProcent());
        s2 += "%";
        txtA2.setText(s2);
        btnActive20.setText(String.format(Locale.ENGLISH, "%.2f", iSA2.getCost()));

        String s3 = String.format(Locale.ENGLISH, "+%.2f", iSA3.getProcent());
        s3 += "%";
        txtA3.setText(s3);
        btnActive30.setText(String.format(Locale.ENGLISH, "%.2f", iSA3.getCost()));

        String s4 = String.format(Locale.ENGLISH, "+%.2f", iSA5.getProcent());
        s4 += "%";
        txtA5.setText(s4);
        btnActive50.setText(String.format(Locale.ENGLISH, "%.2f", iSA5.getCost()));

        String s6 = String.format(Locale.ENGLISH, "+%.2f", iSP01.getPlus());
        txtP01.setText(s6);
        btnPassive01.setText(String.format(Locale.ENGLISH, "%.2f", iSP01.getCost()));

        String s7 = String.format(Locale.ENGLISH, "+%.2f", iSP1.getProcent());
        s7 += "%";
        txtP1.setText(s7);
        btnPassive10.setText(String.format(Locale.ENGLISH, "%.2f", iSP1.getCost()));

        String s8 = String.format(Locale.ENGLISH, "+%.2f", iSP2.getProcent());
        s8 += "%";
        txtP2.setText(s8);
        btnPassive20.setText(String.format(Locale.ENGLISH, "%.2f", iSP2.getCost()));

        String s9 = String.format(Locale.ENGLISH, "+%.2f", iSP3.getProcent());
        s9 += "%";
        txtP3.setText(s9);
        btnPassive30.setText(String.format(Locale.ENGLISH, "%.2f", iSP3.getCost()));

        String s10 = String.format(Locale.ENGLISH, "+%.2f", iSP5.getProcent());
        s10 += "%";
        txtP5.setText(s10);
        btnPassive50.setText(String.format(Locale.ENGLISH, "%.2f", iSP5.getCost()));
    }

}
