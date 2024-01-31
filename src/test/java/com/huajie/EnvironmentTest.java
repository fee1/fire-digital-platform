package com.huajie;


import com.huajie.domain.common.constants.CommonConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class EnvironmentTest extends BaseTest {

    @Autowired
    private Environment environment;

    @Test
    public void getProperties(){
        String priceStr = environment.getProperty(CommonConstants.ENTERPRISE_TYPE_PRE + "Company");
        System.out.println(priceStr);
    }

}
