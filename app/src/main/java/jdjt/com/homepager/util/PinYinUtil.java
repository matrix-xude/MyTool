package jdjt.com.homepager.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * Created by xxd on 2018/9/20.
 */

public class PinYinUtil {

    /**
     * 获取一个字符串转成拼音后的的首字母，如果不能转拼音，返null
     *
     * @param str
     * @return
     */
    public static String getPinYin(String str) {
        String pinyin = null;
        try {
            pinyin = PinyinHelper.convertToPinyinString(str, " ", PinyinFormat.WITH_TONE_NUMBER);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return pinyin;
    }

    /**
     * 获取一个字符串转成拼音后的的首字母（大写），如果不能转拼音，或者不是[A-Z]，返回#
     *
     * @param str
     * @return
     */
    public static String getFirstLetter(String str) {
        String pinyin = null;
        try {
            pinyin = PinyinHelper.convertToPinyinString(str, " ", PinyinFormat.WITH_TONE_NUMBER);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        if (pinyin == null || pinyin.length() == 0)
            return "#";
        else {
            String firstLetter = pinyin.toUpperCase().substring(0, 1);
            if (firstLetter.matches("[A-Z]"))
                return firstLetter;
            else
                return "#";
        }
    }
}
