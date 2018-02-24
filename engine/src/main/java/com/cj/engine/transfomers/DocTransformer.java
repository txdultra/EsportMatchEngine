package com.cj.engine.transfomers;

public interface DocTransformer<T,R> {
    R transform(T data);
}
