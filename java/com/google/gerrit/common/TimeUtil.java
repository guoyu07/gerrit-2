// Copyright (C) 2013 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.common;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.sql.Timestamp;
import java.util.function.LongSupplier;

/** Static utility methods for dealing with dates and times. */
@GwtIncompatible("Unemulated Java 8 functionalities")
public class TimeUtil {
  private static final LongSupplier SYSTEM_CURRENT_MILLIS_SUPPLIER = System::currentTimeMillis;

  private static volatile LongSupplier currentMillisSupplier = SYSTEM_CURRENT_MILLIS_SUPPLIER;

  public static long nowMs() {
    // We should rather use Instant.now(Clock).toEpochMilli() instead but this would require some
    // changes in our testing code as we wouldn't have clock steps anymore.
    return currentMillisSupplier.getAsLong();
  }

  public static Timestamp nowTs() {
    return new Timestamp(nowMs());
  }

  public static Timestamp roundToSecond(Timestamp t) {
    return new Timestamp((t.getTime() / 1000) * 1000);
  }

  @VisibleForTesting
  public static void setCurrentMillisSupplier(LongSupplier customCurrentMillisSupplier) {
    currentMillisSupplier = customCurrentMillisSupplier;
  }

  @VisibleForTesting
  public static void resetCurrentMillisSupplier() {
    currentMillisSupplier = SYSTEM_CURRENT_MILLIS_SUPPLIER;
  }

  private TimeUtil() {}
}
