package com.example.deepak.radio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.deepak.radio.adapters.RadioAdaptor;
import com.example.deepak.radio.classes.RedioBean;
import com.example.deepak.radio.files.MainAct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by deepak on 8/2/2016.
 */
public class AllStation extends Fragment {
    StringRequest stringRequest;
    RecyclerView recyclerView;
    RadioAdaptor radioAdaptor;
    ArrayList<Object> arrayList;
    ProgressBar progressBar;
    HashMap<String, ItemsBean> itemsBeanHashMap;
    ArrayList<RedioBean> categorylist;
    ArrayList<Integer> pos = new ArrayList<>();
    View v;
    int j = 0;
    Spinner sp;
    List<String> spinerlist = new ArrayList<String>();
    ArrayAdapter<String> adp1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v != null)
            return v;
        v = inflater.inflate(R.layout.all_radio, container, false);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.content);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        sp = (Spinner) v.findViewById(R.id.spinner);
        adp1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsBeanHashMap = new HashMap<>();
        categorylist = new ArrayList<>();
        radioAdaptor = new RadioAdaptor((MainAct) getActivity(), arrayList, categorylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(radioAdaptor);
        addItems();
recyclerView.setNestedScrollingEnabled(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.scrollToPosition(pos.get(position)+2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return v;
    }

    public void addItems() {
        stringRequest = new StringRequest(Request.Method.POST, "http://52.76.68.122/radio/radioch.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setValue(new JSONObject(response));

                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("itemToFetch", arrayList.size() + "");
                return stringStringHashMap;
            }
        };
        BackgroundProcess.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void setValue(JSONObject jsonObject) {
        try {
            progressBar.setVisibility(View.GONE);
            if (jsonObject != null) {
                int posi=0;
                for (Iterator<String> iter = jsonObject.keys(); iter.hasNext(); ) {
                    String key = iter.next();
                    Log.e("error", "k " + key);
                    JSONArray response = jsonObject.getJSONArray(key);
                    arrayList.add(key);
                    spinerlist.add(key);
                    pos.add(posi);
                    adp1.add(key);
                    posi++;
                    for (int i = 0; i < response.length(); i++) {
                        posi++;
                        JSONObject jsonObject1 = response.getJSONObject(i);
                        RedioBean itemsBean = new RedioBean();
                        itemsBean.setAudioName(jsonObject1.getString("name"));
                        itemsBean.setAudioUrl(jsonObject1.getString("streamURL"));
                        itemsBean.setAlbum(jsonObject1.getString("desc"));
                        itemsBean.setArtist(jsonObject1.getString("longDesc"));
                        itemsBean.setPosition(j);
                        j++;
                        if (jsonObject1.has("imageURL")) {
                            itemsBean.setImgUrl(jsonObject1.getString("imageURL"));
                        } else
                            itemsBean.setImgUrl("http://i.radionomy.com/documents/radio/000e2e66-2049-413f-9274-16d909fb42ba.s165.jpg");
                        arrayList.add(itemsBean);
                        //itemsBeanHashMap.put(key,itemsBean);
                        categorylist.add(itemsBean);
                    }

                    radioAdaptor.notifyDataSetChanged();

                    sp.setAdapter(adp1);

                }
            } else {

            }
        } catch (Exception e) {
            Log.e("volllllyy", e.toString());
        }

    }

    public ArrayList<String> readURLs(String url) {
        if (url == null) return null;
        ArrayList<String> allURls = new ArrayList<String>();
        try {

            URL urls = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(urls
                    .openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                allURls.add(str);
            }
            in.close();
            return allURls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
