package no.nordicsemi.android.ble.livedata.state;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: BondState.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\n\u000b\f\rB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u0082\u0001\u0003\u000e\u000f\u0010¨\u0006\u0011"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/BondState;", "", "state", "Lno/nordicsemi/android/ble/livedata/state/BondState$State;", "(Lno/nordicsemi/android/ble/livedata/state/BondState$State;)V", "isBonded", "", "()Z", "getState", "()Lno/nordicsemi/android/ble/livedata/state/BondState$State;", "Bonded", "Bonding", "NotBonded", "State", "Lno/nordicsemi/android/ble/livedata/state/BondState$NotBonded;", "Lno/nordicsemi/android/ble/livedata/state/BondState$Bonding;", "Lno/nordicsemi/android/ble/livedata/state/BondState$Bonded;", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes.dex */
public abstract class BondState {
    private final State state;

    /* compiled from: BondState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/BondState$State;", "", "(Ljava/lang/String;I)V", "NOT_BONDED", "BONDING", "BONDED", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public enum State {
        NOT_BONDED,
        BONDING,
        BONDED
    }

    private BondState(State state) {
        this.state = state;
    }

    public /* synthetic */ BondState(State state, DefaultConstructorMarker defaultConstructorMarker) {
        this(state);
    }

    public final State getState() {
        return this.state;
    }

    /* compiled from: BondState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/BondState$NotBonded;", "Lno/nordicsemi/android/ble/livedata/state/BondState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class NotBonded extends BondState {
        public static final NotBonded INSTANCE = new NotBonded();

        private NotBonded() {
            super(State.NOT_BONDED, null);
        }
    }

    /* compiled from: BondState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/BondState$Bonding;", "Lno/nordicsemi/android/ble/livedata/state/BondState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class Bonding extends BondState {
        public static final Bonding INSTANCE = new Bonding();

        private Bonding() {
            super(State.BONDING, null);
        }
    }

    /* compiled from: BondState.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lno/nordicsemi/android/ble/livedata/state/BondState$Bonded;", "Lno/nordicsemi/android/ble/livedata/state/BondState;", "()V", "ble-livedata_release"}, k = 1, mv = {1, 1, 16})
    public static final class Bonded extends BondState {
        public static final Bonded INSTANCE = new Bonded();

        private Bonded() {
            super(State.BONDED, null);
        }
    }

    public final boolean isBonded() {
        return this instanceof Bonded;
    }
}
