package com.lorem_ipsum.utils.volley;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * * This is a Volley custom request with a Map String for authentication
 */
public class VolleyCustomRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> mListener;
    private Map<String, String> mParams;


    /**
     * Params can be null
     * ErrorListener can be null if you want to ignore error
     */

    public VolleyCustomRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.mListener = responseListener;
        this.mParams = params;

        setShouldCache(false);
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

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            //response.data return byte[]
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (mListener != null)
            mListener.onResponse(response);
    }
}