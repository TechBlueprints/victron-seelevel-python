package no.nordicsemi.android.ble.livedata.state;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: ConnectionState.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\u000b\f\r\u000e\u000f\u0010B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u0011\u0010\b\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u0082\u0001\u0005\u0011\u0012\u0013\u0014\u0015¨\u0006\u0016"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "", "state", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState$State;", "(Lno/nordicsemi/android/ble/livedata/state/ConnectionState$State;)V", "isConnected", "", "()Z", "isReady", "getState", "()Lno/nordicsemi/android/ble/livedata/state/ConnectionState$State;", "Connecting", "Disconnected", "Disconnecting", "Initializing", "Ready", "State", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Connecting;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Initializing;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Ready;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Disconnecting;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Disconnected;", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class ConnectionState {
    private final State state;

    /* compiled from: ConnectionState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState$State;", "", "(Ljava/lang/String;I)V", "CONNECTING", "INITIALIZING", "READY", "DISCONNECTING", "DISCONNECTED", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public enum State {
        CONNECTING,
        INITIALIZING,
        READY,
        DISCONNECTING,
        DISCONNECTED
    }

    private ConnectionState(State state) {
        this.state = state;
    }

    public /* synthetic */ ConnectionState(State state, DefaultConstructorMarker defaultConstructorMarker) {
        this(state);
    }

    public final State getState() {
        return this.state;
    }

    /* compiled from: ConnectionState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Connecting;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class Connecting extends ConnectionState {
        public static final Connecting INSTANCE = new Connecting();

        private Connecting() {
            super(State.CONNECTING, null);
        }
    }

    /* compiled from: ConnectionState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Initializing;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class Initializing extends ConnectionState {
        public static final Initializing INSTANCE = new Initializing();

        private Initializing() {
            super(State.INITIALIZING, null);
        }
    }

    /* compiled from: ConnectionState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Ready;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class Ready extends ConnectionState {
        public static final Ready INSTANCE = new Ready();

        private Ready() {
            super(State.READY, null);
        }
    }

    /* compiled from: ConnectionState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Disconnecting;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class Disconnecting extends ConnectionState {
        public static final Disconnecting INSTANCE = new Disconnecting();

        private Disconnecting() {
            super(State.DISCONNECTING, null);
        }
    }

    /* compiled from: ConnectionState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u00062\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u0011\u0010\b\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0007R\u0011\u0010\t\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\t\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0014"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/ConnectionState$Disconnected;", "Lno/nordicsemi/android/ble/livedata/state/ConnectionState;", "reason", "", "(I)V", "isLinkLoss", "", "()Z", "isNotSupported", "isTimeout", "getReason", "()I", "component1", "copy", "equals", "other", "", "hashCode", "toString", "", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final /* data */ class Disconnected extends ConnectionState {
        private final int reason;

        public static /* synthetic */ Disconnected copy$default(Disconnected disconnected, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = disconnected.reason;
            }
            return disconnected.copy(i);
        }

        /* renamed from: component1, reason: from getter */
        public final int getReason() {
            return this.reason;
        }

        public final Disconnected copy(int reason) {
            return new Disconnected(reason);
        }

        public boolean equals(Object other) {
            if (this != other) {
                return (other instanceof Disconnected) && this.reason == ((Disconnected) other).reason;
            }
            return true;
        }

        public int hashCode() {
            return this.reason;
        }

        public String toString() {
            return "Disconnected(reason=" + this.reason + ")";
        }

        public Disconnected(int i) {
            super(State.DISCONNECTED, null);
            this.reason = i;
        }

        public final int getReason() {
            return this.reason;
        }

        public final boolean isLinkLoss() {
            return this.reason == 3;
        }

        public final boolean isNotSupported() {
            return this.reason == 4;
        }

        public final boolean isTimeout() {
            return this.reason == 10;
        }
    }

    public final boolean isConnected() {
        return (this instanceof Initializing) || (this instanceof Ready);
    }

    public final boolean isReady() {
        return this instanceof Ready;
    }
}
