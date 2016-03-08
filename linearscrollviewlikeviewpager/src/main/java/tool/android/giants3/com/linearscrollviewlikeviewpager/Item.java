package tool.android.giants3.com.linearscrollviewlikeviewpager;

/**
 * Created by HP on 2016/2/29.
 */

public   class Item {
    // 唯一标记
    public String iD;
    // 左标题前的图标 系统约定的所有ICON需要支持图文
    public String leftIcon;
    // 左边文字标题 支持HTML
    public String left;
    // 左边文字点击
    public String leftHref;
    // 右标题前的图标 系统约定的所有ICON需要支持图文
    public String rightIcon;
    // 内容
    public String content;
    // 滚动时间间隔【V5.2】 以秒为单位
    public int animateTimer;
    // 滚完一屏所需要的时间【V5.2】 以秒为单位
    public int animateOneScreenTimer;
    // 走马灯滚动方式【V5.2】
    public int animateType;
    // 内容点击
    public String contentHref;
}