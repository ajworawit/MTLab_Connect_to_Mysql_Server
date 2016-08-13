package itrmu.worawit.serverjson_okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowActivity extends AppCompatActivity {

    TextView tv_name,tv_label;
    String id,name,label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_label=(TextView) findViewById(R.id.tv_label);


        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("id");
        name=bundle.getString("name");
        label=bundle.getString("label");

        tv_name.setText(name);
        tv_label.setText(label);
    }
}
