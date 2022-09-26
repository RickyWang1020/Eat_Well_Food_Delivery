package com.abc.test;

import org.junit.jupiter.api.Test;

public class UploadFileTest {

    @Test
    public void test() {
        String fileName = "photoacbdeirjkd.png";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }
}
