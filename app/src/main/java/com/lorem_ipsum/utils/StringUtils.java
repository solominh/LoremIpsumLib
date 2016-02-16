package com.lorem_ipsum.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author Originally.US
 */
public class StringUtils {

    public static boolean isNotNull(String string) {
        return !(string == null || string.trim().isEmpty() || "null".equals(string.toLowerCase(Locale.US)));
    }

    public static boolean isNull(String string) {
        return (string == null || string.trim().isEmpty() || "null".equals(string.toLowerCase(Locale.US)));
    }

    /**
     * Capitalize the first char.
     *
     * @param s the string to be capitalized
     * @return Capitalized first char of the given word.
     */
    @SuppressLint("DefaultLocale")
    public static String capitalize(String s) {
        if (s == null)
            return null;
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /**
     * get String from ArrayList string with separator
     */
    public static String join(AbstractCollection<String> s, String delimiter) {
        if (s == null || s.isEmpty()) return "";
        Iterator<String> iterator = s.iterator();
        StringBuilder builder = new StringBuilder(iterator.next());
        while (iterator.hasNext())
            builder.append(delimiter).append(iterator.next());
        return builder.toString();
    }

    /**
     * Convert a String (ex: "AA,BB,CC,DD") to ArrayList<String>
     */
    public static List<String> split(String str, String separator) {
        if (StringUtils.isNull(str))
            return new ArrayList<>();

        String[] arr = str.split(separator);
        if (arr.length <= 0)
            return new ArrayList<>();
        else
            return new ArrayList<>(Arrays.asList(arr));
    }

    /**
     * check string value is a http url
     */

    public static boolean isHttp(String string) {
        return !(string == null || string.trim().length() < 10 || "null".equals(string.toLowerCase(Locale.US)))
                && string.startsWith("http");
    }

    /**
     * check string value is a valid email address
     */

    public static boolean isValidEmail(String string) {
        return !(string == null || string.length() <= 0 || !string.contains("@"))
                && android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    public static String encodeUrl(final String originalUrl) {
        String encodedUrl = originalUrl;
        try {
            URL url = new URL(originalUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            encodedUrl = uri.toURL().toString();
        } catch (Exception e) {

        }
        return encodedUrl;
    }

    /**
     * Convert a String to int, with error catching
     */
    public static int getIntegerValue(String string, int defaultValue) {
        if (isNull(string))
            return defaultValue;

        int intValue;
        try {
            intValue = Integer.parseInt(string);
        } catch (Exception e) {
            intValue = defaultValue;
        }
        return intValue;
    }

    /**
     * Copy a string to default clipboard
     */
    public static void copyToClipboard(Context context, String string) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", string);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * remove keyword: "," and " "
     */
    public static String replaceKeyword(String string, AbstractCollection<String> keywords) {
        if (isNull(string))
            return "";

        for (String elem : keywords) {
            string = string.replace(elem, "");
        }

        return string;
    }

    /**
     * get String from two string with separator
     */
    public static String join(String arrString, String string, String delimiter) {
        if (isNull(string))
            return arrString;

        StringBuilder builder = new StringBuilder(arrString);
        if (isNull(arrString))
            builder.append(string);
        else builder.append(delimiter).append(string);

        return builder.toString();
    }

    /**
     * Convert a String (ex: "1,2,3,4") to ArrayList<Integer>
     */
    public static ArrayList<Integer> getIntegerListFromString(String str, String separator) {
        if (str != null && !str.isEmpty()) {
            String[] array = str.split(separator);
            ArrayList<Integer> ints = new ArrayList<>();

            for (String element : array) {
                try {
                    ints.add(Integer.parseInt(element));
                } catch (NumberFormatException e) {
                    String message = "parsing error with Int value: " + element;
                    LogUtils.d(StringUtils.class.getSimpleName(), e.getMessage() != null ? e.getMessage() : message);
                }
            }
            return ints;

        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Convert a ArrayList<Integer> to String with "separator"
     */
    public static String getStringFromIntegerList(ArrayList<Integer> ints, String separator) {
        StringBuilder strBuilder = new StringBuilder();
        if (!ints.isEmpty()) {
            strBuilder.append(ints.get(0));

            int size = ints.size();
            for (int i = 1; i < size; i++) {
                strBuilder.append(separator);
                strBuilder.append(ints.get(i));
            }
        }

        return strBuilder.toString();
    }

    /**
     * Get String with currency format
     */
    public static String getStringWithCurrencyFormat(String strOld, int digit) {
        double parsed;
        try {
            parsed = Double.parseDouble(strOld);
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMaximumFractionDigits(digit);
        return formatter.format((parsed));
    }

    //-----------------------------------------------------------------------------
    // String encode - hoangminh - 2:48 PM - 2/1/16
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    //- SHA1 - hoangminh - 2:48 PM - 2/1/16

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String sha1Hash(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            hash = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //-----------------------------------------------------------------------------
    //- MD5 - hoangminh - 2:50 PM - 2/1/16

    public static String md5Hash(String url) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(url.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte aMessageDigest : array) {
                sb.append(Integer.toHexString((aMessageDigest & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-----------------------------------------------------------------------------
    // Spannable string - hoangminh - 4:52 PM - 2/1/16
    //-----------------------------------------------------------------------------
    // Spannable string + TextUtils.concat

    public static SpannableString getTextBold(CharSequence s) {
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        return ss;
    }

    public static SpannableString styleText(CharSequence s, @FloatRange float scale) {
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new RelativeSizeSpan(scale), 0, s.length(), 0);
        return ss;
    }


    public static SpannableString styleText(CharSequence s, @IntRange @ColorInt int color) {
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        return ss;
    }

    public static SpannableString styleText(CharSequence s, @FloatRange float scale, @IntRange @ColorInt int color) {
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        ss.setSpan(new RelativeSizeSpan(scale), 0, s.length(), 0);
        return ss;
    }


}
