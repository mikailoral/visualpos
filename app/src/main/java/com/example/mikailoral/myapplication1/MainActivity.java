package com.example.mikailoral.myapplication1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mikailoral.myapplication1.api.APIUtil;
import com.example.mikailoral.myapplication1.api.ApiService;
import com.example.mikailoral.myapplication1.api.model.AccountDto;
import com.example.mikailoral.myapplication1.api.response.AccessTokenResponse;
import com.example.mikailoral.myapplication1.api.OAuthService;
import com.example.mikailoral.myapplication1.api.response.ResponseForAccountList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button gonderButton ;
    EditText miktar;

    TextToSpeech textToSpeech;

    public static MainActivity MAINACTIVITY_INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });

        MAINACTIVITY_INSTANCE = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        gonderButton = findViewById(R.id.button3);
        miktar = findViewById(R.id.editText3);

        gonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paraGonder();
            }
        });


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

    private void paraGonder(){
        OkHttpClient client = new OkHttpClient();
        Request request = ss();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                String s = "FAIL";
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    String s = "OK";
                }
            }
        });

    }



    public Request ss(){

        String girilenMiktar = miktar.getText().toString();

        String jsonMessage = "  {" +
                "  \"data\": {" +
                "    \"title\": \"Ödeme istenen miktar\"," +
                "    \"detail\": \""+ girilenMiktar +"\"" +
                "  }," +
                "  \"to\": \"cPbMzbe4uwI:APA91bFVcag6j14TZGT_fW3eFgRe2tU7E3XVpeor_owHw0o2EDIQ4OkvlXCr4hexkF3W175kXUxQQURg-aJpjL_3tJ00FYIWyr6gXraT2U3So48Z9wUgx3cVknCnmIm1tIZ5a4fi-lgT\"" +
                "}  ";


        MediaType MEDIA_TYPE = MediaType.parse("application/json");


        RequestBody body = RequestBody.create(MEDIA_TYPE, jsonMessage);

        final Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAA7frz3DM:APA91bExKtwj-pefaswEMwS3_y3gMI7nxNhKGi4Oq7dBoYMb99XWBHLRgwxzIYKr6aE2fK8e5wrLaW8U4vgIpNzVlSrvCEyjbo-Q3Uk9_I_BV7kjE051y2Qqp_t-CzdzC_5Cmnm2LFLH")
                .build();

        return request;
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
                    List<AccountDto> list = response.body().getResponse().getRet().getAccountList();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            final ArrayList<String> matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


            if (requestCode == 123) {
                boolean match = false;

                for (String m :
                        matches_text) {
                    if (m.toLowerCase().equals("evet")) {
                        match = true;
                        break;
                    }
                }

                if (match) {
                    Log.d("dorlion", "begin transaction");
                    //callWs();
                    textToSpeech.speak("Ödemeniz Tamamlanmıştır.",TextToSpeech.QUEUE_FLUSH,null,null);
                } else {
                    Log.w("dorlion", "transaction cancelled");
                    textToSpeech.speak("İşleminiz isteğiniz üzerine iptal edilmiştir.",TextToSpeech.QUEUE_FLUSH,null,null);
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void voiceActivity(String ucret) {
        textToSpeech.speak("Hesabınızdan " + ucret + " lira ücret kesilecektir. Onaylıyorsanız evet onaylamıyorsanız hayır ",TextToSpeech.QUEUE_FLUSH, null ,null);
        while(true){
            if (!textToSpeech.isSpeaking()){
                break;
            }
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, 123);

    }
}
