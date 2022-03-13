package edu.illinois.cs.cs125.fall2020.mp.network;

import android.util.Log;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;

/**
 * Course API client.
 *
 * <p>You will add functionality to the client as part of MP1 and MP2.
 */
public final class Client {
  private static final String TAG = Client.class.getSimpleName();
  private static final int INITIAL_CONNECTION_RETRY_DELAY = 1000;

  /**
   * Course API client callback interface.
   *
   * <p>Provides a way for the client to pass back information obtained from the course API server.
   */
  public interface CourseClientCallbacks {
    /**
     * Return course summaries for the given year and semester.
     *
     * @param year the year that was retrieved
     * @param semester the semester that was retrieved
     * @param summaries an array of course summaries
     */
    default void summaryResponse(String year, String semester, Summary[] summaries) {}
    /**
     * Return course summaries for the given summary and course.
     *
     * @param summary the summary that was retrieved
     * @param course the course that was retrieved
     */
    default void courseResponse(Summary summary, Course course) {}

    /**
     * Return course summaries for a given summary and rating.
     *
     * @param summary the summary that was retrieved
     * @param rating the rating that was retrieved
     */
    default void yourRating(Summary summary, Rating rating) {}
  }

  /**
   * Retrieve course summaries for a given year and semester.
   *
   * @param year the year to retrieve
   * @param semester the semester to retrieve
   * @param callbacks the callback that will receive the result
   */
  public void getSummary(
      @NonNull final String year,
      @NonNull final String semester,
      @NonNull final CourseClientCallbacks callbacks) {
    String url = CourseableApplication.SERVER_URL + "summary/" + year + "/" + semester;
    StringRequest summaryRequest =
        new StringRequest(
            Request.Method.GET,
            url,
            response -> {
              try {
                Summary[] courses = objectMapper.readValue(response, Summary[].class);
                callbacks.summaryResponse(year, semester, courses);
              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
            },
            error -> Log.e(TAG, error.toString()));
    requestQueue.add(summaryRequest);
  }

  /**
   * Retrieve course summaries for a given year and semester.
   *
   * @param callbacks the callback that will receive the result
   * @param summary the Summary object to retrieve
   */
  public void getCourse(
      @NonNull final Summary summary, @NonNull final CourseClientCallbacks callbacks) {
    String url =
        CourseableApplication.SERVER_URL
            + "course/"
            + summary.getYear()
            + "/"
            + summary.getSemester()
            + "/"
            + summary.getDepartment()
            + "/"
            + summary.getNumber();
    StringRequest courseRequest =
        new StringRequest(
            Request.Method.GET,
            url,
            response -> {
              try {
                Course course = objectMapper.readValue(response, Course.class);
                callbacks.courseResponse(summary, course);
              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
            },
            error -> Log.e(TAG, error.toString()));
    requestQueue.add(courseRequest);
  }

  @SuppressWarnings("checkstyle:ConstantName")
  private static final ObjectMapper MAPPER = new ObjectMapper();
  /**
   * Retrieve course summaries for a given summary and rating.
   *
   * @param summary the Summary object to retrieve
   * @param clientID the clientID to retrieve
   * @param callbacks the callback that will receive the result
   */
  public void getRating(
      @NonNull final Summary summary,
      @NonNull final String clientID,
      @NonNull final CourseClientCallbacks callbacks) {
    // Server.start();
    OkHttpClient client = new OkHttpClient();
    String url =
        CourseableApplication.SERVER_URL
            + "rating/"
            + summary.getYear()
            + "/"
            + summary.getSemester()
            + "/"
            + summary.getDepartment()
            + "/"
            + summary.getNumber()
            + "?client="
            + clientID;
    StringRequest ratingRequest =
        new StringRequest(
            Request.Method.GET,
            url,
            response -> {
              try {
                ObjectNode rating = (ObjectNode) MAPPER.readTree(response);
                double rate = rating.get("rating").asDouble();
                Rating ratingObject = new Rating(clientID, rate);
                callbacks.yourRating(summary, ratingObject);
              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
            },
            error -> Log.e(TAG, error.toString()));
    requestQueue.add(ratingRequest);
  }
  /**
   * . Post the information for a given summary and rating
   *
   * @param summary the Summary object to retrieve
   * @param rating the Rating object to retrieve
   * @param callbacks the callback that will receive the result
   */
  public void postRating(
      @NonNull final Summary summary,
      @NonNull final Rating rating,
      @NonNull final CourseClientCallbacks callbacks) {
    // Server.start();
    OkHttpClient client = new OkHttpClient();
    String url =
        CourseableApplication.SERVER_URL
            + "rating/"
            + summary.getYear()
            + "/"
            + summary.getSemester()
            + "/"
            + summary.getDepartment()
            + "/"
            + summary.getNumber()
            + "?client="
            + rating.getId();
    StringRequest ratingRequest =
            new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                      try {
                        ObjectNode rating1 = (ObjectNode) MAPPER.readTree(String.valueOf(response));
                        double rate = rating1.get("rating").asDouble();
                        String clientID = rating1.get("id").asText();
                        Rating ratingObject = new Rating(clientID, rate);
                        callbacks.yourRating(summary, ratingObject);
                      } catch (JsonProcessingException e) {
                        e.printStackTrace();
                      }
                    },
                    error -> Log.e(TAG, error.toString())) {
      @Override
      public byte[] getBody() throws AuthFailureError {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
          String x = "";
          try {
            x = objectMapper.writeValueAsString(rating);

          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
          return x.getBytes();
        }
    };
    requestQueue.add(ratingRequest);
    /*JSONObject body = new JSONObject();
    try {
      body.put("id", rating.getId());
      body.put("rating", rating.getRating());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    JsonObjectRequest ratingRequest =
        new JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
              ObjectNode rating1 = null;
              try {
                rating1 = (ObjectNode) MAPPER.readTree(String.valueOf(response));
              } catch (JsonProcessingException e) {
                e.printStackTrace();
              }
              double rate = rating1.get("rating").asDouble();
              String clientID = rating1.get("id").asText();
              Rating ratingObject = new Rating(clientID, rate);
              callbacks.yourRating(summary, ratingObject);
            }
        },
           new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
              Log.e(TAG, error.toString());
          }
        });*/

  }

  private static Client instance;

  /**
   * Retrieve the course API client. Creates one if it does not already exist.
   *
   * @return the course API client
   */
  public static Client start() {
    if (instance == null) {
      instance = new Client();
    }
    return instance;
  }

  private static final int MAX_STARTUP_RETRIES = 8;
  private static final int THREAD_POOL_SIZE = 4;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final RequestQueue requestQueue;

  /*
   * Set up our client, create the Volley queue, and establish a backend connection.
   */
  private Client() {
    // Configure the Volley queue used for our network requests
    Cache cache = new NoCache();
    Network network = new BasicNetwork(new HurlStack());
    HttpURLConnection.setFollowRedirects(true);
    requestQueue =
        new RequestQueue(
            cache,
            network,
            THREAD_POOL_SIZE,
            new ExecutorDelivery(Executors.newSingleThreadExecutor()));
    // requestQueue = new RequestQueue(cache, network);

    // Configure the Jackson object mapper to ignore unknown properties
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Make sure the backend URL is valid
    URL serverURL;
    try {
      serverURL = new URL(CourseableApplication.SERVER_URL);
    } catch (MalformedURLException e) {
      Log.e(TAG, "Bad server URL: " + CourseableApplication.SERVER_URL);
      return;
    }

    // Start a background thread to establish the server connection
    new Thread(
            () -> {
              for (int i = 0; i < MAX_STARTUP_RETRIES; i++) {
                try {
                  // Issue a HEAD request for the root URL
                  HttpURLConnection connection = (HttpURLConnection) serverURL.openConnection();
                  connection.setRequestMethod("HEAD");
                  connection.connect();
                  connection.disconnect();
                  // Once this succeeds, we can start the Volley queue
                  requestQueue.start();
                  break;
                } catch (Exception e) {
                  Log.e(TAG, e.toString());
                }
                // If the connection fails, delay and then retry
                try {
                  Thread.sleep(INITIAL_CONNECTION_RETRY_DELAY);
                } catch (InterruptedException ignored) {
                }
              }
            })
        .start();
  }
}
