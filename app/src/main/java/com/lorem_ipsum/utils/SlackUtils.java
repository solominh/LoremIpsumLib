package com.lorem_ipsum.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Torin on 21/12/15.
 */
public class SlackUtils {

    private static final String TAG = "SlackUtils";

    public interface SendToSlackCallBack {
        void onSuccess();

        void onError();
    }

    public static void sendMessageToSlack(Context context, String slack_channel, String slack_name, String message, final SendToSlackCallBack callBack) {
        final String slack_team_name = "loremipsum";
        final String slack_token = "37rlKOfjRpEoZFvmhVW7Dz7O";

        if (slack_name == null)
            slack_name = "HoiPos App";

        String channel = "testing";
        if (slack_channel != null && slack_channel.length() > 0)
            channel = slack_channel;
        if (slack_channel == null)
            channel = context.getPackageName();

        String icon_url = "http://by.originally.us/hoipos_icon.png";

        String url = "https://" + slack_team_name + ".slack.com/services/hooks/incoming-webhook?token=" + slack_token;

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("text", message);
        payload.put("channel", "#" + channel);
        payload.put("username", slack_name);
        payload.put("icon_url", icon_url);
        final Map<String, String> params = new HashMap<>();
        params.put("payload", GsonUtils.getGson().toJson(payload));

        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (callBack != null)
                            callBack.onSuccess();

                        if (response != null)
                            Log.e(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null)
                    callBack.onError();

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    int statusCode = response.statusCode;
                    String responseData = new String(response.data);

                    Log.e(TAG, "Status Code: " + statusCode);
                    Log.e(TAG, "Response Data: " + responseData);
                }

                if (error.getMessage() != null)
                    Log.e(TAG, error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the queue
        Volley.newRequestQueue(AppUtils.getAppContext()).add(request);
    }
}
