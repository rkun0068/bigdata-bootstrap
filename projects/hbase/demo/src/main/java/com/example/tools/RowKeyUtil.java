package com.example.tools;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class RowKeyUtil {
    public static String generateRowKey(String appId, String appName) {
        String prefix = getMurmurHashPrefix(appId + appName); // Compute hash prefix
        return prefix + "_" + appId;
    }

    private static String getMurmurHashPrefix(String key) {
        return Hashing.murmur3_32()
                .hashString(key, StandardCharsets.UTF_8)
                .toString()
                .substring(0, 8); // Take the first 8 characters
    }

    public static void main(String[] args) {
        String appId = "com.ishakwe.gakondo";
        String appName = "Gakondo";
        String rowKey = generateRowKey(appId, appName);
        System.out.println("Generated RowKey: " + rowKey);
    }
}
