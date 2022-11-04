package com.ojih.rex.eventplanner.utilities;

import java.util.List;

public interface Mapper<T, K> {

    public T toDto(K k);
    public List<T> toDtos(List<K> ks);
}
