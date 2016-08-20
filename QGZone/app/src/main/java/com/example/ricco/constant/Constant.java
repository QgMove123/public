package com.example.ricco.constant;

/**
 *全局常量
 */
public final class Constant {
    //主机号
    public static final String host = "192.168.3.49:8080";
    //自己的ID，用于访问自己页面
    public static final int HOST_ID = 1;
    //好友ID
    public static int FRIEND_ID = 0;
    //密码格式
    public static final String regex = "[a-z0-9A-Z_]{5,15}";
    //所有与相册相关url内部类
    public static final class Album {
        public static final String createAlbum = "http://"+host+"/QGzone/CreateAlbum";
        public static final String showAlbum = "http://"+host+"/QGzone/Albums";
        public static final String deleteAlbum = "http://"+host+"/QGzone/DeleteAlbum";
        public static final String clearAlbum = "http://"+host+"/QGzone/EmptyAlbum";
        public static final String checkPublicAlbum = "http://"+host+"/QGzone/CheckPublicAlbum";
        public static final String checkPrivacyAlbum = "http://"+host+"/QGzone/CheckPrivacyAlbum";
        public static final String inspectAlbum = "http://"+host+"/QGzone/InspectAlbum";
        public static final String alterAlbumInfo = "http://"+host+"/QGzone/AlterAlbumInformation";
        public static final String uploadPhoto = "http://"+host+"/QGzone/UploadPhoto";
        public static final String deletePhoto = "http://"+host+"/QGzone/DeletePhoto";
        public static final String showPhoto = "http://"+host+"/QGzone/album";
    }

    //所有与用户相关url内部类
    public static final class Account {
        public static final String userSignUp = "http://" + host + "/QGzone/UserSignUp";
        public static final String userSignIn = "http://" + host + "/QGzone/UserSignIn";
        public static final String userSignOut = "http://" + host + "/QGzone/UserSignOut";
        public static final String userCheckSecret = "http://" + host + "/QGzone/UserCheckSecret";
        public static final String userForgetPassword = "http://" + host + "/QGzone/UserForgetPassword";
        public static final String MessageGet = "http://" + host + "/QGzone/MessageGet";
        public static final String MessageSearch = "http://" + host + "/QGzone/MessageSearch";
        public static final String RelationGet = "http://" + host + "/QGzone/RelationsGet";
        public static final String RelationDelete = "http://" + host + "/QGzone/RelationsDelete";
        public static final String RelationRefresh = "http://" + host + "/QGzone/RelationRefresh";
        public static final String RelationGetDetails = "http://" + host + "/QGzone/RelationGetDetails";
        public static final String MessageChange = "http://" + host + "/QGzone/MessageChange";
        public static final String UserChangeSecret = "http://" + host + "/QGzone/UserChangeSecret";
        public static final String UserChangePassword = "http://" + host + "/QGzone/UserChangePassword";
        public static final String UserUploadImage = "http://" + host + "/QGzone/UserUploadImage";
    }

    // 所有与好友相关的的url
    public static final class Friend {
        public static final String friendList = "http://" + host + "/QGzone/MyFriends";
        public static final String deleteFriend = "http://" + host + "/QGzone/DeleteFriend?friendId=";
        public static final String isHaveFriend = "http://" + host + "/QGzone/HaveFriendApply";
        public static final String checkFriend = "http://" + host + "/QGzone/ConductFriendApply?friendApplyId=";
        public static final String addFriend = "http://" + host + "/QGzone/SendFriendApply?addFriendId=";
        public static final String searchName = "http://" + host + "/QGzone/SearchByUserName?searchName=";
        public static final String searchAccount = "http://" + host + "/QGzone/SearchByUserId?searchId=";
        public static final String friendRequest = "http://" + host + "/QGzone/MyFriendApply";
        public static final String deleteRequest = "http://" + host + "/QGzone/DeleteFriendApply?friendApplyId=";
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
