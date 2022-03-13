package edu.illinois.cs.cs125.fall2020.mp.network;

import androidx.annotation.NonNull;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
//import edu.illinois.cs.cs125.fall2020.mp.models.Course;

import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.util.Set;

/**
 * Development course API server.
 *
 * <p>Normally you would run this server on another machine, which the client would connect to over
 * the internet. For the sake of development, we're running the server right alongside the app on
 * the same device. However, all communication between the course API client and course API server
 * is still done using the HTTP protocol. Meaning that eventually it would be straightforward to
 * move this server to another machine where it could provide data for all course API clients.
 *
 * <p>You will need to add functionality to the server for MP1 and MP2.
 */
public final class Server extends Dispatcher {
  @SuppressWarnings({"unused", "RedundantSuppression"})
  private static final String TAG = Server.class.getSimpleName();

  private final Map<String, String> summaries = new HashMap<>();

  private MockResponse getSummary(@NonNull final String path) {
    String[] parts = path.split("/");
    if (parts.length != 2) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    String summary = summaries.get(parts[0] + "_" + parts[1]);
    if (summary == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(summary);
  }

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final Map<Summary, String> courses = new HashMap<>();
  private final int x = 4;

  /**.
   * ....
   * @param summary ...
   * @return return certain description
   */
  public String getDescription(final Summary summary) {
    return courses.get(summary);
  }

  /**.
   * ...
   * @param path ...
   * @return return Mockresponse
   */
  public MockResponse getCourse(@NonNull final String path) {
    String[] parts = path.split("/");
    if (parts.length != x) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    Set<Summary> keys = courses.keySet();
    Summary using = null;
    for (Summary s : keys) {
      if (s.getDepartment().equals(parts[2])
              && s.getYear().equals(parts[0])
              && s.getSemester().equals(parts[1])
              && s.getNumber().equals(parts[3])) {
        using = s;
      }
    }

    String course = courses.get(using);
    if (course == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(course);
  }


  private final Map<Summary, String> ratings = new IdentityHashMap<>();
  private final int y = 5;
  private final int z = 7;

  /**
   * ...
   * @param path ...
   * @return ...
   */
  public MockResponse getRating(@NonNull final String path) {
    String[] part1 = path.split("/");
    if (part1.length != x) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] part2 = part1[3].split("[?]client=");
    if (part2.length != 2) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] parts = {part1[0], part1[1], part1[2], part2[0]};
    String using = null;
    double rate = Rating.NOT_RATED;
    boolean find = false;
    boolean findSummary = false;
    String str1 = "";
    Summary s1 = null;
    for (Map.Entry<Summary, String> m: ratings.entrySet()) {
      Summary s = m.getKey();

      if (s.getDepartment().equals(parts[2])
              && s.getYear().equals(parts[0])
              && s.getSemester().equals(parts[1])
              && s.getNumber().equals(parts[3])) {
        findSummary = true;

        //s1 = s;
        str1 = m.getValue();
        if (m.getValue().contains(part2[1])) {
          find = true;
          String[] a = m.getValue().split("\n");
          String[] w = a[z].split(":");
          String c = "\"" + part2[1] + "\",";
          using = m.getValue().replace(w[1], c);
          break;
        } /*else {
          String[] g = a[u].split(":");
          String t = String.valueOf(-1.0);
          using = m.getValue().replace(g[1], t);
          ratings.put(s, using);
        }*/
      }
    }
    if (!findSummary) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    if (!find) {
      String[] q = str1.split("\n");
      String[] k = q[u - 1].split(":");
      String[] w = q[u].split(":");
      using = str1.replace(w[1], String.valueOf(Rating.NOT_RATED));
      using = using.replace(k[1], "\"" + part2[1] + "\",");
      //Summary use = new Summary(parts[0], parts[1], parts[2], parts[3], s1.getTitle());
      //ratings.put(use, using);
    }

    if (using == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(using);
  }

  private final int u = 8;
  private Rating rating1;
  //private final ObjectMapper mapper1 = new ObjectMapper();
  /**
   * ...
   * @param path ...
   * @param body ...
   * @return return
   */
  public MockResponse postRating(@NonNull final String path, @NonNull final String body) {
    String[] part1 = path.split("/");
    if (part1.length != x) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] part2 = part1[3].split("[?]client=");
    if (part2.length != 2) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
    String[] parts = {part1[0], part1[1], part1[2], part2[0]};
    //ObjectMapper mapper1 = new ObjectMapper();
    //Rating rating = null;
    /*try {
      rating = mapper1.readValue(body, Rating.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }*/
    String[] bodyParts = body.split(",");
    String[] bodyPart1 = bodyParts[0].split(":");
    String[] bodyPart2 = bodyParts[1].split(":");

    String a = bodyPart1[1].substring(1, bodyPart1[1].length() - 1);
    double b =  Double.parseDouble(bodyPart2[1].substring(0, bodyPart2[1].length() - 1));
    rating1 = new Rating(a, b);

    Summary s1 = null;
    String str1 = "";
    Set<Summary> keys = ratings.keySet();
    String using = null;
    double rate = Rating.NOT_RATED;
    boolean find = false;
    for (Map.Entry<Summary, String> m: ratings.entrySet()) {
      Summary s = m.getKey();
      if (s.getDepartment().equals(parts[2])
              && s.getYear().equals(parts[0])
              && s.getSemester().equals(parts[1])
              && s.getNumber().equals(parts[3])) {
        s1 = s;
        str1 = m.getValue();
        if (m.getValue().contains(part2[1])) {
          find = true;
          String[] q = m.getValue().split("\n");
          String[] w = q[u].split(":");
          String t = String.valueOf(b);
          using = m.getValue().replace(w[1], t);
          //ratings.remove(s, m.getValue());
         // using = ratings.get(s).replace("-1.0", String.valueOf(rating1.getRating()));
          ratings.put(s, using);
          break;
        }
      }
    }
    if (!find) {
      String[] q = str1.split("\n");
      String[] k = q[u - 1].split(":");
      String[] w = q[u].split(":");
      String t = String.valueOf(b);
      using = str1.replace(w[1], t);
      using = using.replace(k[1], "\"" + a + "\",");
      Summary use = new Summary(parts[0], parts[1], parts[2], parts[3], s1.getTitle());
      ratings.put(use, using);
      //ratings.put(s1, using);
    }

    if (using == null) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    }
    return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(using);
  }


  @NonNull
  @Override
  public MockResponse dispatch(@NonNull final RecordedRequest request) {
    try {
      String path = request.getPath();
      if (path == null || request.getMethod() == null) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
      } else if (path.equals("/") && request.getMethod().equalsIgnoreCase("HEAD")) {
        return new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK);
      } else if (path.startsWith("/summary/")) {
        return getSummary(path.replaceFirst("/summary/", ""));
      } else if (path.startsWith("/course/")) {
        return getCourse(path.replaceFirst("/course/", ""));
      } else if (path.startsWith("/rating/") && request.getMethod().equals("GET")) {
        return getRating(path.replaceFirst("/rating/", ""));
      } else if (path.startsWith("/rating/") && request.getMethod().equals("POST")) {
        String body = request.getBody().readUtf8();
        return postRating(path.replaceFirst("/rating/", ""), body);
      }
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
    } catch (Exception e) {
      return new MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
  }

  private static boolean started = false;

  /**
   * Start the server if has not already been started.
   *
   * <p>We start the server in a new thread so that it operates separately from and does not
   * interfere with the rest of the app.
   */
  public static void start() {
    if (!started) {
      new Thread(Server::new).start();
      started = true;
    }
  }

  private final ObjectMapper mapper = new ObjectMapper();

  /**.
   * ...
   */
  public Server() {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    loadSummary("2020", "fall");
    loadCourses("2020", "fall");
    loadRating("2020", "fall");

    try {
      MockWebServer server = new MockWebServer();
      server.setDispatcher(this);
      server.start(CourseableApplication.SERVER_PORT);

      String baseUrl = server.url("").toString();
      if (!CourseableApplication.SERVER_URL.equals(baseUrl)) {
        throw new IllegalStateException("Bad server URL: " + baseUrl);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void loadSummary(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + "_summary.json";
    String json =
        new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    summaries.put(year + "_" + semester, json);
  }

  @SuppressWarnings("SameParameterValue")
  private void loadCourses(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + ".json";
    String json =
        new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    try {
      JsonNode nodes = mapper.readTree(json);
      for (Iterator<JsonNode> it = nodes.elements(); it.hasNext(); ) {
        JsonNode node = it.next();
        Summary course = mapper.readValue(node.toString(), Summary.class);
        courses.put(course, node.toPrettyString());
      }
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  private void loadRating(@NonNull final String year, @NonNull final String semester) {
    String filename = "/" + year + "_" + semester + "_summary.json";
    String json =
            new Scanner(Server.class.getResourceAsStream(filename), "UTF-8").useDelimiter("\\A").next();
    try {
      JsonNode nodes = mapper.readTree(json);
      for (Iterator<JsonNode> it = nodes.elements(); it.hasNext(); ) {
        JsonNode node = it.next();
        Summary rating = mapper.readValue(node.toString(), Summary.class);
        String a = node.toPrettyString();
        a = a.substring(0, a.length() - 2) + ",\n\"id\" : \"" + Rating.DEFAULT_CLIENTID
                  + "\",\n\"rating\":" + Rating.NOT_RATED + "\n}";
        ratings.put(rating, a);
      }
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }
}
