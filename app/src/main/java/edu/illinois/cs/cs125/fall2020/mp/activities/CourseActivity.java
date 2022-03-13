package edu.illinois.cs.cs125.fall2020.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.application.CourseableApplication;
import edu.illinois.cs.cs125.fall2020.mp.models.Course;
import edu.illinois.cs.cs125.fall2020.mp.models.Rating;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import edu.illinois.cs.cs125.fall2020.mp.network.Client;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 * . receives a serialized Summary as the COURSE field when it starts and displays detailed
 * information about that couse
 */

public class CourseActivity extends AppCompatActivity implements Client.CourseClientCallbacks {
  private static final String TAG = CourseActivity.class.getSimpleName();
  private TextView myText;
  private TextView description;
  private static ObjectMapper mapper = new ObjectMapper();
  private Course myCourse;
  private RatingBar ratingBar;
  private Client client;
  private CourseActivity thisCourseActivity;
  private static List<String> summaries = new ArrayList<>();
  private double myRating;
  private String clientID;

  /**
   * ...
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    // Log.d(TAG, intent.getStringExtra("TITLE"));
    // Bind to the layout in activity_main.xml
    DataBindingUtil.setContentView(this, R.layout.activity_course);
    Bundle bundle = intent.getExtras();

    String a = bundle.getString("COURSE");
    try {
      myCourse = mapper.readValue(a, Course.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    getCourseDescription(myCourse);
    CourseableApplication application = (CourseableApplication) getApplication();
    application.getCourseClient().getRating(myCourse, application.getClientID(), this);
    myText = findViewById(R.id.text_course);
    myText.setText(
        myCourse.getDepartment() + " " + myCourse.getNumber() + ": " + myCourse.getTitle());
    description = findViewById(R.id.text_description);
    description.setText(myCourse.getDescription());
    ratingBar = findViewById(R.id.rating);
    // double rating = bundle.getDouble("RATING");
    //ratingBar.setRating((float) myRating);
    // tring clientID = bundle.getString("UUID");
    thisCourseActivity = this;
    ratingBar.setOnRatingBarChangeListener(
        new RatingBar.OnRatingBarChangeListener() {
          @Override
          public void onRatingChanged(
              final RatingBar ratingBar1, final float rating, final boolean fromUser) {
            CourseableApplication application = (CourseableApplication) getApplication();
              application
                  .getCourseClient()
                  .postRating(myCourse, new Rating(clientID, rating), thisCourseActivity);
          }
        });
  }
  /**...
   * ..
   * @param summary the summary that was retrieved
   * @param rating the rating that was retrieved
   */
  @Override
  public void yourRating(final Summary summary, final Rating rating) {
    myRating = rating.getRating();
    clientID = rating.getId();
    ratingBar.setRating((float) myRating);
  }
  /**
   * ...
   *
   * @param course ...
   * @return ...
   */


  private static void getCourseDescription(final Course course) {
    String myDescription = "";
    String summaryJson =
        new Scanner(CourseActivity.class.getResourceAsStream("/2020_fall_summary.json"), "UTF-8")
            .useDelimiter("\\A")
            .next();
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
          course.setDescription(c.getDescription());
          break;
        }
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
  }
}


