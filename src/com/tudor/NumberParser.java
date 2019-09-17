package com.tudor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class returns a dialled number in international format
 * If the dialled number is already in international format , it is simply returned
 * If the dialled number is in national format, it's prefix is replaced with the international calling code of the user's number
 * If no national prefix or international calling code is found, the original dialled number is returned. TODO throw exception?
 */
public class NumberParser {

    private final Map<String, Integer> callingCodesHashMap;
    private final Map<String, String> prefixesHashMap;

    /**
     * Constructor takes
     *
     * @param callingCodesHashMap represents mapping between country codes (e.g. "GB", "US") and international dialling code (e.g 44,1)
     * @param prefixesHashMap    represents mapping between country codes (e.g. "GB", "US") and national prefixes (e.g "0","1")
     */
    public NumberParser(Map<String, Integer> callingCodesHashMap, Map<String, String> prefixesHashMap) {

        this.callingCodesHashMap = callingCodesHashMap;
        this.prefixesHashMap = prefixesHashMap;
    }

    /**
     * Method returns a the dialledNumber in international format.
     * If the dialled number is not already in international format,
     * the method formats the number based on the national prefix
     * and the international dialling code associated with the prefix's country.
     *
     * @param userNumber has no use in current implementation. 
     */
    public String parse(String dialledNumber, String userNumber) {

        if (dialledNumber.substring(0, 1).equals("+")) { // If dialled number starts with "+" character, the number is considered to already be in international format.
            return dialledNumber;
        } else { // dialledNumber needs to be formatted in international format.
            return formatNationalNumberToInternationalFormat(dialledNumber,userNumber);
        }

    }

    /**
     * Method tries to return a national number into it's international format.
     * It tries to match all possible international calling codes,
     * if a match with the dataset is found, it tries to match the original prefix with the know country prefix
     * @param dialledNumber is the number which we wish to format.
     * @param userNumber the calling user's number.
     * @return international format of dialledNumber OR original dialledNumber if formatting was not possible
     */
    private String formatNationalNumberToInternationalFormat(String dialledNumber,String userNumber) {

        for (int callingCodeLenght = 1; callingCodeLenght <= 4; callingCodeLenght++) {
            //possible calling code, starting at index 0 and increasing length.
            String possibleCallingCode = userNumber.substring(1, callingCodeLenght + 1);// + 1 offsets "+" sign in international number
            String possibleCountry = findCountryAssociatedWithCallingCode(Integer.valueOf(possibleCallingCode)); // country code associated with possible prefix, empty string if no country found
            if(!possibleCountry.isEmpty()){
                    if(nationalNumberBelongsToCountry(possibleCountry,dialledNumber)){//the User's international calling code country matches with the country associated with the dialled number prefix
                        return buildInternationalFormattedNumber(dialledNumber, prefixesHashMap.get(possibleCountry).length() , callingCodesHashMap.get(possibleCountry));
                    }
                }
            }
        //number cannot be formatted
        return dialledNumber;
    }


    /**
    * Method finds a possible country for the given calling code, in the database.
     * As calling codes are unique, only one match is possible.
     *
     * @return country code associated with calling code OR empty string, if no match was made
    * */
    private String findCountryAssociatedWithCallingCode(Integer possibleCallingCode){
        return callingCodesHashMap.entrySet().stream()
                .filter(p -> p.getValue().equals(possibleCallingCode))
                .map(Map.Entry::getKey)
                .findFirst().orElse("");
    }
    /**
     * Checks whether national number matches to know country prefix
     *
     * @param countryCode of country the number might belong to.
     * @param nationalNumber the number which we want to check.
     * @return returns TRUE if the nationalNumber belongs to the given country, FALSE otherwise
     * */
    private boolean nationalNumberBelongsToCountry(String countryCode, String nationalNumber){
        String countryPrefix = prefixesHashMap.get(countryCode);
        if(!countryPrefix.isEmpty()){
            return countryPrefix.equals(nationalNumber.substring(0,countryPrefix.length()));
        }
        return false;
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
                .append(dialledNumber.substring(prefixLength))
                .toString();

    }
}
