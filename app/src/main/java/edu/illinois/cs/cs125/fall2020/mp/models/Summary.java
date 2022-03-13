package edu.illinois.cs.cs125.fall2020.mp.models;

import androidx.annotation.NonNull;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Model holding the course summary information shown in the course list.
 *
 * <p>You will need to complete this model for MP0.
 */
public class Summary implements SortedListAdapter.ViewModel {
  private String year;
  private String semester;
  private String department;
  private String number;
  private String title;

  /**
   * Get the year for this Summary.
   *
   * @return the year for this Summary
   */
  public final String getYear() {
    return year;
  }

  /**
   * Get the semester for this Summary.
   *
   * @return the semester for this Summary
   */
  public final String getSemester() {
    return semester;
  }

  /**
   * Get the department for this Summary.
   *
   * @return the department for this Summary
   */
  public final String getDepartment() {
    return department;
  }

  /**
   * Get the number for this Summary.
   *
   * @return the number for this Summary
   */
  public final String getNumber() {
    return number;
  }

  /**
   * Get the title for this Summary.
   *
   * @return the title for this Summary
   */
  public final String getTitle() {
    return title;
  }

  /** Create an empty Summary. */
  @SuppressWarnings({"unused", "RedundantSuppression"})
  public Summary() {}

  /**
   * Create a Summary with the provided fields.
   *
   * @param setYear the year for this Summary
   * @param setSemester the semester for this Summary
   * @param setDepartment the department for this Summary
   * @param setNumber the number for this Summary
   * @param setTitle the title for this Summary
   */
  public Summary(
      final String setYear,
      final String setSemester,
      final String setDepartment,
      final String setNumber,
      final String setTitle) {
    year = setYear;
    semester = setSemester;
    department = setDepartment;
    number = setNumber;
    title = setTitle;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Summary)) {
      return false;
    }
    Summary course = (Summary) o;
    return Objects.equals(year, course.year)
        && Objects.equals(semester, course.semester)
        && Objects.equals(department, course.department)
        && Objects.equals(number, course.number);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(year, semester, department, number);
  }

  /** {@inheritDoc} */
  @Override
  public <T> boolean isSameModelAs(@NonNull final T model) {
    return equals(model);
  }

  /** {@inheritDoc} */
  @Override
  public <T> boolean isContentTheSameAs(@NonNull final T model) {
    return equals(model);
  }

  /**
   * compare the passed instances. compare the field with the order of department, number, and then
   * title department & title: follow the rule of string compariion number: follow the rule of int
   * comparison
   */
  public static final Comparator<Summary> COMPARATOR =
      (courseModel1, courseModel2) -> {
        if (courseModel1.department.compareTo(courseModel2.department) != 0) {
          return courseModel1.department.compareTo(courseModel2.department);
        }
        if (courseModel1.number.compareTo(courseModel2.number) != 0) {
          return courseModel1.number.compareTo(courseModel2.number);
        }
        if (courseModel1.title.compareTo(courseModel2.title) != 0) {
          return courseModel1.title.compareTo(courseModel2.title);
        }
        return 0;
      };

  /**
   * filter the instance with unrelated text. if any object of the courses have no certain text,
   * then filter it
   *
   * @param courses a list if instances of model
   * @param text the certain filter text
   * @return List<Summary> filtered list
   */
  public static List<Summary> filter(
      // for search sth i'm looking for
      @NonNull final List<Summary> courses, @NonNull final String text) {

    if (null == courses) {
      return courses;
    }
    if (0 == courses.size()) {
      return courses;
    }
    List<Summary> list = new ArrayList<>(courses);
    List<Summary> ret = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {
      if ((list.get(i).getNumber().contains(text))
          || list.get(i).getTitle().toLowerCase().contains(text.toLowerCase())
          || list.get(i).getDepartment().contains(text)) {
        ret.add(list.get(i));
      }
    }
    return ret;
  }
}
