package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * Created by dgroves on 19/01/2017.
 */
public class ScaffoldAddress {
    public static final String ADDRESS_ID = "AddressId";
    public static final Type ADDRESS_ID_TYPE = Type.INTEGER;
    public static final String ADDRESSES = "Addresses";
    public static final Type ADDRESSES_TYPE = Type.ARRAY;
    public static final String ADDRESSES_ITEM = "Addresses[%d]";
    public static final Type ADDRESSES_ITEM_TYPE = Type.OBJECT;
    public static final String ADDRESSES_ITEM_HOUSE_NUMBER = "Addresses[%d].HouseNumber";
    public static final Type ADDRESSES_ITEM_HOUSE_NUMBER_TYPE = Type.INTEGER;
    public static final String ADDRESSES_ITEM_ROAD = "Addresses[%d].Road";
    public static final Type ADDRESSES_ITEM_ROAD_TYPE = Type.STRING;
}
