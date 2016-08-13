package itrmu.worawit.serverjson_okhttp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public TextView str;
    private JSONArray jarray;
    private JSONObject jobj;
    ListView lv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String key="0";

    ServerURL serverURL;


    private static final String[] menu ={"Update", "Delete"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverURL=new ServerURL();
       // key=getIntent().getStringExtra("key");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        lv=(ListView)findViewById(R.id.listView);

        // connect to server
        AsynTaskGetData load=new AsynTaskGetData();
        load.execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jobj;
                try {
                    jobj=jarray.getJSONObject(position);

                    Intent intent=new Intent(getApplicationContext(),ShowActivity.class);
                    intent.putExtra("id",jobj.getString("major_id"));
                    intent.putExtra("name",jobj.getString("major_name"));
                    intent.putExtra("label",jobj.getString("major_label"));
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int item=position;

                dialogList(item);

                return true;
            }
        });



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

    }

    // Refresh list
    void refreshItems() {
        ( new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // โหลดข้อมูลใหม่
                AsynTaskGetData load=new AsynTaskGetData();
                load.execute();

//                mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, menu);
//                lv.setAdapter(mAdapter);
                // โหลดเสร็จแล้ว
                onRefreshCompleted();
            }
        }, 1000);
    }

    private void onRefreshCompleted() {
        // สั่งให้ SwipeRefreshLayout หยุดทำงาน
        mSwipeRefreshLayout.setRefreshing(false);
    }


    // show dialog
    private void dialogList(final int item){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Menu");
        builder.setItems(menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String selected = menu[i];

                switch (i){
                    case  0 :
                        JSONObject jobj;
                        try {
                            jobj=jarray.getJSONObject(item);

                            // Toast.makeText(getApplicationContext(),"item:"+jobj.getString("major_id"),Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(getApplicationContext(),EditDataActivity.class);
                            intent.putExtra("id",jobj.getString("major_id"));
                            intent.putExtra("name",jobj.getString("major_name"));
                            intent.putExtra("label",jobj.getString("major_label"));
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1 :
                        dialogConfirm(item);
                        break;
                        }
                dialog.dismiss();
            }

        });
        builder.create();
        builder.show();

    }

    private void dialogConfirm(final int item){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Confirm to Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),
                        "Yes", Toast.LENGTH_SHORT).show();
                JSONObject jb;
                try {
                    jb=jarray.getJSONObject(item);
                    String id_values=jb.getString("major_id");

                    // syn data && delete data
                    DelData del=new DelData();
                    del.execute(id_values);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // syn to server
                AsynTaskGetData load=new AsynTaskGetData();
                load.execute();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();

        // สุดท้ายอย่าลืม show() ด้วย
        builder.show();
    }






    // Syn Data and set adapter
    public  class AsynTaskGetData extends  AsyncTask<String,Void,Void>{
        String result;
        @Override
        protected Void doInBackground(String... params) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();

            // get data
            Request request = builder
                    .url(serverURL.url)
                    .build();

            Response response;
            try {
                response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    result = response.body().string();
                }else{
                    result=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }// doInBackground

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(result!=null){
                try {
                    jarray = new JSONArray(result);

                    // set adapter
                    LvAdapter adapter=new LvAdapter(jarray,getApplicationContext());
                    lv.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(),"data :"+result,Toast.LENGTH_SHORT).show();
            }


        }//onPostExecute


    }//class


    // syn edit data
    public class DelData extends AsyncTask<String,Void,Void> {
        String result;
        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();

            // post data
            RequestBody body=new FormBody.Builder()
                    .add("major_id",params[0])
                    .build();

            // post data
            Request request = builder
                    .url(serverURL.url_del)
                    .post(body)
                    .build();

            Response response;
            try {
                response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    result = response.body().string();
                }else{
                    result=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }



}//class






