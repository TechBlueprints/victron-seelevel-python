package no.nordicsemi.android.dfu.internal;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import no.nordicsemi.android.dfu.internal.exception.HexFileValidationException;

/* loaded from: classes.dex */
public class HexInputStream extends FilterInputStream {
    private final int LINE_LENGTH;
    private final int MBRSize;
    private int available;
    private int bytesRead;
    private int lastAddress;
    private final byte[] localBuf;
    private int localPos;
    private int pos;
    private int size;

    private int asciiToInt(int i) {
        if (i >= 65) {
            return i - 55;
        }
        if (i >= 48) {
            return i - 48;
        }
        return -1;
    }

    public HexInputStream(InputStream inputStream, int i) throws IOException {
        super(new BufferedInputStream(inputStream));
        this.LINE_LENGTH = 128;
        byte[] bArr = new byte[128];
        this.localBuf = bArr;
        this.localPos = 128;
        this.size = bArr.length;
        this.lastAddress = 0;
        this.MBRSize = i;
        this.available = calculateBinSize(i);
    }

    public HexInputStream(byte[] bArr, int i) throws IOException {
        super(new ByteArrayInputStream(bArr));
        this.LINE_LENGTH = 128;
        byte[] bArr2 = new byte[128];
        this.localBuf = bArr2;
        this.localPos = 128;
        this.size = bArr2.length;
        this.lastAddress = 0;
        this.MBRSize = i;
        this.available = calculateBinSize(i);
    }

    private int calculateBinSize(int i) throws IOException {
        int address;
        InputStream inputStream = this.in;
        inputStream.mark(inputStream.available());
        try {
            int i2 = inputStream.read();
            int i3 = 0;
            int i4 = 0;
            while (true) {
                checkComma(i2);
                int i5 = readByte(inputStream);
                int address2 = readAddress(inputStream);
                int i6 = readByte(inputStream);
                if (i6 != 0) {
                    if (i6 == 1) {
                        return i4;
                    }
                    if (i6 == 2) {
                        address = readAddress(inputStream) << 4;
                        if (i4 > 0 && (address >> 16) != (i3 >> 16) + 1) {
                            return i4;
                        }
                        skip(inputStream, 2L);
                    } else if (i6 == 4) {
                        int address3 = readAddress(inputStream);
                        if (i4 > 0 && address3 != (i3 >> 16) + 1) {
                            return i4;
                        }
                        address = address3 << 16;
                        skip(inputStream, 2L);
                    }
                    i3 = address;
                    while (true) {
                        i2 = inputStream.read();
                        if (i2 != 10 || i2 == 13) {
                        }
                    }
                } else if (address2 + i3 >= i) {
                    i4 += i5;
                }
                skip(inputStream, (i5 * 2) + 2);
                while (true) {
                    i2 = inputStream.read();
                    if (i2 != 10) {
                    }
                }
            }
        } finally {
            inputStream.reset();
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() {
        return this.available - this.bytesRead;
    }

    public int readPacket(byte[] bArr) throws IOException {
        int i = 0;
        while (i < bArr.length) {
            int i2 = this.localPos;
            if (i2 < this.size) {
                byte[] bArr2 = this.localBuf;
                this.localPos = i2 + 1;
                bArr[i] = bArr2[i2];
                i++;
            } else {
                int i3 = this.bytesRead;
                int line = readLine();
                this.size = line;
                this.bytesRead = i3 + line;
                if (line == 0) {
                    break;
                }
            }
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() {
        throw new UnsupportedOperationException("Please, use readPacket() method instead");
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return readPacket(bArr);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) {
        throw new UnsupportedOperationException("Please, use readPacket() method instead");
    }

    public int sizeInBytes() {
        return this.available;
    }

    public int sizeInPackets(int i) {
        int iSizeInBytes = sizeInBytes();
        return (iSizeInBytes / i) + (iSizeInBytes % i > 0 ? 1 : 0);
    }

    private int readLine() throws IOException {
        if (this.pos == -1) {
            return 0;
        }
        InputStream inputStream = this.in;
        while (true) {
            int i = inputStream.read();
            this.pos++;
            if (i != 10 && i != 13) {
                checkComma(i);
                int i2 = readByte(inputStream);
                this.pos += 2;
                int address = readAddress(inputStream);
                this.pos += 4;
                int i3 = readByte(inputStream);
                int i4 = this.pos + 2;
                this.pos = i4;
                if (i3 != 0) {
                    if (i3 == 1) {
                        this.pos = -1;
                        return 0;
                    }
                    if (i3 == 2) {
                        int address2 = readAddress(inputStream) << 4;
                        this.pos += 4;
                        if (this.bytesRead > 0 && (address2 >> 16) != (this.lastAddress >> 16) + 1) {
                            return 0;
                        }
                        this.lastAddress = address2;
                        this.pos = (int) (this.pos + skip(inputStream, 2L));
                    } else if (i3 == 4) {
                        int address3 = readAddress(inputStream);
                        this.pos += 4;
                        if (this.bytesRead > 0 && address3 != (this.lastAddress >> 16) + 1) {
                            return 0;
                        }
                        this.lastAddress = address3 << 16;
                        this.pos = (int) (this.pos + skip(inputStream, 2L));
                    } else {
                        this.pos = (int) (i4 + skip(inputStream, (i2 * 2) + 2));
                    }
                } else if (this.lastAddress + address < this.MBRSize) {
                    this.pos = (int) (i4 + skip(inputStream, (i2 * 2) + 2));
                    i3 = -1;
                }
                if (i3 == 0) {
                    for (int i5 = 0; i5 < this.localBuf.length && i5 < i2; i5++) {
                        int i6 = readByte(inputStream);
                        this.pos += 2;
                        this.localBuf[i5] = (byte) i6;
                    }
                    this.pos = (int) (this.pos + skip(inputStream, 2L));
                    this.localPos = 0;
                    return i2;
                }
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        super.reset();
        this.pos = 0;
        this.bytesRead = 0;
        this.localPos = 128;
    }

    private void checkComma(int i) throws HexFileValidationException {
        if (i != 58) {
            throw new HexFileValidationException("Not a HEX file");
        }
    }

    private long skip(InputStream inputStream, long j) throws IOException {
        long jSkip = inputStream.skip(j);
        return jSkip < j ? jSkip + inputStream.skip(j - jSkip) : jSkip;
    }

    private int readByte(InputStream inputStream) throws IOException {
        return asciiToInt(inputStream.read()) | (asciiToInt(inputStream.read()) << 4);
    }

    private int readAddress(InputStream inputStream) throws IOException {
        return readByte(inputStream) | (readByte(inputStream) << 8);
    }
}
