// Generated by data binding compiler. Do not edit!
package edu.illinois.cs.cs125.fall2020.mp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.adapters.CourseListAdapter;
import edu.illinois.cs.cs125.fall2020.mp.models.Summary;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemCourseBinding extends ViewDataBinding {
  @Bindable
  protected CourseListAdapter.Listener mListener;

  @Bindable
  protected Summary mModel;

  protected ItemCourseBinding(Object _bindingComponent, View _root, int _localFieldCount) {
    super(_bindingComponent, _root, _localFieldCount);
  }

  public abstract void setListener(@Nullable CourseListAdapter.Listener listener);

  @Nullable
  public CourseListAdapter.Listener getListener() {
    return mListener;
  }

  public abstract void setModel(@Nullable Summary model);

  @Nullable
  public Summary getModel() {
    return mModel;
  }

  @NonNull
  public static ItemCourseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_course, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemCourseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemCourseBinding>inflateInternal(inflater, R.layout.item_course, root, attachToRoot, component);
  }

  @NonNull
  public static ItemCourseBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_course, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemCourseBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemCourseBinding>inflateInternal(inflater, R.layout.item_course, null, false, component);
  }

  public static ItemCourseBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ItemCourseBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemCourseBinding)bind(component, view, R.layout.item_course);
  }
}