package de.thb.anylearn.common;

public class SupportFunctions {

    /**
     * Function to convert a int-Array into a string for url (e.g. categories)
     *
     * @param arr (int-array which should converted to String for URL
     * @return return_string
     */
    public String arrayToUrlString(int[] arr) {
        String return_string = "";
        if (arr != null) {
            for (int a : arr) {
                if (!return_string.equals(""))
                    return_string += ",";
                return_string = return_string + a;
            }
        } else {
            return_string = "0";
        }
        return return_string;
    }

}
