package com.linlazy.mmorpg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Test {
    public static void main(String[] args) {
        testLog4j();
    }

    public static void testLog4j() {
        try {  
            Logger logger = LogManager.getLogger(Test.class);
            logger.trace("this is trace");         
            logger.debug("this is debug");         
            logger.info("this is info");       
            logger.warn("this is warn");       
            logger.error("this is error"); 
            logger.fatal("this is fatal");
        } catch (Exception e) {  
            e.printStackTrace();  
        }      
    }
}
