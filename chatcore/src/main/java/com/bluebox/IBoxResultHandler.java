package com.bluebox;

/**
 * Created by xuchen on 16/7/15.
 */
public interface IBoxResultHandler<T> {


    T getResult(byte[] data);


}
