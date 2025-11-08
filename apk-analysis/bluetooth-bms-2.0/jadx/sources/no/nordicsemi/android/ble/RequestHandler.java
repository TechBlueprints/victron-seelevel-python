package no.nordicsemi.android.ble;

/* loaded from: classes.dex */
abstract class RequestHandler implements CallbackHandler {
    abstract void cancelQueue();

    abstract void enqueue(Request request);

    abstract void onRequestTimeout(TimeoutableRequest timeoutableRequest);

    RequestHandler() {
    }
}
