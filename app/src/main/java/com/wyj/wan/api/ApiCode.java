package com.wyj.wan.api;


import com.wyj.wan.R;

import java.util.Hashtable;
import java.util.Map;



public class ApiCode {

    public static Map<Integer, APIResultCode> sApiCodeMap = new Hashtable<>();

    public static final int CODE_SUCCESS = 0;

    static {
        sApiCodeMap.put(0, new APIResultCode(0, R.string.code0, false));

//        sApiCodeMap.put(4001, new APIResultCode(4001, R.string.code4001, true));
//        sApiCodeMap.put(4011, new APIResultCode(4011, R.string.code4011, false));
//        sApiCodeMap.put(4003, new APIResultCode(4003, R.string.code4003, true));
//        sApiCodeMap.put(4004, new APIResultCode(4004, R.string.code4004, false));
//        sApiCodeMap.put(4404, new APIResultCode(4404, R.string.code4404, false));
//
//        sApiCodeMap.put(1001, new APIResultCode(1001, R.string.code1001, true));
//        sApiCodeMap.put(1002, new APIResultCode(1002, R.string.code1002, true));
//        sApiCodeMap.put(1003, new APIResultCode(1003, R.string.code1003, true));
//        sApiCodeMap.put(1004, new APIResultCode(1004, R.string.code1004, true));
//        sApiCodeMap.put(1005, new APIResultCode(1005, R.string.code1005, true));
//        sApiCodeMap.put(1006, new APIResultCode(1006, R.string.code1006, true));
//        sApiCodeMap.put(1007, new APIResultCode(1007, R.string.code1007, true));
//        sApiCodeMap.put(1008, new APIResultCode(1008, R.string.code1008, true));
//        sApiCodeMap.put(1009, new APIResultCode(1009, R.string.code1009, true));
//        sApiCodeMap.put(1010, new APIResultCode(1010, R.string.code1010, true));
//
//        sApiCodeMap.put(2001, new APIResultCode(2001, R.string.code2001, true));
//        sApiCodeMap.put(2002, new APIResultCode(2002, R.string.code2002, true));
//        sApiCodeMap.put(2003, new APIResultCode(2003, R.string.code2003, true));
//        sApiCodeMap.put(2004, new APIResultCode(2004, R.string.code2004, true));
//        sApiCodeMap.put(2005, new APIResultCode(2005, R.string.code2005, true));
//        sApiCodeMap.put(2006, new APIResultCode(2006, R.string.code2006, true));
//        sApiCodeMap.put(2007, new APIResultCode(2007, R.string.code2007, true));
//        sApiCodeMap.put(2008, new APIResultCode(2008, R.string.code2008, true));
//        sApiCodeMap.put(2009, new APIResultCode(2009, R.string.code2009, true));
//        sApiCodeMap.put(2010, new APIResultCode(2010, R.string.code2010, true));
//
//        sApiCodeMap.put(3001, new APIResultCode(3001, R.string.code3001, false));
//        sApiCodeMap.put(3002, new APIResultCode(3002, R.string.code3002, true));
//        sApiCodeMap.put(3003, new APIResultCode(3003, R.string.code3003, true));
//        sApiCodeMap.put(3004, new APIResultCode(3004, R.string.code3004, true));
//        sApiCodeMap.put(3005, new APIResultCode(3005, R.string.code3005, true));
//        sApiCodeMap.put(3006, new APIResultCode(3006, R.string.code3006, true));
//        sApiCodeMap.put(3007, new APIResultCode(3007, R.string.code3007, true));

    }


    public static class APIResultCode {
        public int code;
        public int tipStringId;

        public boolean isNeedTip;

        public APIResultCode(int code, int tipStringId, boolean isNeedTip) {
            this.code = code;
            this.tipStringId = tipStringId;
            this.isNeedTip = isNeedTip;
        }
    }

}
