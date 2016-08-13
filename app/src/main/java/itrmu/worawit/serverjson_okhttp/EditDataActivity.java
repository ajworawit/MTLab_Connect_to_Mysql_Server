package itrmu.worawit.serverjson_okhttp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditDataActivity extends AppCompatActivity {

    ServerURL serverURL;
    EditText edt_name,edt_label;
    Button ok;

    String id,name,label;
    String af_id,af_name,af_label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        serverURL=new ServerURL();

        edt_name=(EditText)findViewById(R.id.edt_edit_name);
        edt_label=(EditText)findViewById(R.id.edt_edit_label);
        ok=(Button)findViewById(R.id.btnEditSubmit);

        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("id");
        name=bundle.getString("name");
        label=bundle.getString("label");

        edt_name.setText(name);
        edt_label.setText(label);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                af_id=id;
                af_name=edt_name.getText().toString();
                af_label=edt_label.getText().toString();

                // syn data && add data
                EditData editData=new EditData();
                editData.execute(af_id,af_name,af_label);


                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });



    }


    // syn edit data
    public class EditData extends AsyncTask<String,Void,Void> {
        String result;
        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();

            // post data
            RequestBody body=new FormBody.Builder()
                    .add("major_id",params[0])
                    .add("major_name", params[1])
                    .add("major_label",params[2])
                    .build();

            // post data
            Request request = builder
                    .url(serverURL.url_edit)
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



}
