package com.proof.androidcodingexercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String UrlData = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ItemList> itemLists;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        itemLists = new ArrayList<>();

        //Handling Swipe Event

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresher);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData(){
        refreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                itemLists.clear();
                try {
                    /* Fetching data from JSON */
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("rows");
                    String title = jsonObject.getString("title");

                    //Setting title of Action Bar based on JSON data
                    getSupportActionBar().setTitle(title);

                    //Adding items from JSON Data to ArrayList
                    for(int i=0;i<array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        ItemList itemList = new ItemList(
                                o.getString("title"),
                                o.getString("description"),
                                o.getString("imageHref")
                        );
                        itemLists.add(itemList);
                    }
                    adapter = new Adapter(itemLists, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
