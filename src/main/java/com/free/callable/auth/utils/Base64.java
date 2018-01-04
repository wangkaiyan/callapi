package com.free.callable.auth.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by  on 2016/9/19.
 */
public class Base64 {
    static final int CHUNK_SIZE = 76;
    static final byte[] CHUNK_SEPARATOR = new byte[]{(byte)13, (byte)10};
    private static final byte[] intToBase64 = new byte[]{(byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)43, (byte)47};
    private static final byte PAD = 61;
    private static final byte[] base64ToInt = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)62, (byte)-1, (byte)-1, (byte)-1, (byte)63, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51};
    private static final int MASK_6BITS = 63;
    private static final int MASK_8BITS = 255;
    private final int lineLength;
    private final byte[] lineSeparator;
    private final int decodeSize;
    private final int encodeSize;
    private byte[] buf;
    private int pos;
    private int readPos;
    private int currentLinePos;
    private int modulus;
    private boolean eof;
    private int x;

    public Base64() {
        this(76, CHUNK_SEPARATOR);
    }

    public Base64(int lineLength) {
        this(lineLength, CHUNK_SEPARATOR);
    }

    public Base64(int lineLength, byte[] lineSeparator) {
        this.lineLength = lineLength;
        this.lineSeparator = new byte[lineSeparator.length];
        System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
        if(lineLength > 0) {
            this.encodeSize = 4 + lineSeparator.length;
        } else {
            this.encodeSize = 4;
        }

        this.decodeSize = this.encodeSize - 1;
        if(containsBase64Byte(lineSeparator)) {
            String sep;
            try {
                sep = new String(lineSeparator, "UTF-8");
            } catch (UnsupportedEncodingException var5) {
                sep = new String(lineSeparator);
            }

            throw new IllegalArgumentException("lineSeperator must not contain base64 characters: [" + sep + "]");
        }
    }

    boolean hasData() {
        return this.buf != null;
    }

    int avail() {
        return this.buf != null?this.pos - this.readPos:0;
    }

    private void resizeBuf() {
        if(this.buf == null) {
            this.buf = new byte[8192];
            this.pos = 0;
            this.readPos = 0;
        } else {
            byte[] b = new byte[this.buf.length * 2];
            System.arraycopy(this.buf, 0, b, 0, this.buf.length);
            this.buf = b;
        }

    }

    int readResults(byte[] b, int bPos, int bAvail) {
        if(this.buf != null) {
            int len = Math.min(this.avail(), bAvail);
            if(this.buf != b) {
                System.arraycopy(this.buf, this.readPos, b, bPos, len);
                this.readPos += len;
                if(this.readPos >= this.pos) {
                    this.buf = null;
                }
            } else {
                this.buf = null;
            }

            return len;
        } else {
            return this.eof?-1:0;
        }
    }

    void setInitialBuffer(byte[] out, int outPos, int outAvail) {
        if(out != null && out.length == outAvail) {
            this.buf = out;
            this.pos = outPos;
            this.readPos = outPos;
        }

    }

    void encode(byte[] in, int inPos, int inAvail) {
        if(!this.eof) {
            if(inAvail < 0) {
                this.eof = true;
                if(this.buf == null || this.buf.length - this.pos < this.encodeSize) {
                    this.resizeBuf();
                }

                switch(this.modulus) {
                    case 1:
                        this.buf[this.pos++] = intToBase64[this.x >> 2 & 63];
                        this.buf[this.pos++] = intToBase64[this.x << 4 & 63];
                        this.buf[this.pos++] = 61;
                        this.buf[this.pos++] = 61;
                        break;
                    case 2:
                        this.buf[this.pos++] = intToBase64[this.x >> 10 & 63];
                        this.buf[this.pos++] = intToBase64[this.x >> 4 & 63];
                        this.buf[this.pos++] = intToBase64[this.x << 2 & 63];
                        this.buf[this.pos++] = 61;
                }

                if(this.lineLength > 0) {
                    System.arraycopy(this.lineSeparator, 0, this.buf, this.pos, this.lineSeparator.length);
                    this.pos += this.lineSeparator.length;
                }
            } else {
                for(int i = 0; i < inAvail; ++i) {
                    if(this.buf == null || this.buf.length - this.pos < this.encodeSize) {
                        this.resizeBuf();
                    }

                    this.modulus = ++this.modulus % 3;
                    int b = in[inPos++];
                    if(b < 0) {
                        b += 256;
                    }

                    this.x = (this.x << 8) + b;
                    if(0 == this.modulus) {
                        this.buf[this.pos++] = intToBase64[this.x >> 18 & 63];
                        this.buf[this.pos++] = intToBase64[this.x >> 12 & 63];
                        this.buf[this.pos++] = intToBase64[this.x >> 6 & 63];
                        this.buf[this.pos++] = intToBase64[this.x & 63];
                        this.currentLinePos += 4;
                        if(this.lineLength > 0 && this.lineLength <= this.currentLinePos) {
                            System.arraycopy(this.lineSeparator, 0, this.buf, this.pos, this.lineSeparator.length);
                            this.pos += this.lineSeparator.length;
                            this.currentLinePos = 0;
                        }
                    }
                }
            }

        }
    }

    void decode(byte[] in, int inPos, int inAvail) {
        if(!this.eof) {
            if(inAvail < 0) {
                this.eof = true;
            }

            for(int i = 0; i < inAvail; ++i) {
                if(this.buf == null || this.buf.length - this.pos < this.decodeSize) {
                    this.resizeBuf();
                }

                byte b = in[inPos++];
                if(b == 61) {
                    this.x <<= 6;
                    switch(this.modulus) {
                        case 2:
                            this.x <<= 6;
                            this.buf[this.pos++] = (byte)(this.x >> 16 & 255);
                            break;
                        case 3:
                            this.buf[this.pos++] = (byte)(this.x >> 16 & 255);
                            this.buf[this.pos++] = (byte)(this.x >> 8 & 255);
                    }

                    this.eof = true;
                    return;
                }

                if(b >= 0 && b < base64ToInt.length) {
                    byte result = base64ToInt[b];
                    if(result >= 0) {
                        this.modulus = ++this.modulus % 4;
                        this.x = (this.x << 6) + result;
                        if(this.modulus == 0) {
                            this.buf[this.pos++] = (byte)(this.x >> 16 & 255);
                            this.buf[this.pos++] = (byte)(this.x >> 8 & 255);
                            this.buf[this.pos++] = (byte)(this.x & 255);
                        }
                    }
                }
            }

        }
    }

    public static boolean isBase64(byte octet) {
        return octet == 61 || octet >= 0 && octet < base64ToInt.length && base64ToInt[octet] != -1;
    }

    public static boolean isArrayByteBase64(byte[] arrayOctet) {
        for(int i = 0; i < arrayOctet.length; ++i) {
            if(!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
                return false;
            }
        }

        return true;
    }

    private static boolean containsBase64Byte(byte[] arrayOctet) {
        for(int i = 0; i < arrayOctet.length; ++i) {
            if(isBase64(arrayOctet[i])) {
                return true;
            }
        }

        return false;
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public byte[] decode(byte[] pArray) {
        return decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        if(binaryData != null && binaryData.length != 0) {
            Base64 b64 = isChunked?new Base64():new Base64(0);
            long len = (long)(binaryData.length * 4 / 3);
            long mod = len % 4L;
            if(mod != 0L) {
                len += 4L - mod;
            }

            if(isChunked) {
                len += (1L + len / 76L) * (long)CHUNK_SEPARATOR.length;
            }

            if(len > 2147483647L) {
                throw new IllegalArgumentException("Input array too big, output array would be bigger than Integer.MAX_VALUE=2147483647");
            } else {
                byte[] buf = new byte[(int)len];
                b64.setInitialBuffer(buf, 0, buf.length);
                b64.encode(binaryData, 0, binaryData.length);
                b64.encode(binaryData, 0, -1);
                if(b64.buf != buf) {
                    b64.readResults(buf, 0, buf.length);
                }

                return buf;
            }
        } else {
            return binaryData;
        }
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        if(base64Data != null && base64Data.length != 0) {
            Base64 b64 = new Base64();
            long len = (long)(base64Data.length * 3 / 4);
            byte[] buf = new byte[(int)len];
            b64.setInitialBuffer(buf, 0, buf.length);
            b64.decode(base64Data, 0, base64Data.length);
            b64.decode(base64Data, 0, -1);
            byte[] result = new byte[b64.pos];
            b64.readResults(result, 0, result.length);
            return result;
        } else {
            return base64Data;
        }
    }

    private static boolean isWhiteSpace(byte byteToCheck) {
        switch(byteToCheck) {
            case 9:
            case 10:
            case 13:
            case 32:
                return true;
            default:
                return false;
        }
    }

    static byte[] discardNonBase64(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;

        for(int packedData = 0; packedData < data.length; ++packedData) {
            if(isBase64(data[packedData])) {
                groomedData[bytesCopied++] = data[packedData];
            }
        }

        byte[] var4 = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, var4, 0, bytesCopied);
        return var4;
    }

    public byte[] encode(byte[] pArray) {
        return encodeBase64(pArray, false);
    }

    public static BigInteger decodeInteger(byte[] pArray) {
        return new BigInteger(1, decodeBase64(pArray));
    }

    public static byte[] encodeInteger(BigInteger bigInt) {
        if(bigInt == null) {
            throw new NullPointerException("encodeInteger called with null parameter");
        } else {
            return encodeBase64(toIntegerBytes(bigInt), false);
        }
    }

    static byte[] toIntegerBytes(BigInteger bigInt) {
        int bitlen = bigInt.bitLength();
        bitlen = bitlen + 7 >> 3 << 3;
        byte[] bigBytes = bigInt.toByteArray();
        if(bigInt.bitLength() % 8 != 0 && bigInt.bitLength() / 8 + 1 == bitlen / 8) {
            return bigBytes;
        } else {
            byte startSrc = 0;
            int len = bigBytes.length;
            if(bigInt.bitLength() % 8 == 0) {
                startSrc = 1;
                --len;
            }

            int startDst = bitlen / 8 - len;
            byte[] resizedBytes = new byte[bitlen / 8];
            System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
            return resizedBytes;
        }
    }
}

