package tool.android.giants3.com.linearscrollviewlikeviewpager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 横屏滚动
 *
 * @author davidleen29
 *
 */
public class CustomLinearScrollView extends
        LinearScrollView<Item> {


    public CustomLinearScrollView(Context context
                                  ) {
        super(context);

    }

    @Override
    public void applyItem(Item item, View layout) {
        GiftViewHolder holder;
        if (layout.getTag() == null) {
            holder = new GiftViewHolder();
            holder.convertView = layout;
            holder.user_head = (ImageView) layout
                    .findViewById(R.id.user_head);
            holder.username = (TextView) layout
                    .findViewById(R.id.user_name);

            holder.user_gift = (TextView) layout
                    .findViewById(R.id.user_gift);

            holder.img_gift = (ImageView) layout
                    .findViewById(R.id.img_gift);

            layout.setTag(holder);
        } else {
            holder = (GiftViewHolder) layout.getTag();
        }

        bindData(holder, item);
    }

    @Override
    public int getChildLayout() {

        return R.layout.style_gift_form;
    }

    private class GiftViewHolder {
        ImageView user_head;
        TextView username;
        TextView user_gift;
        ImageView img_gift;
        View convertView;

    }

    private void bindData(GiftViewHolder holder,
                          Item data) {


        holder.username.setText(data.left);
        holder.username
                .setVisibility(TextUtils.isEmpty(data.left) ? View.GONE
                        : View.VISIBLE);

        holder.user_gift.setText(data.content);
        holder.user_gift
                .setVisibility(TextUtils.isEmpty(data.content) ? View.GONE
                        : View.VISIBLE);
        holder.img_gift.setVisibility(View.VISIBLE);




    }
}