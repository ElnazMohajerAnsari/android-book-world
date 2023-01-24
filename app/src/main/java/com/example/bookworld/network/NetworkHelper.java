package com.example.bookworld.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookworld.R;
import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.Genre;
import com.example.bookworld.beans.Review;
import com.example.bookworld.beans.User;
import com.example.bookworld.entities.GenreEntity;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkHelper {

    private static final String TAG = "NetworkHelper";
    private static NetworkHelper instance = null;
    private Context context;
    private Gson gson = new Gson();
    private RequestQueue requestQueue;
    private String appId;
    private String apiKey;
    private String hostUrl;

    private NetworkHelper(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.appId = context.getString(R.string.appId);
        this.apiKey = context.getString(R.string.apiKey);
        this.hostUrl = context.getString(R.string.hostUrl);
    }

    public static NetworkHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkHelper(context);
        }
        return instance;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return ((net != null) && net.isConnected());
    }

    private void printVolleyErrorDetails(VolleyError error) {
        NetworkResponse errResponse = (error != null) ? error.networkResponse : null;
        int statusCode = 0;
        String data = "";
        if (errResponse != null) {
            statusCode = errResponse.statusCode;
            byte[] bytes = errResponse.data;
            data = (bytes != null) ? new String(bytes, StandardCharsets.UTF_8) : "";
        }
        Log.e(TAG, "Volley error with status code " + statusCode + " received with this message: " + data);
    }

    public void signupUser(final User user, final ResultListener<User> listener) {
        if (!isNetworkConnected()) {
            Error error = new Error(context.getString(R.string.network_connection_error));
            listener.onResult(new Result<User>(null, null, error));
            return;
        }

        String url = hostUrl + "/users";
        String userJson = null;
        try {
            userJson = gson.toJson(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            Error error = new Error(context.getString(R.string.network_json_error));
            listener.onResult(new Result<User>(null, null, error));
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    Error error = new Error(context.getString(R.string.network_general_error));
                    listener.onResult(new Result<User>(null, null, error));
                    return;
                }

                User resultUser = null;
                try {
                    resultUser = gson.fromJson(response, new TypeToken<User>() {
                    }.getType());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Error error = new Error(context.getString(R.string.network_json_error));
                    listener.onResult(new Result<User>(null, null, error));
                    return;
                }
                listener.onResult(new Result<User>(resultUser, null, null));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printVolleyErrorDetails(error);
                Error err = new Error(context.getString(R.string.network_general_error));
                listener.onResult(new Result<User>(null, null, err));
            }
        };
        final String jsonStr = userJson;
        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Parse-Application-Id", appId);
                headers.put("X-Parse-REST-API-Key", apiKey);
                headers.put("X-Parse-Revocable-Session", "1");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonStr.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(request);
    }


    public void signInUser(final User user, final ResultListener<User> listener) {
        if (!isNetworkConnected()) {
            Error error = new Error(context.getString(R.string.network_connection_error));
            listener.onResult(new Result<User>(null, null, error));
            return;
        }

        String url = hostUrl + "/login?username=" + user.getUsername() + "&password=" + user.getPassword();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User signIn response: " + response);
                if (TextUtils.isEmpty(response)) {
                    Error error = new Error(context.getString(R.string.network_general_error));
                    listener.onResult(new Result<User>(null, null, error));
                    return;
                }

                User resultUser = null;
                try {
                    resultUser = gson.fromJson(response, new TypeToken<User>() {
                    }.getType());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Error error = new Error(context.getString(R.string.network_json_error));
                    listener.onResult(new Result<User>(null, null, error));
                    return;
                }

                listener.onResult(new Result<User>(resultUser, null, null));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printVolleyErrorDetails(error);
                Error err = new Error(context.getString(R.string.network_general_error));
                listener.onResult(new Result<User>(null, null, err));
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Parse-Application-Id", appId);
                headers.put("X-Parse-REST-API-Key", apiKey);
                headers.put("X-Parse-Revocable-Session", "1");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    //----------------------------------------------------------------------------------------------------------
    public void readGenres(final ResultListener<List<Genre>> listener) {
        if (!isNetworkConnected()) {
            Error error = new Error(context.getString(R.string.network_connection_error));
            listener.onResult(new Result<List<Genre>>(null, null, error));
            return;
        }

        String url = hostUrl + "/classes/genre";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            String str = null;

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Genre reading response: " + response);
                if (TextUtils.isEmpty(response)) {
                    Error error = new Error(context.getString(R.string.network_general_error));
                    listener.onResult(new Result<List<Genre>>(null, null, error));
                    return;
                }

                str = response.substring(11);
                str = removeLastChar(str);

                List<Genre> resultGenre = null;
                try {
                    resultGenre = gson.fromJson(str, new TypeToken<List<Genre>>() {
                    }.getType());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Error error = new Error(context.getString(R.string.network_json_error));
                    listener.onResult(new Result<List<Genre>>(null, null, error));
                    return;
                }

                listener.onResult(new Result<List<Genre>>(resultGenre, null, null));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printVolleyErrorDetails(error);
                Error err = new Error(context.getString(R.string.network_general_error));
                listener.onResult(new Result<List<Genre>>(null, null, err));
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Parse-Application-Id", appId);
                headers.put("X-Parse-REST-API-Key", apiKey);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public String removeLastChar(String str) {
        if (str != null && str.length() > 5 && str.charAt(str.length() - 1) == '}') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    //------------------------------------------------------------------------------------
    public void readBooks(final ResultListener<List<Book>> listener) {
        if (!isNetworkConnected()) {
            Error error = new Error(context.getString(R.string.network_connection_error));
            listener.onResult(new Result<List<Book>>(null, null, error));
            return;
        }

        String url = hostUrl + "/classes/book";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            String str = null;

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Books reading response: " + response);
                if (TextUtils.isEmpty(response)) {
                    Error error = new Error(context.getString(R.string.network_general_error));
                    listener.onResult(new Result<List<Book>>(null, null, error));
                    return;
                }
                str = response.substring(11);
                str = removeLastChar(str);
                List<Book> resultBook = null;
                try {
                    resultBook = gson.fromJson(str, new TypeToken<List<Book>>() {
                    }.getType());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Error error = new Error(context.getString(R.string.network_json_error));
                    listener.onResult(new Result<List<Book>>(null, null, error));
                    return;
                }

                listener.onResult(new Result<List<Book>>(resultBook, null, null));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printVolleyErrorDetails(error);
                Error err = new Error(context.getString(R.string.network_general_error));
                listener.onResult(new Result<List<Book>>(null, null, err));
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Parse-Application-Id", appId);
                headers.put("X-Parse-REST-API-Key", apiKey);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    //------------------------------------------------------------------------------------------------------

    public void insertReview(final Review review, final User currentUser, final ResultListener<Review> listener) {
        if (!isNetworkConnected()) {
            Error error = new Error(context.getString(R.string.network_connection_error));
            listener.onResult(new Result<Review>(null, null, error));
            return;
        }

        String url = hostUrl + "/classes/review";
        String reviewJson = null;
        try {
            reviewJson = gson.toJson(review);
        } catch (Exception ex) {
            ex.printStackTrace();
            Error error = new Error(context.getString(R.string.network_json_error));
            listener.onResult(new Result<Review>(null, null, error));
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Review insert response: " + response);
                if (TextUtils.isEmpty(response)) {
                    Error error = new Error(context.getString(R.string.network_general_error));
                    listener.onResult(new Result<Review>(null, null, error));
                    return;
                }

                Review resultReview = null;
                try {
                    resultReview = gson.fromJson(response, new TypeToken<Review>() {
                    }.getType());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Error error = new Error(context.getString(R.string.network_json_error));
                    listener.onResult(new Result<Review>(null, null, error));
                    return;
                }

                listener.onResult(new Result<Review>(resultReview, null, null));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printVolleyErrorDetails(error);
                Error err = new Error(context.getString(R.string.network_general_error));
                listener.onResult(new Result<Review>(null, null, err));
            }
        };

        final String jsonStr = reviewJson;
        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Parse-Application-Id", appId);
                headers.put("X-Parse-REST-API-Key", apiKey);
                headers.put("X-Parse-Session-Token", currentUser.getSessionToken());
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonStr.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(request);
    }

    public void readReviews(final ResultListener<List<Review>> listener) {
        if (!isNetworkConnected()) {
            Error error = new Error(context.getString(R.string.network_connection_error));
            listener.onResult(new Result<List<Review>>(null, null, error));
            return;
        }

        String url = hostUrl + "/classes/review";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            String str = null;

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reviews reading response: " + response);
                if (TextUtils.isEmpty(response)) {
                    Error error = new Error(context.getString(R.string.network_general_error));
                    listener.onResult(new Result<List<Review>>(null, null, error));
                    return;
                }
                str = response.substring(11);
                str = removeLastChar(str);
                List<Review> resultReview = null;
                try {
                    resultReview = gson.fromJson(str, new TypeToken<List<Review>>() {
                    }.getType());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Error error = new Error(context.getString(R.string.network_json_error));
                    listener.onResult(new Result<List<Review>>(null, null, error));
                    return;
                }

                listener.onResult(new Result<List<Review>>(resultReview, null, null));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printVolleyErrorDetails(error);
                Error err = new Error(context.getString(R.string.network_general_error));
                listener.onResult(new Result<List<Review>>(null, null, err));
            }
        };

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Parse-Application-Id", appId);
                headers.put("X-Parse-REST-API-Key", apiKey);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
