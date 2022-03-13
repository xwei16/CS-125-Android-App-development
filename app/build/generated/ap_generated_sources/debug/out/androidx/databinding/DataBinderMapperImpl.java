package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new edu.illinois.cs.cs125.fall2020.mp.DataBinderMapperImpl());
  }
}
