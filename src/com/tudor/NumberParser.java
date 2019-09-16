package com.tudor;

import java.util.Map;
import java.util.Optional;

/**
 * This class returns a dialled number in international format
 * <p>
 * TODO: Class makes assumption that national country codes are unique world wide, in order to associate national codes with international codes
 */
public class NumberParser {

    private final Map<String, Integer> callingCode;
    private final Map<String, String> prefixes;

    /**
     * Constructor takes
     *
     * @param callingCode represents mapping between country codes (e.g. "GB", "US") and international dialling code (e.g 44,1)
     * @param prefixes    represents mapping between country codes (e.g. "GB", "US") and national prefixes (e.g "0","1")
     */
    public NumberParser(Map<String, Integer> callingCode, Map<String, String> prefixes) {

        this.callingCode = callingCode;
        this.prefixes = prefixes;
    }

    /**
     * Method returns a the dialledNumber in international format.
     * If the dialled number is not already in international format,
     * the method formats the number based on the national prefix
     * and the international dialling code associated with the prefix's country.
     *
     * @param userNumber has no use in current implementation. TODO: Method makes no use of @param userNumber
     */
    public String parse(String dialledNumber, String userNumber) {

        if (dialledNumber.substring(0, 1).equals("+")) { // If dialled number starts with "+" character, the number is considered to already be in international format.
            return dialledNumber;
        } else { // dialledNumber needs to be formatted in international format.
            return formatNationalNumberToInternationalFormat(dialledNumber);
        }

    }

    /**
     * Method tries to return a national number into it's international format.
     *
     * @param dialledNumber is the number which we wish to format.
     */
    private String formatNationalNumberToInternationalFormat(String dialledNumber) {
        for (int prefixLength = 1; prefixLength < dialledNumber.length(); prefixLength++) {

            //possible prefix, starting at index 0 and increasing length.
            String possiblePrefix = dialledNumber.substring(0, prefixLength);
            int countryCode = nationalToInternationalDiallingCode(possiblePrefix);
            if (countryCode > 0) {// there exists an associated international code with the possible prefix
                return buildInternationalFormattedNumber(dialledNumber,prefixLength,countryCode);
            }
        }

        //number cannot be formatted
        return dialledNumber;
    }

    /**
     * Method returns the associated international dialling code to the given national dialling code.
     *
     * @param nationalDiallingCode is the national prefix for which we want the international dialling code.
     * @return international dialling code OR -1 if no international dialling code is found.
     */
    private int nationalToInternationalDiallingCode(String nationalDiallingCode) {

        String absentCountryCode = "NoCty";

        // find country code associated with prefix, if this method is called we are sure the country code exists in the hashmap
        Optional<Map.Entry<String, String>> optionalCountryCode = prefixes.entrySet().stream()
                .filter(p -> p.getValue().equals(nationalDiallingCode))
                .findAny();
        String countryCode = optionalCountryCode.isPresent() ? optionalCountryCode.get().getKey() : absentCountryCode;

        if (!countryCode.equals(absentCountryCode)) {//there exists an entry with the possible prefix given as parameter
            Integer callingCodeForPrefix = callingCode.get(countryCode);
            if (callingCode.get(countryCode) != null) {//there exists and associated international code with the possible prefix given as parameter
                return callingCodeForPrefix;
            } else {//there's not international prefix found for the possible prefix.
                return -1;
            }
        }
        return -1;
    }

    /**
     * Builds the final international format dialling number
     *
     * @param dialledNumber initial dialled number
     * @param prefixLength length of national prefix
     * @param countryCode international country code associated with national prefix
     * */
    private String buildInternationalFormattedNumber(String dialledNumber, int prefixLength,int countryCode){
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder
                .append("+")
                .append(countryCode)
                .append(dialledNumber.substring(prefixLength, dialledNumber.length() - prefixLength + 1))
                .toString();

    }
}
