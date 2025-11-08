package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealConnection;
import okio.BufferedSink;
import okio.Okio;

/* compiled from: CallServerInterceptor.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lokhttp3/internal/http/CallServerInterceptor;", "Lokhttp3/Interceptor;", "forWebSocket", "", "(Z)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "okhttp"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes.dex */
public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        boolean z;
        Response responseBuild;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        Exchange exchange = realInterceptorChain.exchange();
        Request request = realInterceptorChain.getRequest();
        RequestBody requestBodyBody = request.body();
        long jCurrentTimeMillis = System.currentTimeMillis();
        exchange.writeRequestHeaders(request);
        Response.Builder responseHeaders = (Response.Builder) null;
        if (HttpMethod.permitsRequestBody(request.method()) && requestBodyBody != null) {
            if (StringsKt.equals("100-continue", request.header("Expect"), true)) {
                exchange.flushRequest();
                exchange.responseHeadersStart();
                responseHeaders = exchange.readResponseHeaders(true);
                z = true;
            } else {
                z = false;
            }
            if (responseHeaders == null) {
                if (requestBodyBody.isDuplex()) {
                    exchange.flushRequest();
                    requestBodyBody.writeTo(Okio.buffer(exchange.createRequestBody(request, true)));
                } else {
                    BufferedSink bufferedSinkBuffer = Okio.buffer(exchange.createRequestBody(request, false));
                    requestBodyBody.writeTo(bufferedSinkBuffer);
                    bufferedSinkBuffer.close();
                }
            } else {
                exchange.noRequestBody();
                RealConnection realConnectionConnection = exchange.connection();
                if (realConnectionConnection == null) {
                    Intrinsics.throwNpe();
                }
                if (!realConnectionConnection.isMultiplexed()) {
                    exchange.noNewExchangesOnConnection();
                }
            }
        } else {
            exchange.noRequestBody();
            z = false;
        }
        if (requestBodyBody == null || !requestBodyBody.isDuplex()) {
            exchange.finishRequest();
        }
        if (!z) {
            exchange.responseHeadersStart();
        }
        if (responseHeaders == null && (responseHeaders = exchange.readResponseHeaders(false)) == null) {
            Intrinsics.throwNpe();
        }
        Response.Builder builderRequest = responseHeaders.request(request);
        RealConnection realConnectionConnection2 = exchange.connection();
        if (realConnectionConnection2 == null) {
            Intrinsics.throwNpe();
        }
        Response responseBuild2 = builderRequest.handshake(realConnectionConnection2.getHandshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int iCode = responseBuild2.code();
        if (iCode == 100) {
            Response.Builder responseHeaders2 = exchange.readResponseHeaders(false);
            if (responseHeaders2 == null) {
                Intrinsics.throwNpe();
            }
            Response.Builder builderRequest2 = responseHeaders2.request(request);
            RealConnection realConnectionConnection3 = exchange.connection();
            if (realConnectionConnection3 == null) {
                Intrinsics.throwNpe();
            }
            responseBuild2 = builderRequest2.handshake(realConnectionConnection3.getHandshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            iCode = responseBuild2.code();
        }
        exchange.responseHeadersEnd(responseBuild2);
        if (this.forWebSocket && iCode == 101) {
            responseBuild = responseBuild2.newBuilder().body(Util.EMPTY_RESPONSE).build();
        } else {
            responseBuild = responseBuild2.newBuilder().body(exchange.openResponseBody(responseBuild2)).build();
        }
        if (StringsKt.equals("close", responseBuild.request().header("Connection"), true) || StringsKt.equals("close", Response.header$default(responseBuild, "Connection", null, 2, null), true)) {
            exchange.noNewExchangesOnConnection();
        }
        if (iCode == 204 || iCode == 205) {
            ResponseBody responseBodyBody = responseBuild.body();
            if ((responseBodyBody != null ? responseBodyBody.getContentLength() : -1L) > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("HTTP ");
                sb.append(iCode);
                sb.append(" had non-zero Content-Length: ");
                ResponseBody responseBodyBody2 = responseBuild.body();
                sb.append(responseBodyBody2 != null ? Long.valueOf(responseBodyBody2.getContentLength()) : null);
                throw new ProtocolException(sb.toString());
            }
        }
        return responseBuild;
    }
}
