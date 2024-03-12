package com.indie.apps.pannypal;

import android.text.TextUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.indie.apps.pannypal.Model.UserProfile;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class Globle {

    public static UserProfile MyProfile = null;

    public static String getFormattedValue(Double value)
    {
        DecimalFormat format = new DecimalFormat("##,##,##0.##");
        return format.format(value) + " ₹";
    }

    public static String getValue(Double value)
    {
        DecimalFormat format = new DecimalFormat("##0.##");
        return format.format(value);
    }


    public static boolean isValidPhoneNumber(String countryCode,String phoneNumber) {

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(phoneNumber, countryCode);
            return phoneUtil.isValidNumber(swissNumberProto);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            return  false;
        }
    }
}
