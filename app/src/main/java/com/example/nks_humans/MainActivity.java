package com.example.nks_humans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }
    private void init()
    {

        listView=findViewById(R.id.listView);
        arrayList=new ArrayList<>();
        adapter=new CustomArrayAdapter(this,R.layout.list_item_1,arrayList,getLayoutInflater());
        listView.setAdapter(adapter);
        runnable=new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread=new Thread(runnable);
        secThread.start();

    }

    private void getWeb()
    {
        try {
            doc=Jsoup.connect("https://countrymeters.info/ru/World").get();
            Elements tables = doc.getElementsByTag("tbody");
            Element our_table = tables.get(0);
            Elements elements_from_table = our_table.children();
            Element Total= elements_from_table.get(0);
            Elements Total_elements = Total.children();


            for (int i=0;i< our_table.childrenSize();i++)
            {
                ListItemClass items = new ListItemClass();
                items.setData_1(our_table.children().get(i).child(0).text());
                items.setData_2(our_table.children().get(i).child(1).text());
                arrayList.add(items);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}