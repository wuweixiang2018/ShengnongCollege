package com.education.shengnongcollege.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuweixiang on 18/6/3.
 */

public class BaseUtil {
    public static String UserId = "108fddbd-cc72-4471-81b9-7391d61943e0";
    public static int Online = 0;//登录状态
    /**
     * 国家号码段分配如下：
     *
     * 　　 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     *
     * 　 　联通：130、131、132、152、155、156、185、186
     *
     * 　 　电信：133、153、180、189、（1349卫通）
     *
     * 此方法描述的是：验证是否为手机号码
     *
     * @author:Auser
     * @since: 2013-10-26 下午6:26:50
     * @param mobiles
     * @return
     * @return boolean
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
