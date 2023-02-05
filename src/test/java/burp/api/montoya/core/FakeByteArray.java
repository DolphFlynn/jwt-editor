package burp.api.montoya.core;

import java.util.Iterator;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FakeByteArray implements ByteArray {
    private final byte[] data;

    public FakeByteArray(String data) {
        this.data = data.getBytes(UTF_8);
    }

    @Override
    public byte getByte(int index) {
        return data[index];
    }

    @Override
    public void setByte(int index, byte value) {
        data[index] = value;
    }

    @Override
    public void setByte(int index, int value) {
        setByte(index, (byte) (0xFF & value));
    }

    @Override
    public void setBytes(int index, byte... data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBytes(int index, int... data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBytes(int index, ByteArray byteArray) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int length() {
        return data.length;
    }

    @Override
    public byte[] getBytes() {
        return data;
    }

    @Override
    public ByteArray subArray(int startIndexInclusive, int endIndexExclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray subArray(Range range) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray copy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray copyToTempFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(ByteArray searchTerm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(String searchTerm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(ByteArray searchTerm, boolean caseSensitive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(String searchTerm, boolean caseSensitive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(ByteArray searchTerm, boolean caseSensitive, int startIndexInclusive, int endIndexExclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(String searchTerm, boolean caseSensitive, int startIndexInclusive, int endIndexExclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int countMatches(ByteArray searchTerm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int countMatches(String searchTerm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int countMatches(ByteArray searchTerm, boolean caseSensitive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int countMatches(String searchTerm, boolean caseSensitive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int countMatches(ByteArray searchTerm, boolean caseSensitive, int startIndexInclusive, int endIndexExclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int countMatches(String searchTerm, boolean caseSensitive, int startIndexInclusive, int endIndexExclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray withAppended(byte... data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray withAppended(int... data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray withAppended(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray withAppended(ByteArray byteArray) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Byte> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return new String(data, UTF_8);
    }
}
