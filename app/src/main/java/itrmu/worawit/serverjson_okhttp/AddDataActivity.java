package itrmu.worawit.serverjson_okhttp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class AddDataActivity extends AppCompatActivity {

    EditText name,label;
    Button submit;
    private JSONArray jarray;
    private JSONObject jobj;
    ServerURL serverURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_form);

        serverURL=new ServerURL();

        name=(EditText)findViewById(R.id.edtName);
        label=(EditText)findViewById(R.id.edtLabel);
        submit=(Button)findViewById(R.id.btnEditSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String values=name.getText().toString();
                String values1=label.getText().toString();

                // syn data && add data
                AddData addData=new AddData();
                addData.execute(values,values1);

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


    }

    // syn add data
    public class AddData extends AsyncTask<String,Void,Void>{
        String result;
        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();

            // post data
            RequestBody body=new FormBody.Builder()
                    .add("major_name", params[0])
                    .add("major_label",params[1])
                    .build();

            // get data
            Request request = builder
                    .url(serverURL.url_add)
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


    @Override
    protected void onPostResume() {
        super.onPostResume();
        name.setText("");
        label.setText("");
    }
}
