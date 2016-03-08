package tool.android.giants3.com.linearscrollviewlikeviewpager;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomLinearScrollView scrollView = new CustomLinearScrollView(this);
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            if (i % 2 == 0)
                item.content = i+"测试测试测试---";
            else
                item.content = i+"我的基金极大解放大解放了大空间发垃圾房间的垃圾口附近的垃圾费来得及安福利就大了就发的卡减肥靠大家发电机奥拉夫---";
            items.add(item);
        }

        scrollView.setData(items);
        scrollView.setAnimationParams(3,3,LinearScrollView.ANIM_RIGHT_OUT);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(480, 200);
        lp.topMargin = 400;
        ((ViewGroup) findViewById(R.id.content)).addView(scrollView, lp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
