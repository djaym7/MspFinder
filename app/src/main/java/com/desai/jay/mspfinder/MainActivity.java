package com.desai.jay.mspfinder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView info,head;
    private MapFragment map;
    String[] gcity,getStudent,getCollege;
    Double[] glng,glat;Double MspLng,MspLat;
    mspdb db = new mspdb(this);
    ListView l;
    Cursor cursor;
    String MspCity;
    //String city,cityname;
    //String[] c;
   // Double lng,lat;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
       // info = (TextView) findViewById(R.id.tvDetails);
        l = (ListView) findViewById(R.id.ListView);
        head = (TextView) findViewById(R.id.tvHead);


        db.open();
        Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
        gcity=db.GetTitleNames();
        glng=db.GetLongitude();
        glat=db.GetLatitude();
        db.close();

       /* for(int i =0;i<173;i++) {

            DataLongOperationAsynchTask loc = new DataLongOperationAsynchTask();
            loc.execute(c[i]);


        }*/

    }


    @Override
    public void onMapReady(final GoogleMap map) {

        Intent intent = getIntent();
        String msp = intent.getStringExtra(Intent.EXTRA_TEXT);

        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
            db.open();
            MspCity = db.getMspCity(msp);
            MspLat = db.GetMspLatitude(MspCity);
            MspLng = db.GetMspLongitude(MspCity);
            Log.d("tags",msp+""+MspCity+""+MspLat+""+MspLng);
            //Toast.makeText(MainActivity.this,msp+""+MspCity+""+MspLat+""+MspLng,Toast.LENGTH_SHORT).show();
            map.addMarker(new MarkerOptions().title(MspCity).position(new LatLng(MspLat, MspLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_rounded_grey_4)));
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(MspLat, MspLng),10);
            map.animateCamera(cu);

        }else {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937,78.9629),4);
            map.animateCamera(cu);
            for (int i = 0; i < gcity.length; i++) {

                map.addMarker(new MarkerOptions().title(gcity[i]).position(new LatLng(glat[i], glng[i])).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_rounded_grey_4)));
                // map.addCircle( new CircleOptions().center(new LatLng(glat[i],glng[i])).radius(10000).fillColor(0x44ff0000).strokeColor(0xffff0000).strokeWidth(8));


           }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {


            @Override
            public boolean onMarkerClick(Marker marker) {
                db.open();
                marker.setVisible(true);
                getStudent = db.GetStudent(marker.getTitle());
                getCollege = db.GetCollege(marker.getTitle());

                head.setText(marker.getTitle());

                myAdapter adapter = new myAdapter(MainActivity.this,getStudent,getCollege);
                l.setAdapter(adapter);
                l.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);

                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);

                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });

                db.close();
                return false;
            }
        });



    /*
        for(int i=0;i<locations.size();i++){
            Toast.makeText(MainActivity.this, locations.get(i).toString(),Toast.LENGTH_SHORT).show();
            map.addMarker(new MarkerOptions()
                    .position(locations.get(i)));
        }*/


    }




    class myAdapter extends ArrayAdapter<String>{
        Context context;
        String[] Snames,Cnames;
        public myAdapter(Context context,String[] Snames,String[] Cnames) {
            super(context, R.layout.info,R.id.tvSname,Snames);
            this.context = context;
            this.Snames = Snames;
            this.Cnames = Cnames;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            myViewHolder holder=null;
            int i = 0;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.info, parent, false);
                holder = new myViewHolder(row);
                row.setTag(holder);

                i++;
            }else{
                holder = (myViewHolder) row.getTag();


            }
            row.getTag();
            holder.Sname.setText(position+1+". "+Snames[position]);
            holder.Cname.setText("\t\t\t"+Cnames[position]);
            return row;
        }
        class myViewHolder{
            String[] Snames,Cnames;
            TextView Sname,Cname;
            myViewHolder(View row)
            {
                 Sname = (TextView) row.findViewById(R.id.tvSname);
                 Cname = (TextView) row.findViewById(R.id.tvCname);
            }

        }
    }
    class comment {
    /*private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address="+params[0]+"&sensor=false");
                Log.d("response",""+response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }




        @Override
        protected void onPostExecute(String... result) {


            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                 lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                 lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                 cityname = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                         .getJSONArray("address_components").getJSONObject(0).getString("long_name");
                mspdb db = new mspdb(MainActivity.this);
                db.open();
                db.AddLatLngToDb(cityname,lat,lng);

                db.close();

                Log.d("CityName + lat+ lon",""+cityname+","+lat+","+lng);
                //Log.d("latitude", "" + lat);
                //Log.d("longitude", "" + lng);

            } catch (JSONException e) {
                e.printStackTrace();

            }


        }
    }



    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.search){

            Intent i = new Intent("com.desai.jay.SEARCHACTIVITY");
            startActivity(i);

        }else if (item.getItemId()==R.id.MenuABout){

            Intent i = new Intent("com.desai.jay.ABOUT");
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}


