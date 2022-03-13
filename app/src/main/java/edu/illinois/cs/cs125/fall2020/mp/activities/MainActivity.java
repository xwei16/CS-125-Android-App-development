package edu.illinois.cs.cs125.fall2020.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.adapters.CourseListAdapter;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.databinding.ActivityMainBinding;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import edu.illinois.cs.cs125.fall2020.mp.network.Client;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/** Main activity showing the course summary list. */
public final class MainActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener,
        SortedListAdapter.Callback,
        Client.CourseClientCallbacks,
        CourseListAdapter.Listener {
  @SuppressWarnings({"unused", "RedundantSuppression"})
  private static final String TAG = MainActivity.class.getSimpleName();

  // You should not need to modify these values.
  // At this point you only have data for this semester anyway.
  private static final String DEFAULT_YEAR = "2020";
  private static final String DEFAULT_SEMESTER = "fall";

  // Binding to the layout in activity_main.xml
  private ActivityMainBinding binding;
  // Adapter that connects our list of courses with the list displayed on the display
  private CourseListAdapter listAdapter;
  // List of courses retrieved from the backend server
  @SuppressWarnings("FieldCanBeLocal")
  private List<Summary> courses;

  /**
   * Called when this activity is created.
   *
   * <p>Because this is the main activity for this app, this method is called when the app is
   * started, and any time that this view is shown.
   *
   * @param unused saved instance state, currently unused and always empty or null
   */
  @Override
  // called every time activity get show
  protected void onCreate(final Bundle unused) {
    super.onCreate(unused);
    Log.i("Startup", "onCreat in MainActivity");
    // Server.start();

    // Bind to the layout in activity_main.xml
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Setup the list adapter for the list of courses
    listAdapter = new CourseListAdapter(this, this);
    listAdapter.addCallback(this);
    binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    binding.recyclerView.setAdapter(listAdapter);

    // Retrieve the API client from the application and initiate a course summary request
    CourseableApplication application = (CourseableApplication) getApplication();
    application.getCourseClient().getSummary(DEFAULT_YEAR, DEFAULT_SEMESTER, this);

    // Register this component as a callback for changes to the search view component shown above
    // the course list
    // We use these events to initiate course list filtering
    binding.search.setOnQueryTextListener(this);

    // Register our toolbar
    setSupportActionBar(binding.toolbar);
  }

  /**
   * Callback called when the client has retrieved the list of courses for this component to
   * display.
   *
   * @param year the year that was retrieved
   * @param semester the semester that was retrieved
   * @param setSummaries the summaries returned from the course API client
   */
  @Override
  public void summaryResponse(
      final String year, final String semester, final Summary[] setSummaries) {
    courses = Arrays.asList(setSummaries);
    listAdapter.edit().replaceAll(courses).commit();
  }

  @Override
  public void yourRating(final Summary summary, final Rating rating) {
    Intent startCourseActivity = new Intent(this, CourseActivity.class);
    bundle.putDouble("RATING", rating.getRating());
    bundle.putString("UUID", rating.getId());
    startCourseActivity.putExtras(bundle);
    startActivity(startCourseActivity);
  }

  /**
   * Callback fired when the course list view component begins editing the list of visible courses.
   */
  @Override
  public void onEditStarted() {}

  /**
   * Callback fired when the course list view component completes editing the list of visible
   * courses.
   */
  @Override
  public void onEditFinished() {
    binding.recyclerView.scrollToPosition(0);
  }

  /**
   * Callback fired when the user submits a search query.
   *
   * <p>Because we update the list on each change to the search value, we do not handle this
   * callback.
   *
   * @param unused current query text
   * @return true because we handled this action
   */
  @Override
  public boolean onQueryTextSubmit(final String unused) {
    /*  this.courses = Summary.filter(courses, unused);
    if (this.courses.size() > 0) {
      listAdapter.edit().replaceAll(this.courses).commit();
      return true;
    }
    return false;
    */
    return true;
  }

  /**
   * Callback fired when the user edits the text in the search query box.
   *
   * <p>We handle this by updating the list of visible courses.
   *
   * @param query the text to use to filter the course list
   * @return true because we handled the action
   */
  @Override
  public boolean onQueryTextChange(final String query) {
    String[] temp = query.split(" ");
    List filteredCourse = courses;
    for (int i = 0; i < temp.length; i++) {
      filteredCourse = Summary.filter(filteredCourse, temp[i]);
    }
    listAdapter.edit().replaceAll(filteredCourse).commit();
    return true;
  }

  private static ObjectMapper mapper = new ObjectMapper();

  private static List<String> summaries = new ArrayList<>();
  private static List<Course> courseList = new ArrayList<>();
  private Bundle bundle;

  /**
   * Callback fired when a user clicks on a course in the list view.
   *
   * <p>You will handle this as part of MP1.
   *
   * @param course the course that was clicked
   */
  @Override
  public void onCourseClicked(final Summary course) {
    // Server.start();
    String x = "";
    // lauch new Activity - CourseActivity
    Intent startCourseActivity = new Intent(this, CourseActivity.class);
    bundle = new Bundle();

    // File file = new File("/2020_fall_summary.json");
    // ObjectMapper map = new ObjectMapper();
    // Course a = null;

    String summaryJson =
        new Scanner(MainActivity.class.getResourceAsStream("/2020_fall_summary.json"), "UTF-8")
            .useDelimiter("\\A")
            .next();
    // Load summaries JSON
    // String summaryJson =
    // new Scanner(MP1Test.class.getResourceAsStream("/2020_fall.json"),
    // "UTF-8").useDelimiter("\\A").next();
    JsonNode summaryNodes = null;
    try {
      summaryNodes = mapper.readTree(summaryJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    for (Iterator<JsonNode> it = summaryNodes.elements(); it.hasNext(); ) {
      JsonNode node = it.next();
      summaries.add(node.toPrettyString());
    }
    for (String s : summaries) {
      try {
        Course c = mapper.readValue(s, Course.class);
        if (c.getDepartment().equals(course.getDepartment())
            && c.getTitle().equals(course.getTitle())
            && c.getNumber().equals(course.getNumber())
            && c.getSemester().equals(course.getSemester())
            && c.getYear().equals(course.getYear())) {
          x = s;
          break;
        }
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    bundle.putString("COURSE", x);
    CourseableApplication application = (CourseableApplication) getApplication();
    application.getCourseClient().getRating(course, application.getClientID(), this);
    /*bundle.putString("UUID", application.getClientID());
    //client.getRating(course, application.getClientID(), )
    String client = "";
    try {
      client = mapper.writeValueAsString(application.getCourseClient());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    bundle.putString("CLIENT", client);*/
    startCourseActivity.putExtras(bundle);
    // startActivity(startCourseActivity);
  }
}
