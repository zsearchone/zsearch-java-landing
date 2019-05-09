package com.alipay.zsearch.landing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {

    @Autowired
    Landing landing;

    @Test
    public void go() throws IOException {
        landing.createIndex();
        landing.bulk();
        landing.search();
        landing.deleteIndex();
        landing.close();
    }

}
