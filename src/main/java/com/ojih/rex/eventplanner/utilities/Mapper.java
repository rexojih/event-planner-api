package com.ojih.rex.eventplanner.utilities;

import java.util.List;

public interface Mapper<T, K> {

    T toDto(K k);
    List<T> toDtos(List<K> ks);
}
