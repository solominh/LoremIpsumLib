package com.lorem_ipsum.utils.volley;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lorem_ipsum.utils.GsonUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class VolleyObjectRequest<T> extends Request<T> {

    private final Class<T> mResult;
    private final Map<String, String> mParams;
    private final Response.Listener<T> mListener;

    public VolleyObjectRequest(String url, Class<T> result, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);

        this.mResult = result;
        this.mParams = params;
        this.mListener = listener;

        setShouldCache(false);
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Gson gson = GsonUtils.getGson();
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, mResult), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mParams == null)
            return super.getHeaders();

        Map<String, String> headers = new HashMap<>();

        // Content type
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        // Authorization
        String credentials = String.format("%s:%s", mParams.get("USERNAME"), mParams.get("PASSWORD"));
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
        headers.put("Authorization", auth);

        return headers;
    }
}
