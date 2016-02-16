package com.lorem_ipsum.utils.volley;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoangminh on 2/16/16.
 */
public class VolleyExample {

    public final static String url = "https://www.google.com/";

    private void test0() {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private void test1() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        VolleyManager.getInstance().addToRequestQueue(request);
    }

    private void test2() {
        Map<String, String> headers = new HashMap<>();
        headers.put("USERNAME", "solominh");
        headers.put("PASSWORD", "123456");

        VolleyCustomRequest request = new VolleyCustomRequest(Request.Method.GET, url, headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private void test3() {
        Map<String, String> headers = new HashMap<>();
        headers.put("USERNAME", "solominh");
        headers.put("PASSWORD", "123456");

        VolleyObjectRequest<DummyObject> request = new VolleyObjectRequest<>(url, DummyObject.class, headers,
                new Response.Listener<DummyObject>() {
                    @Override
                    public void onResponse(DummyObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private void test4() {
        VolleyByteArrayRequest request = new VolleyByteArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private void test5() {
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
//                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
//                        mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });
    }

    private void test6() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
}
