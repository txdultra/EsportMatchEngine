package com.cj.engine;

import com.cj.engine.cfg.SpringAppContext;
import com.cj.engine.core.IMatchEngine;
import com.cj.engine.core.MResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EngineApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Test
    public void testSingleMatchPattern() {

        IMatchEngine engine = SpringAppContext.getBean(IMatchEngine.class, 1);

        engine.init();
        MResult result = engine.buildSchedule(128);
        Assert.isTrue(result.getCode() == MResult.SUCCESS_CODE,"success");
        engine.save();
    }
}
