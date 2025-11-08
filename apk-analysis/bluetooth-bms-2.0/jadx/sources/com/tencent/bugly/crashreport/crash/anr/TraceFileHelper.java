package com.tencent.bugly.crashreport.crash.anr;

import com.jkcq.util.DateUtil;
import com.tencent.bugly.proguard.al;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class TraceFileHelper {

    /* compiled from: BUGLY */
    public static class a {
        public long a;
        public String b;
        public long c;
        public Map<String, String[]> d;
    }

    /* compiled from: BUGLY */
    public interface b {
        boolean a(long j);

        boolean a(long j, long j2, String str);

        boolean a(String str, int i, String str2, String str3);
    }

    public static a readTargetDumpInfo(final String str, String str2, final boolean z) throws Throwable {
        if (str != null && str2 != null) {
            final a aVar = new a();
            readTraceFile(str2, new b() { // from class: com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.1
                @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
                public final boolean a(String str3, int i, String str4, String str5) {
                    al.c("new thread %s", str3);
                    if (aVar.a > 0 && aVar.c > 0 && aVar.b != null) {
                        if (aVar.d == null) {
                            aVar.d = new HashMap();
                        }
                        aVar.d.put(str3, new String[]{str4, str5, String.valueOf(i)});
                    }
                    return true;
                }

                @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
                public final boolean a(long j, long j2, String str3) {
                    al.c("new process %s", str3);
                    if (!str3.equals(str)) {
                        return true;
                    }
                    aVar.a = j;
                    aVar.b = str3;
                    aVar.c = j2;
                    return z;
                }

                @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
                public final boolean a(long j) {
                    al.c("process end %d", Long.valueOf(j));
                    return aVar.a <= 0 || aVar.c <= 0 || aVar.b == null;
                }
            });
            if (aVar.a > 0 && aVar.c > 0 && aVar.b != null) {
                return aVar;
            }
        }
        return null;
    }

    public static a readFirstDumpInfo(String str, final boolean z) throws Throwable {
        if (str == null) {
            al.e("path:%s", str);
            return null;
        }
        final a aVar = new a();
        readTraceFile(str, new b() { // from class: com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.2
            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(String str2, int i, String str3, String str4) {
                al.c("new thread %s", str2);
                if (aVar.d == null) {
                    aVar.d = new HashMap();
                }
                aVar.d.put(str2, new String[]{str3, str4, String.valueOf(i)});
                return true;
            }

            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(long j, long j2, String str2) {
                al.c("new process %s", str2);
                aVar.a = j;
                aVar.b = str2;
                aVar.c = j2;
                return z;
            }

            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(long j) {
                al.c("process end %d", Long.valueOf(j));
                return false;
            }
        });
        if (aVar.a > 0 && aVar.c > 0 && aVar.b != null) {
            return aVar;
        }
        al.e("first dump error %s", aVar.a + " " + aVar.c + " " + aVar.b);
        return null;
    }

    public static void readTraceFile(String str, b bVar) throws Throwable {
        Throwable th;
        BufferedReader bufferedReader;
        if (str == null || bVar == null) {
            return;
        }
        File file = new File(str);
        if (!file.exists()) {
            return;
        }
        file.lastModified();
        file.length();
        BufferedReader bufferedReader2 = null;
        int i = 0;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            Pattern patternCompile = Pattern.compile("-{5}\\spid\\s\\d+\\sat\\s\\d+-\\d+-\\d+\\s\\d{2}:\\d{2}:\\d{2}\\s-{5}");
            Pattern patternCompile2 = Pattern.compile("-{5}\\send\\s\\d+\\s-{5}");
            Pattern patternCompile3 = Pattern.compile("Cmd\\sline:\\s(\\S+)");
            Pattern patternCompile4 = Pattern.compile("\".+\"\\s(daemon\\s){0,1}prio=\\d+\\stid=\\d+\\s.*");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS, Locale.US);
            while (true) {
                Pattern[] patternArr = new Pattern[1];
                patternArr[i] = patternCompile;
                Object[] objArrA = a(bufferedReader, patternArr);
                if (objArrA == null) {
                    try {
                        bufferedReader.close();
                        return;
                    } catch (IOException e2) {
                        if (al.a(e2)) {
                            return;
                        }
                        e2.printStackTrace();
                        return;
                    }
                }
                Pattern[] patternArr2 = new Pattern[1];
                patternArr2[i] = patternCompile3;
                Object[] objArrA2 = a(bufferedReader, patternArr2);
                if (objArrA2 == null) {
                    al.d("Failed to find process name.", new Object[i]);
                    try {
                        bufferedReader.close();
                        return;
                    } catch (IOException e3) {
                        if (al.a(e3)) {
                            return;
                        }
                        e3.printStackTrace();
                        return;
                    }
                }
                String[] strArrSplit = objArrA[1].toString().split("\\s");
                long j = Long.parseLong(strArrSplit[2]);
                long time = simpleDateFormat.parse(strArrSplit[4] + " " + strArrSplit[5]).getTime();
                Matcher matcher = patternCompile3.matcher(objArrA2[1].toString());
                matcher.find();
                matcher.group(1);
                SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
                if (!bVar.a(j, time, matcher.group(1))) {
                    try {
                        bufferedReader.close();
                        return;
                    } catch (IOException e4) {
                        if (al.a(e4)) {
                            return;
                        }
                        e4.printStackTrace();
                        return;
                    }
                }
                while (true) {
                    Object[] objArrA3 = a(bufferedReader, patternCompile4, patternCompile2);
                    if (objArrA3 == null) {
                        break;
                    }
                    if (objArrA3[0] == patternCompile4) {
                        String string = objArrA3[1].toString();
                        Matcher matcher2 = Pattern.compile("\".+\"").matcher(string);
                        matcher2.find();
                        String strGroup = matcher2.group();
                        String strSubstring = strGroup.substring(1, strGroup.length() - 1);
                        string.contains("NATIVE");
                        Matcher matcher3 = Pattern.compile("tid=\\d+").matcher(string);
                        matcher3.find();
                        String strGroup2 = matcher3.group();
                        bVar.a(strSubstring, Integer.parseInt(strGroup2.substring(strGroup2.indexOf("=") + 1)), a(bufferedReader), b(bufferedReader));
                    } else if (!bVar.a(Long.parseLong(objArrA3[1].toString().split("\\s")[2]))) {
                        try {
                            bufferedReader.close();
                            return;
                        } catch (IOException e5) {
                            if (al.a(e5)) {
                                return;
                            }
                            e5.printStackTrace();
                            return;
                        }
                    }
                }
                simpleDateFormat = simpleDateFormat2;
                i = 0;
            }
        } catch (Exception e6) {
            e = e6;
            bufferedReader2 = bufferedReader;
            if (!al.a(e)) {
                e.printStackTrace();
            }
            al.d("trace open fail:%s : %s", e.getClass().getName(), e.getMessage());
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e7) {
                    if (al.a(e7)) {
                        return;
                    }
                    e7.printStackTrace();
                }
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 == null) {
                throw th;
            }
            try {
                bufferedReader2.close();
                throw th;
            } catch (IOException e8) {
                if (al.a(e8)) {
                    throw th;
                }
                e8.printStackTrace();
                throw th;
            }
        }
    }

    private static Object[] a(BufferedReader bufferedReader, Pattern... patternArr) throws IOException {
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return null;
            }
            for (Pattern pattern : patternArr) {
                if (pattern.matcher(line).matches()) {
                    return new Object[]{pattern, line};
                }
            }
        }
    }

    private static String a(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return null;
            }
            stringBuffer.append(line + "\n");
        }
        return stringBuffer.toString();
    }

    private static String b(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null || line.trim().length() <= 0) {
                break;
            }
            stringBuffer.append(line + "\n");
        }
        return stringBuffer.toString();
    }
}
