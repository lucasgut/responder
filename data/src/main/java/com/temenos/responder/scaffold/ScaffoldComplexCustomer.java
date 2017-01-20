package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * Created by dgroves on 19/01/2017.
 */
public class ScaffoldComplexCustomer implements Scaffold {
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final Type CUSTOMER_NAME_TYPE = Type.STRING;
    public static final String CUSTOMER_ADDRESSES = "Addresses";
    public static final Type CUSTOMER_ADDRESSES_TYPE = Type.ARRAY;
    public static final String CUSTOMER_ADDRESSES_ITEM = "Addresses[%d]";
    public static final Type CUSTOMER_ADDRESSES_ITEM_TYPE = Type.OBJECT;
    public static final String CUSTOMER_ADDRESSES_ITEM_HOUSENUMBER = "Addresses[%d].HouseNumber";
    public static final Type CUSTOMER_ADDRESSES_ITEM_HOUSENUMBER_TYPE = Type.INTEGER;
    public static final String CUSTOMER_ADDRESSES_ITEM_ROAD = "Addresses[%d].Road";
    public static final Type CUSTOMER_ADDRESSES_ITEM_ROAD_TYPE = Type.STRING;
}
