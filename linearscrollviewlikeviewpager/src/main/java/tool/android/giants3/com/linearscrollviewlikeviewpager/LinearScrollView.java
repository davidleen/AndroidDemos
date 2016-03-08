package tool.android.giants3.com.linearscrollviewlikeviewpager;

import com.nineoldandroids.animation.Animator;

import com.nineoldandroids.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;


/**
 * 横向轮播控件，
 *简单的轮播控件 类似 viewpager  轻量级
 * @param <T>
 * @author davidleen29
 *         <p/>
 *         使用nineoldandroids 动画库 原生的viewAnimation 太卡，valueAnimation 不兼容低版本。
 */
@SuppressLint("NewApi")
public abstract class LinearScrollView<T> extends FrameLayout {

    public List<T> items;
    LayoutInflater layoutInflator;
    View[] scrollView = new View[2];


    public int currentIndex;
    public static final int DEFAULT_ANIMATE_TIME = 3;
    /**
     * 动画间隔时间 单位秒
     */
    public int mAnimateTimer = DEFAULT_ANIMATE_TIME;

    private static final String TAG = "LinearScrollView";

    //动画0   即是当前显示的view的动画。
    ValueAnimator outAnimator;

    //动画1  即将要显示的动画
    ValueAnimator inAnimator;

    public static final int MASK_SIZE = 8;
    public static final int MASK = 0x00001111;
    public static final int ANIM_LEFT_OUT = 1 << MASK_SIZE;
    public static final int ANIM_RIGHT_OUT = 2 << MASK_SIZE;
    public static final int ANIM_TOP_OUT = 3;
    public static final int ANIM_BOTTOM_OUT = 4;

    /**
     * 动画类型
     */
    public int animationType = ANIM_LEFT_OUT;

    public LinearScrollView(Context context) {
        super(context);
        // final int width = context.getResources().getDisplayMetrics().widthPixels;
        layoutInflator = LayoutInflater.from(context);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        scrollView[0] = layoutInflator.inflate(getChildLayout(), null);
        scrollView[1] = layoutInflator.inflate(getChildLayout(), null);
        addView(scrollView[1], lp);
        addView(scrollView[0], lp);
        scrollView[0].bringToFront();



        setCompatX(scrollView[1], getWidth());

        //set a default; as left out;
        outAnimator = ValueAnimator.ofFloat(0, -getWidth());
        //set a default; as right in;
        inAnimator = ValueAnimator.ofFloat(-getWidth(), 0);

        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animationType >> MASK_SIZE > 0) {
                    setCompatX(scrollView[0], (Float) animation.getAnimatedValue());
                } else {
                    setCompatY(scrollView[0], (Float) animation.getAnimatedValue());
                }

            }
        };
        outAnimator.addUpdateListener(listener);

        ValueAnimator.AnimatorUpdateListener rightInListener = new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


               if (animationType >> MASK_SIZE > 0) {
                    setCompatX(scrollView[1], (Float) animation.getAnimatedValue());
                } else {
                    setCompatY(scrollView[1], (Float) animation.getAnimatedValue());
                }


            }
        };
        inAnimator.addUpdateListener(rightInListener);

        outAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                int nextIndex = (currentIndex + 1) % items.size();
                applyItem(items.get(nextIndex), scrollView[0]);

                scrollView[0].measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
                // 控件置换 每次动画结束后， 当前为【1】   【0】已经切出屏幕了，所以要做置换。
                View v = scrollView[0];
                scrollView[0] = scrollView[1];
                scrollView[1] = v;
                postDelayed(doScroll, mAnimateTimer * 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                scrollView[0].layout(0, 0, getWidth(), getHeight());
                scrollView[1].layout(-getWidth(), -getHeight(), 0, 0);
            }
        });

        // AccelerateDecelerateInterpolator linearInterpolator= new
        // AccelerateDecelerateInterpolator();
        // outAnimator.setInterpolator(linearInterpolator);
        // inAnimator.setInterpolator(linearInterpolator);
    }

    /**
     * 设置x值 需要做版本适应
     */

    private void setCompatX(View v, float x) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            LayoutParams lp = (LayoutParams) v
                    .getLayoutParams();
            lp.leftMargin = (int) x;
            v.setLayoutParams(lp);

        } else {

            v.setX(x);

        }

    }

    /**
     * 设置y值 需要做版本适应
     */

    private void setCompatY(View v, float y) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            LayoutParams lp = (LayoutParams) v
                    .getLayoutParams();
            lp.topMargin = (int) y;
            v.setLayoutParams(lp);

        } else {

            v.setY(y);

        }

    }

    /**
     * 设置xy值 需要做版本适应
     */

    private void setCompatXY(View v, float x, float y) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            LayoutParams lp = (LayoutParams) v
                    .getLayoutParams();
            lp.topMargin = (int) y;
            lp.leftMargin = (int) x;
            v.setLayoutParams(lp);

        } else {
            v.setX(x);
            v.setY(y);

        }

    }

    /**
     * 设置动画参数
     *
     * @param mAnimateTimer
     * @param mAnimateOneScreenTimer
     * @param mAnimateType           这个参数目前未用， 待扩展
     */
    public void setAnimationParams(int mAnimateTimer,
                                   int mAnimateOneScreenTimer, int mAnimateType) {

        inAnimator.setDuration(mAnimateOneScreenTimer * 1000);
        outAnimator.setDuration(mAnimateOneScreenTimer * 1000);
        this.mAnimateTimer = mAnimateTimer;
        if (animationType != mAnimateType) {
            this.animationType = mAnimateType;
            updateAnimateType(getWidth(), getHeight());
        }
    }

    public void nextAnimation() {

        currentIndex++;
        if (currentIndex >= items.size()) {
            currentIndex = 0;
        }
        starScroll();
    }


    /**
     * 设置数据
     *
     * @param datas
     */
    public void setData(List<T> datas) {
        stopScroll();
        this.items = datas;
        currentIndex = 0;
        prepare();
        stopScroll();
        postDelayed(doScroll, mAnimateTimer * 1000);

    }


    private void starScroll() {
        if (items == null || items.size() > 1)
            outAnimator.start();
        inAnimator.start();

    }

    /**
     * 滚动执行代码
     */
    private Runnable doScroll = new Runnable() {

        @Override
        public void run() {

            if (items == null) return;
            if (items.size() > 1 && currentIndex < items.size())
                nextAnimation();
        }
    };

    /**
     * 数据准备
     */
    private void prepare() {
        if (items != null && items.size() > 0 && currentIndex >= 0 && currentIndex < items.size()) {

            applyItem(items.get(currentIndex), scrollView[0]);
            if (items.size() > 1)
                applyItem(items.get(currentIndex + 1), scrollView[1]);
            scrollView[0].bringToFront();
        }

    }

    private void stopScroll() {
        removeCallbacks(doScroll);
        outAnimator.cancel();
        inAnimator.cancel();

    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //here  reset animation range;

        updateAnimateType(w, h);


    }

    /**
     * 更新动画类型
     */
    private void updateAnimateType(int width, int height) {


        switch (animationType) {
            case ANIM_LEFT_OUT:
                inAnimator.setFloatValues(width, 0);
                outAnimator.setFloatValues(0, -width);
                break;
            case ANIM_RIGHT_OUT:
                inAnimator.setFloatValues(-width, 0);
                outAnimator.setFloatValues(0, width);
                break;
            case ANIM_TOP_OUT:
                inAnimator.setFloatValues(height, 0);
                outAnimator.setFloatValues(0, -height);
                break;
            case ANIM_BOTTOM_OUT:
                inAnimator.setFloatValues(-height, 0);
                outAnimator.setFloatValues(0, height);
                break;

        }

    }

    /**
     * 数据绑定
     *
     * @param item
     * @param layout
     */
    public abstract void applyItem(T item, View layout);

    public abstract int getChildLayout();

    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();
        removeCallbacks(doScroll);
        postDelayed(doScroll, mAnimateTimer * 1000);

    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        stopScroll();

    }

    public void doMeasure() {
        if (scrollView[0] != null) {
            scrollView[0].measure(MeasureSpec.makeMeasureSpec(720, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY));

        }
        if (scrollView[1] != null) {
            scrollView[1].measure(MeasureSpec.makeMeasureSpec(720, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY));

        }
    }




}
