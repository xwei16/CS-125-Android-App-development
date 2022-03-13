package edu.illinois.cs.cs125.fall2020.mp.databinding;
import edu.illinois.cs.cs125.fall2020.mp.R;
import edu.illinois.cs.cs125.fall2020.mp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemCourseBindingImpl extends ItemCourseBinding implements edu.illinois.cs.cs125.fall2020.mp.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemCourseBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 2, sIncludes, sViewsWithIds));
    }
    private ItemCourseBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        mCallback1 = new edu.illinois.cs.cs125.fall2020.mp.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.model == variableId) {
            setModel((edu.illinois.cs.cs125.fall2020.mp.models.Summary) variable);
        }
        else if (BR.listener == variableId) {
            setListener((edu.illinois.cs.cs125.fall2020.mp.adapters.CourseListAdapter.Listener) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setModel(@Nullable edu.illinois.cs.cs125.fall2020.mp.models.Summary Model) {
        this.mModel = Model;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.model);
        super.requestRebind();
    }
    public void setListener(@Nullable edu.illinois.cs.cs125.fall2020.mp.adapters.CourseListAdapter.Listener Listener) {
        this.mListener = Listener;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        edu.illinois.cs.cs125.fall2020.mp.models.Summary model = mModel;
        java.lang.String modelDepartmentChar = null;
        java.lang.String modelDepartmentCharModelNumberCharChar = null;
        java.lang.String modelTitle = null;
        java.lang.String modelDepartmentCharModelNumberChar = null;
        java.lang.String modelDepartmentCharModelNumber = null;
        edu.illinois.cs.cs125.fall2020.mp.adapters.CourseListAdapter.Listener listener = mListener;
        java.lang.String modelNumber = null;
        java.lang.String modelDepartmentCharModelNumberCharCharModelTitle = null;
        java.lang.String modelDepartment = null;

        if ((dirtyFlags & 0x5L) != 0) {



                if (model != null) {
                    // read model.title
                    modelTitle = model.getTitle();
                    // read model.number
                    modelNumber = model.getNumber();
                    // read model.department
                    modelDepartment = model.getDepartment();
                }


                // read (model.department) + (' ')
                modelDepartmentChar = (modelDepartment) + (' ');


                // read ((model.department) + (' ')) + (model.number)
                modelDepartmentCharModelNumber = (modelDepartmentChar) + (modelNumber);


                // read (((model.department) + (' ')) + (model.number)) + (':')
                modelDepartmentCharModelNumberChar = (modelDepartmentCharModelNumber) + (':');


                // read ((((model.department) + (' ')) + (model.number)) + (':')) + (' ')
                modelDepartmentCharModelNumberCharChar = (modelDepartmentCharModelNumberChar) + (' ');


                // read (((((model.department) + (' ')) + (model.number)) + (':')) + (' ')) + (model.title)
                modelDepartmentCharModelNumberCharCharModelTitle = (modelDepartmentCharModelNumberCharChar) + (modelTitle);
        }
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.mboundView0.setOnClickListener(mCallback1);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, modelDepartmentCharModelNumberCharCharModelTitle);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // model
        edu.illinois.cs.cs125.fall2020.mp.models.Summary model = mModel;
        // listener != null
        boolean listenerJavaLangObjectNull = false;
        // listener
        edu.illinois.cs.cs125.fall2020.mp.adapters.CourseListAdapter.Listener listener = mListener;



        listenerJavaLangObjectNull = (listener) != (null);
        if (listenerJavaLangObjectNull) {



            listener.onCourseClicked(model);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): model
        flag 1 (0x2L): listener
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}