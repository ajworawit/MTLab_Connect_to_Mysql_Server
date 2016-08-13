package itrmu.worawit.serverjson_okhttp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by worawit on 9/22/2015.
 */
public class LvAdapter extends BaseAdapter {

    String[] menu;
    int[] data_pic;
    Context cont;
    JSONArray jarray;
    JSONObject jobj;

    // constructor method
    public LvAdapter(JSONArray ja, Context ct) {
        //ข้อมูลส่งมาจาก MaintActivity
        this.jarray=ja;
        this.cont=ct;
    }

    @Override
    public int getCount() {
        return jarray.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inf=(LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // ยัด layout_item ใส่ใน adapter
        convertView=inf.inflate(R.layout.layout_item,null);

        // อ้างไปหา id ที่อยู่ใน layout_id
        ImageView img_menu=(ImageView)convertView.findViewById(R.id.imgMenu);
        TextView tv_menu=(TextView)convertView.findViewById(R.id.tvMenu);

        try {
            jobj=jarray.getJSONObject(position);

            // เชตค่าให้กับ view ที่อยู่ใน layout_item
            //img_menu.setImageResource(data_pic[position]);
            tv_menu.setText(""+jobj.getString("major_name").toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }





        return convertView;
    }






}
