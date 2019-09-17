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
        countryCodes.put("FR", 33);
        countryCodes.put("CHN", 86);

        prefixes.put("GB","0");
        prefixes.put("US","1");
        prefixes.put("FR","0");
        prefixes.put("CHN", "072");


        NumberParser numberParser = new NumberParser(countryCodes,prefixes);

        assertEquals("+442079460056",numberParser.parse("02079460056","+441614960178"));
        assertEquals("+442079460056",numberParser.parse("+442079460056","+441614960178"));
        assertEquals("12079460056",numberParser.parse("12079460056","+441614960178"));
        assertEquals("212079460056",numberParser.parse("212079460056","+441614960178"));
        assertEquals("+33108822726", numberParser.parse("0108822726", "+33109758351")); //This is the new test scenario

        //extra tests
        // national to international
        assertEquals("+442079460056",numberParser.parse("02079460056","+441614960178"));
        assertEquals("+12079460056",numberParser.parse("12079460056","+11614960178"));
        assertEquals("+33108822726",numberParser.parse("0108822726","+331614960178"));
        assertEquals("+862079460056",numberParser.parse("0722079460056","+861614960178"));

        //international to international
        assertEquals("+12079460056",numberParser.parse("+12079460056","+11614960178"));
        assertEquals("+441614960178",numberParser.parse("+441614960178","+861614960178"));

        //no national prefix known
        assertEquals("212079460056",numberParser.parse("212079460056","+441614960178"));

        //international calling code not known
        assertEquals("212079460056",numberParser.parse("212079460056","+761614960178"));

    }
}
