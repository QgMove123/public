package com.example.ricco.constant;

import javax.net.ssl.HostnameVerifier;

/**
 *全局常量
 */
public final class Constant {
    //主机号
    public static final String host = "192.168.3.33";

    //所有与相册相关url内部类
    public static final class Album {
        public static final String showAlbum = "http://"+host+":8080/QGzone/Albums";
        public static final String deleteAlbum = "http://"+host+":8080/QGzone/DeleteAlbum";
        public static final String clearAlbum = "http://"+host+":8080/QGzone/EmptyAlbum";
        public static final String renameAlbum = "http://"+host+":8080/QGzone/RechristenAlbum";
        public static final String showPhoto = "http://"+host+":8080/QGzone/CheckAlbum";
        public static final String inspectAlbum = "http://"+host+":8080/QGzone/InspectAlbum";
        public static final String alterAlbumInfo = "http://"+host+":8080/QGzone/AlterAlbumInformation";
        public static final String uploadPhoto = "http://"+host+":8080/QGzone/UploadPhoto";
        public static final String deletePhoto = "http://"+host+":8080/QGzone/DeletePhoto";
    }

    // 获取的头像的url
    public static final String civUrl = "http://"+ host + ":8080/QGzone/jpg/";

    //Btn的标识
    public static final int BTN_FLAG_DONGTAI = 1;
    public static final int BTN_FLAG_ABOUTME = 2;
    public static final int BTN_FLAG_SEND = 3;
    public static final int BTN_FLAG_ZONE = 4;
    public static final int BTN_FLAG_FRIEND = 5;

    //Fragment的标识
    public static final String FRAGMENT_FLAG_DONGTAI = "好友动态";
    public static final String FRAGMENT_FLAG_ABOUTME = "与我相关";
    public static final String FRAGMENT_FLAG_SEND = "发布说说";
    public static final String FRAGMENT_FLAG_ZONE = "个人空间";
    public static final String FRAGMENT_FLAG_FRIEND = "好友列表";
}