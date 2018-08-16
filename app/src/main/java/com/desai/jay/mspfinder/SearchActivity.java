package com.desai.jay.mspfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {
    Button searchButton;
    EditText searchContent;
    String[] Snames,Cnames;
    String query;
    mspdb db;
    ListView l;
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = (Button) findViewById(R.id.sButton);
        searchContent = (EditText) findViewById(R.id.etSearch);
        l = (ListView) findViewById(R.id.ListViewSearch);
        db = new mspdb(this);
        db.open();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = searchContent.getText().toString();
                Snames=db.SearchMsp(query);
                Cnames=db.SearchCollege(query);
                adapter = new myAdapter(SearchActivity.this,Snames,Cnames);
               l.setAdapter(adapter);
               }
        });
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String msp = adapter.getItem(position);

                Intent i = new Intent(SearchActivity.this,MainActivity.class).putExtra(Intent.EXTRA_TEXT,msp);
                startActivity(i);
            }
        });


    }
    class myAdapter extends ArrayAdapter<String> {
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
}
