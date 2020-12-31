package lsy.mypda;

import android.text.TextUtils;

/**
 * Created by hello on 2020/12/29.
 */

public class Util {
    /**
     * 是否有效的网页
     * @param url
     * @return
     */
    public static boolean isVaildUrl(String url){
        return !TextUtils.isEmpty(url) && (url.startsWith("http:")|| url.startsWith("https:") );
    }
}
