package com.github.bkhezry.persianner.util;

public class AppUtil {
  private static final Long timeDiff = 1800000L;

  public static boolean isTokenExpire(Long storeTimestamp) {
    return System.currentTimeMillis() - storeTimestamp > timeDiff;
  }
}
