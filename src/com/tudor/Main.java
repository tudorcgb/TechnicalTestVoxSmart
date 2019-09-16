package com.tudor;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Main {

    @Test
    public void simpleEqualsTest(){

        Map<String, Integer> countryCodes = new HashMap<>();
        Map<String, String> prefixes = new HashMap<>();

        countryCodes.put("GB",44);
        countryCodes.put("US",1);
        prefixes.put("GB","0");
        prefixes.put("US","1");

        NumberParser numberParser = new NumberParser(countryCodes,prefixes);
        assertEquals("+442079460056",numberParser.parse("02079460056","+441614960178"));
        assertEquals("+442079460056",numberParser.parse("+442079460056","+441614960178"));
        assertEquals("+12079460056",numberParser.parse("12079460056","+441614960178"));
        assertEquals("212079460056",numberParser.parse("212079460056","+441614960178"));
    }
}
