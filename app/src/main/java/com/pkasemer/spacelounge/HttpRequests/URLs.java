package com.pkasemer.spacelounge.HttpRequests;
/**
 * Created by Belal on 9/5/2017.
 */

public class URLs {

    private static final String ROOT_URL = "http://pk.kakebe.com/ZodongoFoodsAPI/";
//    private static final String ROOT_URL = "https://ac99-41-75-188-97.ngrok.io/ZodongoFoodsAPI/";
    public static final String URL_REGISTER = ROOT_URL + "users/account?apicall=signup";
    public static final String URL_LOGIN= ROOT_URL + "users/account.php?apicall=login";

    public static final String URL_SEND_ORDER = ROOT_URL + "orders/create_order.php";

}
