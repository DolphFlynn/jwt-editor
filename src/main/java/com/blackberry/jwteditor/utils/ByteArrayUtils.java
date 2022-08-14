package com.blackberry.jwteditor.utils;

import java.util.Arrays;

public class ByteArrayUtils {

    /**
     * Trim a byte[] to an expected length
     * @param bytes input byte[]
     * @param expectedLength expected length
     * @return trimmed byte[]
     */
    public static byte[] trimByteArray(byte[] bytes, int expectedLength){
        return Arrays.copyOfRange(bytes, 0, expectedLength);
    }

    /**
     * Remove all instances of the specified byte from the end of a byte array
     *
     * @param byteArray array of bytes to trim
     * @param trailingByte byte to remove
     * @return trimmed byte array
     */
    public static byte[] trimTrailingBytes(byte[] byteArray, byte trailingByte){
        // Find the earliest position from the end of the continuous sequence of trailingByte
        int i;
        for(i= byteArray.length - 1; i > 0; i--){
            if(byteArray[i] != trailingByte){
                break;
            }
        }

        // Return the subarray to the index
        return Arrays.copyOfRange(byteArray, 0, i);
    }
}
