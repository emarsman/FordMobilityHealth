package org.motechproject.mHealthDataInterface.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.mHealthDataInterface.utility.mHealthException;


/**
 * Custom class for generic utilities
 */
public class GenericUtility {

    /**
     * get data as string
     */
    public static String getJsonObject(String path) throws mHealthException {

        RestInvoker restInvoker = new RestInvoker();
        String json = restInvoker.getDataFromServer(path);

        return json;
    }


    /**
     * setting Gson date format
     */
    public static Gson gsonDateFormat() throws mHealthException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZZZZ").create();

        return gson;
    }


}
