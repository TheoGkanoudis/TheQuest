package com.example.quest.utilities;

public class Variables {
    //login/signup page state
    public static final int SIGNUP = 0;
    public static final int LOGIN = 1;

    //username errors
    public static final int GOOD_USERNAME = 0;
    public static final int USER_LONG = 1;
    public static final int USER_SHORT = 2;
    public static final int USER_WRONG_CHARS = 3;
    public static final int USER_MIN_LENGTH = 6;
    public static final int USER_MAX_LENGTH = 20;
    //password errors
    public static final int GOOD_PASSWORD = 0;
    public static final int PASS_LONG = 1;
    public static final int PASS_SHORT = 2;
    public static final int PASS_WRONG_CHARS = 3;
    public static final int PASS_MIN_LENGTH = 6;
    public static final int PASS_MAX_LENGTH = 15;

    //login and signup
    public static final int LOGIN_SUCCESSFUL = 0;
    public static final int LOGIN_ERROR = 1;
    public static final int SIGNUP_SUCCESSFUL = 0;
    public static final int SIGNUP_ERROR = 1;
    public static final int USERNAME_EXISTS = 2;
    public static final int EMAIL_EXISTS = 3;
    public static final int WRONG_USERNAME = 4;
    public static final int WRONG_PASSWORD = 5;
    public static final int WRONG_USERNAME_PASSWORD = 6;
    public static final String KEY_PREFERENCE_NAME = "theQuestPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ID = "id";

    //main menu
    public static int OFFSCREEN_PAGE_LIMIT = 3;
}
