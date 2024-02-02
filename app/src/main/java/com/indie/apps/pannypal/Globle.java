package com.indie.apps.pannypal;

import com.indie.apps.pannypal.Model.UserProfile;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

public class Globle {

    public static UserProfile MyProfile = null;

    public static String getFormattedValue(Double value)
    {
        DecimalFormat format = new DecimalFormat("##,##,##0.##");
        return format.format(value) + " ₹";
    }
}
