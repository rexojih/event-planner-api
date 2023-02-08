package com.ojih.rex.eventplanner.util;

import java.util.List;

public interface Mapper<T, K> {

    T toDTO(K k);

    List<T> toDTOs(List<K> ks);
}
