package com.foundy.hansungcafeteria.util

import io.kotlintest.Spec
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone

class DateUtilsTest : FunSpec() {
    override fun beforeSpec(spec: Spec) {
        DateTimeZone.setDefault(DateTimeZone.UTC)
    }

    init {
        test("getMonToFriDateString works correctly if today is monday") {
            DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 21, 3, 0).millis)
            DateTime.now().dayOfWeek().get() shouldBe DateTimeConstants.MONDAY

            val result = getMonToFriDateString()
            result shouldBe "3/21 - 3/25"
        }

        test("getMonToFriDateString works correctly if today is sunday") {
            DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 27, 3, 0).millis)
            DateTime.now().dayOfWeek().get() shouldBe DateTimeConstants.SUNDAY

            val result = getMonToFriDateString()
            result shouldBe "3/21 - 3/25"
        }

        test("getMonToFriDateString works correctly if today is friday") {
            DateTimeUtils.setCurrentMillisFixed(DateTime(2022, 3, 25, 3, 0).millis)
            DateTime.now().dayOfWeek().get() shouldBe DateTimeConstants.FRIDAY

            val result = getMonToFriDateString()
            result shouldBe "3/21 - 3/25"
        }
    }
}