package com.example.cne_shop.contents;

/**
 * Created by 博 on 2017/7/20.
 */

public class Contents {

    public static final String WARE = "WARE" ;
    public static final String DES_KEY = "Cniao5_123456" ;
    public static final String USER_JSON = "user_json" ;
    public static final String TOKEN = "token" ;
    public static final  int REQUEST_CODE = 1 ;
    public static final  int REQUEST_ORDER_CODE = 2 ;
    public static final int REQUEST_CONSIGNEE_ADR = 3 ;
    public static final int REQUEST_ORDER_CONSIGNEE = 4 ;

    public static final String ALIPAY = "alipay" ;
    public static final String WX = "wx" ;
    public static final String BD = "bfd" ;

    public class API{

        public static final String COURSE_API = "http://112.124.22.238:8081/course_api/" ;

        public static final String RECOMMEND = COURSE_API + "campaign/recommend" ;
        public static final String QUERY_SLIDER = COURSE_API+"banner/query" ;
        public static final String HOT = COURSE_API+"wares/hot" ;
        public static final String WARE_LIST = COURSE_API+"wares/list" ;
        public static final String CATEGORY_LIST = COURSE_API+"category/list" ;
        public static final String SHOW_WARE_DETIAL = COURSE_API+"wares/detail.html" ;
        public static final String LOGIN = COURSE_API+"auth/login" ;
        public static final String REGISTER =COURSE_API+"auth/reg" ;
        public static final String SUBMIT_ORDER = COURSE_API+"order/create" ;
        public static final String CHANGE_ORDER_STATUS = COURSE_API+"order/complete" ;
        public static final String GET_CONSIGNEE = COURSE_API+"addr/list" ;
        public static final String CREATE_CONSIGNEE = COURSE_API+"addr/create" ;
        public static final String DELETE_CONSIGNEE = COURSE_API+"addr/del" ;
        public static final String UPDATE_CONSIGNEE = COURSE_API+"addr/update" ;
        public static final String GET_ORDER_LIST = COURSE_API+"order/list" ;

    }

}
