package no.nordicsemi.android.ble.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public final class ScanFilter implements Parcelable {
    private final String deviceAddress;
    private final String deviceName;
    private final byte[] manufacturerData;
    private final byte[] manufacturerDataMask;
    private final int manufacturerId;
    private final byte[] serviceData;
    private final byte[] serviceDataMask;
    private final ParcelUuid serviceDataUuid;
    private final ParcelUuid serviceUuid;
    private final ParcelUuid serviceUuidMask;
    private static final ScanFilter EMPTY = new Builder().build();
    public static final Parcelable.Creator<ScanFilter> CREATOR = new Parcelable.Creator<ScanFilter>() { // from class: no.nordicsemi.android.ble.scanner.ScanFilter.1
        @Override // android.os.Parcelable.Creator
        public ScanFilter[] newArray(int i) {
            return new ScanFilter[i];
        }

        @Override // android.os.Parcelable.Creator
        public ScanFilter createFromParcel(Parcel parcel) {
            Builder builder = new Builder();
            if (parcel.readInt() == 1) {
                builder.setDeviceName(parcel.readString());
            }
            if (parcel.readInt() == 1) {
                builder.setDeviceAddress(parcel.readString());
            }
            if (parcel.readInt() == 1) {
                ParcelUuid parcelUuid = (ParcelUuid) parcel.readParcelable(ParcelUuid.class.getClassLoader());
                builder.setServiceUuid(parcelUuid);
                if (parcel.readInt() == 1) {
                    builder.setServiceUuid(parcelUuid, (ParcelUuid) parcel.readParcelable(ParcelUuid.class.getClassLoader()));
                }
            }
            if (parcel.readInt() == 1) {
                ParcelUuid parcelUuid2 = (ParcelUuid) parcel.readParcelable(ParcelUuid.class.getClassLoader());
                if (parcel.readInt() == 1) {
                    byte[] bArr = new byte[parcel.readInt()];
                    parcel.readByteArray(bArr);
                    if (parcel.readInt() == 0) {
                        builder.setServiceData(parcelUuid2, bArr);
                    } else {
                        byte[] bArr2 = new byte[parcel.readInt()];
                        parcel.readByteArray(bArr2);
                        builder.setServiceData(parcelUuid2, bArr, bArr2);
                    }
                }
            }
            int i = parcel.readInt();
            if (parcel.readInt() == 1) {
                byte[] bArr3 = new byte[parcel.readInt()];
                parcel.readByteArray(bArr3);
                if (parcel.readInt() == 0) {
                    builder.setManufacturerData(i, bArr3);
                } else {
                    byte[] bArr4 = new byte[parcel.readInt()];
                    parcel.readByteArray(bArr4);
                    builder.setManufacturerData(i, bArr3, bArr4);
                }
            }
            return builder.build();
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private ScanFilter(String str, String str2, ParcelUuid parcelUuid, ParcelUuid parcelUuid2, ParcelUuid parcelUuid3, byte[] bArr, byte[] bArr2, int i, byte[] bArr3, byte[] bArr4) {
        this.deviceName = str;
        this.serviceUuid = parcelUuid;
        this.serviceUuidMask = parcelUuid2;
        this.deviceAddress = str2;
        this.serviceDataUuid = parcelUuid3;
        this.serviceData = bArr;
        this.serviceDataMask = bArr2;
        this.manufacturerId = i;
        this.manufacturerData = bArr3;
        this.manufacturerDataMask = bArr4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.deviceName == null ? 0 : 1);
        String str = this.deviceName;
        if (str != null) {
            parcel.writeString(str);
        }
        parcel.writeInt(this.deviceAddress == null ? 0 : 1);
        String str2 = this.deviceAddress;
        if (str2 != null) {
            parcel.writeString(str2);
        }
        parcel.writeInt(this.serviceUuid == null ? 0 : 1);
        ParcelUuid parcelUuid = this.serviceUuid;
        if (parcelUuid != null) {
            parcel.writeParcelable(parcelUuid, i);
            parcel.writeInt(this.serviceUuidMask == null ? 0 : 1);
            ParcelUuid parcelUuid2 = this.serviceUuidMask;
            if (parcelUuid2 != null) {
                parcel.writeParcelable(parcelUuid2, i);
            }
        }
        parcel.writeInt(this.serviceDataUuid == null ? 0 : 1);
        ParcelUuid parcelUuid3 = this.serviceDataUuid;
        if (parcelUuid3 != null) {
            parcel.writeParcelable(parcelUuid3, i);
            parcel.writeInt(this.serviceData == null ? 0 : 1);
            byte[] bArr = this.serviceData;
            if (bArr != null) {
                parcel.writeInt(bArr.length);
                parcel.writeByteArray(this.serviceData);
                parcel.writeInt(this.serviceDataMask == null ? 0 : 1);
                byte[] bArr2 = this.serviceDataMask;
                if (bArr2 != null) {
                    parcel.writeInt(bArr2.length);
                    parcel.writeByteArray(this.serviceDataMask);
                }
            }
        }
        parcel.writeInt(this.manufacturerId);
        parcel.writeInt(this.manufacturerData == null ? 0 : 1);
        byte[] bArr3 = this.manufacturerData;
        if (bArr3 != null) {
            parcel.writeInt(bArr3.length);
            parcel.writeByteArray(this.manufacturerData);
            parcel.writeInt(this.manufacturerDataMask != null ? 1 : 0);
            byte[] bArr4 = this.manufacturerDataMask;
            if (bArr4 != null) {
                parcel.writeInt(bArr4.length);
                parcel.writeByteArray(this.manufacturerDataMask);
            }
        }
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public ParcelUuid getServiceUuid() {
        return this.serviceUuid;
    }

    public ParcelUuid getServiceUuidMask() {
        return this.serviceUuidMask;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }

    public byte[] getServiceData() {
        return this.serviceData;
    }

    public byte[] getServiceDataMask() {
        return this.serviceDataMask;
    }

    public ParcelUuid getServiceDataUuid() {
        return this.serviceDataUuid;
    }

    public int getManufacturerId() {
        return this.manufacturerId;
    }

    public byte[] getManufacturerData() {
        return this.manufacturerData;
    }

    public byte[] getManufacturerDataMask() {
        return this.manufacturerDataMask;
    }

    public boolean matches(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        BluetoothDevice device = scanResult.getDevice();
        String str = this.deviceAddress;
        if (str != null && !str.equals(device.getAddress())) {
            return false;
        }
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (scanRecord == null && (this.deviceName != null || this.serviceUuid != null || this.manufacturerData != null || this.serviceData != null)) {
            return false;
        }
        String str2 = this.deviceName;
        if (str2 != null && !str2.equals(scanRecord.getDeviceName())) {
            return false;
        }
        ParcelUuid parcelUuid = this.serviceUuid;
        if (parcelUuid != null && !matchesServiceUuids(parcelUuid, this.serviceUuidMask, scanRecord.getServiceUuids())) {
            return false;
        }
        ParcelUuid parcelUuid2 = this.serviceDataUuid;
        if (parcelUuid2 != null && scanRecord != null && !matchesPartialData(this.serviceData, this.serviceDataMask, scanRecord.getServiceData(parcelUuid2))) {
            return false;
        }
        int i = this.manufacturerId;
        return i < 0 || scanRecord == null || matchesPartialData(this.manufacturerData, this.manufacturerDataMask, scanRecord.getManufacturerSpecificData(i));
    }

    private static boolean matchesServiceUuids(ParcelUuid parcelUuid, ParcelUuid parcelUuid2, List<ParcelUuid> list) {
        if (parcelUuid == null) {
            return true;
        }
        if (list == null) {
            return false;
        }
        Iterator<ParcelUuid> it = list.iterator();
        while (it.hasNext()) {
            if (matchesServiceUuid(parcelUuid.getUuid(), parcelUuid2 == null ? null : parcelUuid2.getUuid(), it.next().getUuid())) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesServiceUuid(UUID uuid, UUID uuid2, UUID uuid3) {
        if (uuid2 == null) {
            return uuid.equals(uuid3);
        }
        if ((uuid.getLeastSignificantBits() & uuid2.getLeastSignificantBits()) != (uuid3.getLeastSignificantBits() & uuid2.getLeastSignificantBits())) {
            return false;
        }
        return (uuid.getMostSignificantBits() & uuid2.getMostSignificantBits()) == (uuid2.getMostSignificantBits() & uuid3.getMostSignificantBits());
    }

    private boolean matchesPartialData(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr == null) {
            return bArr3 != null;
        }
        if (bArr3 == null || bArr3.length < bArr.length) {
            return false;
        }
        if (bArr2 == null) {
            for (int i = 0; i < bArr.length; i++) {
                if (bArr3[i] != bArr[i]) {
                    return false;
                }
            }
            return true;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if ((bArr2[i2] & bArr3[i2]) != (bArr2[i2] & bArr[i2])) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "BluetoothLeScanFilter [deviceName=" + this.deviceName + ", deviceAddress=" + this.deviceAddress + ", mUuid=" + this.serviceUuid + ", uuidMask=" + this.serviceUuidMask + ", serviceDataUuid=" + Objects.toString(this.serviceDataUuid) + ", serviceData=" + Arrays.toString(this.serviceData) + ", serviceDataMask=" + Arrays.toString(this.serviceDataMask) + ", manufacturerId=" + this.manufacturerId + ", manufacturerData=" + Arrays.toString(this.manufacturerData) + ", manufacturerDataMask=" + Arrays.toString(this.manufacturerDataMask) + "]";
    }

    public int hashCode() {
        return Objects.hash(this.deviceName, this.deviceAddress, Integer.valueOf(this.manufacturerId), Integer.valueOf(Arrays.hashCode(this.manufacturerData)), Integer.valueOf(Arrays.hashCode(this.manufacturerDataMask)), this.serviceDataUuid, Integer.valueOf(Arrays.hashCode(this.serviceData)), Integer.valueOf(Arrays.hashCode(this.serviceDataMask)), this.serviceUuid, this.serviceUuidMask);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScanFilter scanFilter = (ScanFilter) obj;
        return Objects.equals(this.deviceName, scanFilter.deviceName) && Objects.equals(this.deviceAddress, scanFilter.deviceAddress) && this.manufacturerId == scanFilter.manufacturerId && Objects.deepEquals(this.manufacturerData, scanFilter.manufacturerData) && Objects.deepEquals(this.manufacturerDataMask, scanFilter.manufacturerDataMask) && Objects.equals(this.serviceDataUuid, scanFilter.serviceDataUuid) && Objects.deepEquals(this.serviceData, scanFilter.serviceData) && Objects.deepEquals(this.serviceDataMask, scanFilter.serviceDataMask) && Objects.equals(this.serviceUuid, scanFilter.serviceUuid) && Objects.equals(this.serviceUuidMask, scanFilter.serviceUuidMask);
    }

    boolean isAllFieldsEmpty() {
        return EMPTY.equals(this);
    }

    public static final class Builder {
        private String deviceAddress;
        private String deviceName;
        private byte[] manufacturerData;
        private byte[] manufacturerDataMask;
        private int manufacturerId = -1;
        private byte[] serviceData;
        private byte[] serviceDataMask;
        private ParcelUuid serviceDataUuid;
        private ParcelUuid serviceUuid;
        private ParcelUuid uuidMask;

        public Builder setDeviceName(String str) {
            this.deviceName = str;
            return this;
        }

        public Builder setDeviceAddress(String str) {
            if (str != null && !BluetoothAdapter.checkBluetoothAddress(str)) {
                throw new IllegalArgumentException("invalid device address " + str);
            }
            this.deviceAddress = str;
            return this;
        }

        public Builder setServiceUuid(ParcelUuid parcelUuid) {
            this.serviceUuid = parcelUuid;
            this.uuidMask = null;
            return this;
        }

        public Builder setServiceUuid(ParcelUuid parcelUuid, ParcelUuid parcelUuid2) {
            if (parcelUuid2 != null && parcelUuid == null) {
                throw new IllegalArgumentException("uuid is null while uuidMask is not null!");
            }
            this.serviceUuid = parcelUuid;
            this.uuidMask = parcelUuid2;
            return this;
        }

        public Builder setServiceData(ParcelUuid parcelUuid, byte[] bArr) {
            if (parcelUuid == null) {
                throw new IllegalArgumentException("serviceDataUuid is null!");
            }
            this.serviceDataUuid = parcelUuid;
            this.serviceData = bArr;
            this.serviceDataMask = null;
            return this;
        }

        public Builder setServiceData(ParcelUuid parcelUuid, byte[] bArr, byte[] bArr2) {
            if (parcelUuid == null) {
                throw new IllegalArgumentException("serviceDataUuid is null");
            }
            if (bArr2 != null) {
                if (bArr == null) {
                    throw new IllegalArgumentException("serviceData is null while serviceDataMask is not null");
                }
                if (bArr.length != bArr2.length) {
                    throw new IllegalArgumentException("size mismatch for service data and service data mask");
                }
            }
            this.serviceDataUuid = parcelUuid;
            this.serviceData = bArr;
            this.serviceDataMask = bArr2;
            return this;
        }

        public Builder setManufacturerData(int i, byte[] bArr) {
            if (bArr != null && i < 0) {
                throw new IllegalArgumentException("invalid manufacture id");
            }
            this.manufacturerId = i;
            this.manufacturerData = bArr;
            this.manufacturerDataMask = null;
            return this;
        }

        public Builder setManufacturerData(int i, byte[] bArr, byte[] bArr2) {
            if (bArr != null && i < 0) {
                throw new IllegalArgumentException("invalid manufacture id");
            }
            if (bArr2 != null) {
                if (bArr == null) {
                    throw new IllegalArgumentException("manufacturerData is null while manufacturerDataMask is not null");
                }
                if (bArr.length != bArr2.length) {
                    throw new IllegalArgumentException("size mismatch for manufacturerData and manufacturerDataMask");
                }
            }
            this.manufacturerId = i;
            this.manufacturerData = bArr;
            this.manufacturerDataMask = bArr2;
            return this;
        }

        public ScanFilter build() {
            return new ScanFilter(this.deviceName, this.deviceAddress, this.serviceUuid, this.uuidMask, this.serviceDataUuid, this.serviceData, this.serviceDataMask, this.manufacturerId, this.manufacturerData, this.manufacturerDataMask);
        }
    }
}
