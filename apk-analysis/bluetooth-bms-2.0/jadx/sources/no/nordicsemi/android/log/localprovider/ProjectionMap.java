package no.nordicsemi.android.log.localprovider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
class ProjectionMap extends HashMap<String, String> {
    private static final long serialVersionUID = -4004367756025538190L;
    private String[] mColumns;

    public static class Builder {
        private ProjectionMap mMap = new ProjectionMap();

        Builder add(String str) {
            this.mMap.putColumn(str, str);
            return this;
        }

        Builder add(String str, String str2) {
            this.mMap.putColumn(str, str2 + " AS " + str);
            return this;
        }

        Builder addAll(String[] strArr) {
            for (String str : strArr) {
                add(str);
            }
            return this;
        }

        Builder addAll(ProjectionMap projectionMap) {
            for (Map.Entry<String, String> entry : projectionMap.entrySet()) {
                this.mMap.putColumn(entry.getKey(), entry.getValue());
            }
            return this;
        }

        ProjectionMap build() {
            String[] strArr = new String[this.mMap.size()];
            this.mMap.keySet().toArray(strArr);
            Arrays.sort(strArr);
            this.mMap.mColumns = strArr;
            return this.mMap;
        }
    }

    private ProjectionMap() {
    }

    static Builder builder() {
        return new Builder();
    }

    public String[] getColumnNames() {
        return this.mColumns;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putColumn(String str, String str2) {
        super.put((ProjectionMap) str, str2);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public String put(String str, String str2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends String, ? extends String> map) {
        throw new UnsupportedOperationException();
    }
}
