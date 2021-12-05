package com.example.cop4655_main_project_burner;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private EditText searchEdt;
    private ArrayList<CurrencyModal> currencyModalArrayList;
    private CurrencyRVAdapter currencyRVAdapter;;
    RecyclerView RVcurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEdt = findViewById(R.id.idEdtCurrency);
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.Home:
                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();break;
                    case R.id.todaysforecast:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    case R.id.map:
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                    case R.id.sevenforecast:
                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }


                return true;

            }
        });
        RVcurrency = findViewById(R.id.idRVcurrency);
        currencyModalArrayList = new ArrayList<>();

        // initializing our adapter class.
        currencyRVAdapter = new CurrencyRVAdapter(currencyModalArrayList, this);

        // setting layout manager to recycler view.
      RVcurrency.setLayoutManager(new LinearLayoutManager(this));

        // setting adapter to recycler view.
        RVcurrency.setAdapter(currencyRVAdapter);

        // calling get data method to get data from API.
        getData();

        // on below line we are adding text watcher for our
        // edit text to check the data entered in edittext.
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // on below line calling a
                // method to filter our array list
                filter(s.toString());
            }
        });
    }

    private void filter(String filter) {
        // on below line we are creating a new array list
        // for storing our filtered data.
        ArrayList<CurrencyModal> filteredlist = new ArrayList<>();
        // running a for loop to search the data from our array list.
        for (CurrencyModal item : currencyModalArrayList) {
            // on below line we are getting the item which are
            // filtered and adding it to filtered list.
            if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        // on below line we are checking
        // weather the list is empty or not.
        if (filteredlist.isEmpty()) {
            // if list is empty we are displaying a toast message.
            Toast.makeText(this, "No currency found..", Toast.LENGTH_SHORT).show();
        } else {
            // on below line we are calling a filter
            // list method to filter our list.
            currencyRVAdapter.filterList(filteredlist);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    private void getData() {
        // creating a variable for storing our string.
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        // creating a variable for request queue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside on response method extracting data
                // from response and passing it to array list
                // on below line we are making our progress
                // bar visibility to gone.
                // extracting data from json.
                JSONArray dataArray = null;
                try {
                    dataArray = response.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObj = null;
                    try {
                        dataObj = dataArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String symbol = null;
                    try {
                        symbol = dataObj.getString("symbol");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String name = null;
                    try {
                        name = dataObj.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject quote = null;
                    try {
                        quote = dataObj.getJSONObject("quote");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject USD = null;
                    try {
                        USD = quote.getJSONObject("USD");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    double price = 0;
                    try {
                        price = USD.getDouble("price");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // adding all data to our array list.
                    currencyModalArrayList.add(new CurrencyModal(name, symbol, price));
                }
                // notifying adapter on data change.
//                    currencyRVAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // displaying error response when received any error.
                Toast.makeText(MainActivity.this, "Something went amiss. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // in this method passing headers as
                // key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "80db3be5-81b3-4f68-9f42-09fdee7ee21b");
                // at last returning headers
                return headers;
            }
        };
        // calling a method to add our
        // json object request to our queue.
        queue.add(jsonObjectRequest);
    }
}
