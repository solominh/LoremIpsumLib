package com.lorem_ipsum.utils.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class VolleyByteArrayRequest extends Request<byte[]> {

    private Response.Listener<byte[]> mListener;
    private Map<String, String> mParams;

    public VolleyByteArrayRequest(int method, String url, Map<String, String> params, Response.Listener<byte[]> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        mListener = responseListener;
        mParams = params;

        // Cache data
        setShouldCache(true);
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }
}
