package com.app4mobi.admonition.utils;


import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Srinivas Rupani on 10/14/2016.
 */
public class Constants {
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    public static final String NAVIGATION_ID = "navigation_id";
    public static boolean DEBUGGGABLE_MODE = false;
    public static final String EMPTY_VAL = "";
    public static final String UTF8 = "UTF-8";
    public static final String HIPHEN = " - ";
    public static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES";
    public static String LEFTSQUAREBRACKET = "[";
    public static String RIGHTSQUAREBRACKET = "]";
    public static String FORWARD_SLASH = "/";

    public static final int FILE_DOCUMENT = 1;
    public static final int FILE_LAB_TEST = 2;
    public static final int FILE_HM_TEST = 3;

    public static final int FILE_CHOOSER = 1;
    public static final int FILE_TAKE_PICTURE = 2;
    public static final int FILE_RW = 3;

    public static final String GCM_SENDER_ID = "580725417426";

    //Response Status Codes
    public static final int OK = 200, CREATED = 201, ACCEPTED = 202, PARTIAL_INFORMATION = 203, NO_RESPONSE = 204,
        NOT_MODIFIED = 304, MOVED = 301, FOUND = 302, METHOD = 303, BAD_REQUEST = 400, UNAUTHORIZED = 401, PAYMENT_REQUIRED = 402,
        FORBIDDEN = 403, NOT_FOUND = 404, INTERNAL_ERROR = 500, NOT_IMPLEMENTED = 501, GATEWAY_TIMEOUT = 503;
    //Tables related
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    public static final String TBL_USER = "USER";
    public static final String TBL_ECG = "ECG";
    public static final String TBL_DOCS = "DOCUMENT";
    public static final String DROP_TBL = "DROP TABLE IF EXISTS ";
    public static final String SELECT_STAR = "SELECT * FROM ";
    public static final String INT_PRIMARY_KEY = " INTEGER PRIMARY KEY,";

    //Patient Search PAGE SIZE value
    public static final int PAGE_SIZE = 30;

    public static final String MQTT_BROKER_URL = "tcp://m14.cloudmqtt.com:15997";//tcp://iot.eclipse.org:1883";

    public static final String PUBLISH_TOPIC = "myapp4u1/topic";//"srinivasr/topic";

    public static final String CLIENT_ID = "m4";

//    public static final String MQTT_BROKER_URL = "tcp://iot.eclipse.org:1883";
//
//    public static final String PUBLISH_TOPIC = "androidkt/topic";
//
//    public static final String CLIENT_ID = "androidkt";
}
