package edu.illinois.cs.cs125.fall2020.mp.models;

/** Rating class for storing client ratings of courses. */
public class Rating {
  /** Rating indication that the course has not been rated yet. */
  public static final double NOT_RATED = -1.0;
  /** .... */
  public static final String DEFAULT_CLIENTID = "0000";
  private String id;
  private double rating;


  /**
   * . constructor for the Rating class
   *
   * @param setId set the field Id for the instance
   * @param setRating set the field rating for the instance
   */
  public Rating(final String setId, final double setRating) {
    rating = setRating;
    id = setId;
  }

  /**
   * . return id of given instance
   *
   * @return ID
   */
  public String getId() {
    return id;
  }

  /**
   * ...
   *
   * @param iD ...
   */
  public void setId(final String iD) {
    id = iD;
  }

  /**
   * . return rating of given instance
   *
   * @return rating
   */
  public double getRating() {
    return rating;
  }

  /**
   * ...
   *
   * @param setR ....
   */
  public void setRating(final double setR) {
    rating = setR;
  }
}
