package com.hrtgo.heritagego.heritagego.API;

public class API {

//    URL request API
//    Link server: http://14.161.14.131/hergo
//    http://e567fdy2.somee.com

    private static String HOME_LIKE = "http://e567fdy2.somee.com/api/HeritagerInfor/GetHeritagerInfoHomeLike/";
    private static String HOME_VIEW = "http://e567fdy2.somee.com/api/HeritagerInfor/GetHeritagerInfoHomeView/";
    private static String LOCATION_DETAIL = "http://e567fdy2.somee.com/api/HeritagerInfor/GetHeritagerInfoByID/";
    private static String SEARCH = "http://e567fdy2.somee.com/api/HeritagerInfor/SearchHeritager/";
    private static String LIKED = "http://e567fdy2.somee.com/api/Liked/LikeID";
    private static String VIEWED = "http://e567fdy2.somee.com/api/Viewed/ViewID";
    private static String GET_COMMENTED = "http://e567fdy2.somee.com/api/Commentary/GetCommentaryByHerID/";
    private static String SAVE_COMMENT = "http://e567fdy2.somee.com/api/Commentary/SaveComment";
    private static String IMAGE_LINK = "http://";
    private static String MARKER_LIST = "http://14.161.14.131/hergo/api/HeritagerInfor/GetMarkersList/";

    public static String HOME_LIKE() {
        return HOME_LIKE;
    }

    public static String HOME_VIEW() {
        return HOME_VIEW;
    }

    public static String LOCATION_DETAIL() {
        return LOCATION_DETAIL;
    }

    public static String SEARCH() {
        return SEARCH;
    }

    public static String LIKED() {
        return LIKED;
    }

    public static String VIEWED() {
        return VIEWED;
    }

    public static String GET_COMMENTED() {
        return GET_COMMENTED;
    }

    public static String SAVE_COMMENT() {
        return SAVE_COMMENT;
    }

    public static String IMAGE_LINK() {
        return IMAGE_LINK;
    }

    public static String MARKER_LIST() {
        return MARKER_LIST;
    }
}
