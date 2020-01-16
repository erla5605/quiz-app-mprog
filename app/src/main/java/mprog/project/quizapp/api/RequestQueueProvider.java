package mprog.project.quizapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueProvider {

    private static RequestQueueProvider instance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestQueueProvider(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueProvider getInstance(Context context){
        if(instance == null){
            instance = new RequestQueueProvider(context);
        }

        return instance;
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
