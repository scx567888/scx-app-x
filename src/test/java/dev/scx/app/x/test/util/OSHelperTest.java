package dev.scx.app.x.test.util;

import dev.scx.app.x._util.os.OSHelper;
import org.testng.annotations.Test;

public class OSHelperTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        System.out.println(OSHelper.getOSInfo());
    }

}
