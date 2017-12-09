package com.example.mikailoral.myapplication1;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mikailoral.myapplication1.api.APIUtil;
import com.example.mikailoral.myapplication1.api.ApiService;
import com.example.mikailoral.myapplication1.api.response.AccessTokenResponse;
import com.example.mikailoral.myapplication1.api.OAuthService;
import com.example.mikailoral.myapplication1.api.response.ResponseForAccountList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView ttxttest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ttxttest = findViewById(R.id.txtTest);

        OAuthService service = new OAuthService();

        try {
            Call<AccessTokenResponse> call = service.getAccessToken(getApplicationContext()).getAccessToken(APIUtil.SCOPE, APIUtil.GRANT_TYPE, APIUtil.CLIENT_ID, APIUtil.CLIENT_SECRET);
            call.clone().enqueue(new Callback<AccessTokenResponse>() {
                @Override
                public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                    APIUtil.ACCEESS_TOKEN = response.body().getAccess_token();
                    getAccountList(MainActivity.this);

                }

                @Override
                public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                    String a = t.getMessage();
                    System.console();
                }
            });

        } catch (Exception ex) {
            System.out.println("Access Token : " + ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void getAccountList(Context context) {
        List<String> currencyList = new ArrayList<>();
        currencyList.add("TL");
        Map<String, Object> map = new ArrayMap<>();

        //TODO: müşteri no yaz
        map.put("customerNumber", "10000972");
        map.put("includedCurrencyCode", currencyList);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(map)).toString());
        ApiService service1 = new ApiService();
        try {

            Call<ResponseForAccountList> a = service1.getInstance(context).getAccountList(body);
            a.clone().enqueue(new Callback<ResponseForAccountList>() {
                @Override
                public void onResponse(Call<ResponseForAccountList> call, Response<ResponseForAccountList> response) {
                        response.isSuccessful();
                    //List<Account> list = response.body().getResponse().getRet().getAccountList();
                    //callback.onSuccess(list);
                }

                @Override
                public void onFailure(Call<ResponseForAccountList> call, Throwable t) {
                    String a = t.getMessage();
                    System.console();
                   // callback.onFail(t.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("Access Token : " + ex.getMessage());
        }
    }
}
