package com.test.kingofclick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private ItemShop itemShop;
    private ItemShopAdapter itemShopAdapter;


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

    private static final String userDir = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/KingOFClicks/shop.is";






    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 111: {
                hasPermissions = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                        hasPermissions = false;
                    }
                }

                if (hasPermissions) {
                    finish();
                    startActivity(getIntent());
                } else {
                    finish();
                    //android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void checkPermissions() {
        if (!hasPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!hasPermissions(PERMISSIONS)) {
                    requestPermissions(PERMISSIONS, 111);
                } else {
                    hasPermissions = true;
                }
            }
        }
    }
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


        hasPermissions = hasPermissions(PERMISSIONS);
        if (!hasPermissions) {
            checkPermissions();
        }
        if(hasPermissions){
            String userDir = this.userDir;
            File createDir = new File(userDir);
            createDir.mkdirs();
//            File file = new File(userDir,"kings.res");
//            try {
//                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//                bw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }



    }

    private static String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private boolean hasPermissions = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;

    public boolean hasPermissions(String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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

                for (int i =0;i<itemShop.getItems().size();i++){
                    ItemShop item = itemShop.getItems().get(i);
//                    Log.w("TAG","count" + values[0]);
//                    Log.w("TAG","cost" + itemShop.getItems().get(1).getCost());
                    if (values[0]>= item.getCost() && !item.isEnable()){
                        item.setEnable(true);
                        itemShopAdapter.notifyItemChanged(i, Boolean.FALSE);



                    }
                    if (values[0]<item.getCost()){
                        item.setEnable(false);
                        itemShopAdapter.notifyItemChanged(i, Boolean.FALSE);

                    }
                }
            }
        }.execute();

    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            load();
        } catch (IOException e) {
            Log.w("TAG","ERROR " + e.getMessage());
        }
        passiveCounter();
        if (passiveCount != 0.0) {
//            passiveCounter(passiveCount);
            if (countFromDate > 50) {
                buildAlertDialogCountFromDate();
            }
        }

//        itemShop  = new ItemShop();
        rvShop = findViewById(R.id.rvShop);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvShop.setLayoutManager(llm);
        itemShopAdapter = new ItemShopAdapter(itemShop.getItems());
        rvShop.setAdapter(itemShopAdapter);
        itemShopAdapter.setOnItemClickListener(new ItemShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (count>=itemShop.getItems().get(position).getCost()) {
                    count -= itemShop.getItems().get(position).getCost();
                    if (itemShop.getItems().get(position).getType() == TypeOfShopItem.ACTIVE) {
                        countPerClick += itemShop.getItems().get(position).getAddCount();
                        btnPlus.setText(String.format(Locale.ENGLISH, "+%.2f", (countPerClick)));
                    } else {
                        passiveCount += itemShop.getItems().get(position).getAddCount();
                        txtPassiveCount.setText(String.format(Locale.ENGLISH, "+%.2f", (passiveCount)));
                    }

                    itemShop.getItems().get(position).setCost(itemShop.getItems().get(position).getCost());
                    txtCount.setText(String.format(Locale.ENGLISH, "%.2f", (count)));
                    txtMoney.setText(String.format(Locale.ENGLISH, "%.2f", (count)));
                }
                if (count<itemShop.getItems().get(position).getCost())
                    itemView.findViewById(R.id.btnAddCount).setEnabled(false);
                itemShop.getItems().get(position).setEnable(false);


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
        Log.d("TAG","called pause");
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(Consts.COUNT.toString(), (float) count);
        editor.putFloat(Consts.COUNT_PER_CLICK.toString(), (float) countPerClick);
        editor.putFloat(Consts.PASSIVE_COUNT.toString(), (float) passiveCount);
        editor.putLong(Consts.START_DATE.toString(), Calendar.getInstance().getTimeInMillis());

        try {
            FileOutputStream fos = new FileOutputStream(userDir+"/kings.res");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(itemShop);
            os.close();
        } catch (IOException w) {
            w.printStackTrace();
        }
//        Gson gson = new Gson();
//        String json = gson.toJson(itemShop);
//        editor.putString("itemShop", json);
        editor.apply();
    }

    public void load() throws IOException {
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
        try {
            FileInputStream fis = new FileInputStream(userDir+"/kings.res");
            ObjectInputStream is = new ObjectInputStream(fis);

            itemShop = (ItemShop) is.readObject();
            is.close();
            fis.close();

        }catch (FileNotFoundException e){
            Log.w("TAG","no file " + e.getMessage());
            itemShop=new ItemShop();
        }
        catch (IOException w) {
            w.printStackTrace();
            Log.w("TAG","ERROR " + w.getMessage());
            itemShop=new ItemShop();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("itemShop", newShop());
//        itemShop = gson.fromJson(json,ItemShop.class);

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
    public String newShop(){
        Gson gson = new Gson();

        ItemShop itemShop=new ItemShop();
        String json = gson.toJson(itemShop);
        return json;

    }
}
