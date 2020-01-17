package mprog.project.quizapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueProvider {

    private static RequestQueueProvider instance;
    private RequestQueue requestQueue;
    private static Context context;

    // Constructor for the RequestQueueProvider.
    private RequestQueueProvider(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    // Provides and instance of the RequestQueueProvider.
    public static synchronized RequestQueueProvider getInstance(Context context){
        if(instance == null){
            instance = new RequestQueueProvider(context);
        }

        return instance;
    }

    // Gets the request queue, creates it if not yet created.
    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    // Adds a request to the request queue.
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
