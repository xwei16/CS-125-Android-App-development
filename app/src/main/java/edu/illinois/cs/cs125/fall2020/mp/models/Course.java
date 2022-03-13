package edu.illinois.cs.cs125.fall2020.mp.models;

/**
 * . this is a Course class extending Summary in order to add something special from the fields of
 * Summary
 */
public class Course extends Summary {
  private String description;
  /**
   * Get the description for this Course.
   *
   * @return the description for this Course
   */
  public final String getDescription() {
    return description;
  }

  /**
   * ...
   *
   * @param setDesc ...
   */
  public final void setDescription(final String setDesc) {
    description = setDesc;
  }
}
