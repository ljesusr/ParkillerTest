package test.parker.com.parkiller;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tools.AndroidFuntions;
import tools.GpsFuntions;

public class HowItWorkActivity extends Activity {


    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<Integer> listOfItems;
    private ArrayList<Integer> listDeImagenes;
    private GpsFuntions gpsFuntions;
    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;

    //tools class
    AndroidFuntions androidFuntions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_it_work);

        initViews();
        setViewPagerItemsWithAdapter();
        setUiPageViewController();
    }


    public void enterToMapActivity(View v){
        if(!gpsFuntions.checkGpsStatus()){
            gpsFuntions.showGpsAlert();
        }else {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void setUiPageViewController() {
        dotsLayout = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        dotsCount = myViewPagerAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }

        dots[0].setTextColor(getResources().getColor(R.color.main_color));
    }

    private void setViewPagerItemsWithAdapter() {
        myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    //	page change listener
    OnPageChangeListener viewPagerPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            dots[position].setTextColor(getResources().getColor(R.color.main_color));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }};

    private void initViews() {
        androidFuntions= new AndroidFuntions(this);
        gpsFuntions= new GpsFuntions(this);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        listOfItems = new ArrayList<Integer>();
        listOfItems.add(1);
        listOfItems.add(2);
        listOfItems.add(3);
        listOfItems.add(4);

        listDeImagenes = new ArrayList<Integer>();
        listDeImagenes.add(R.drawable.tuto1);
        listDeImagenes.add(R.drawable.tuto2);
        listDeImagenes.add(R.drawable.tuto3);
        listDeImagenes.add(R.drawable.tuto4);

    }

    @Override
    public void onBackPressed(){
    androidFuntions.exitApp();
    }
    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter{
        private LayoutInflater layoutInflater;
        private ArrayList<Integer> items;

        public MyViewPagerAdapter(ArrayList<Integer> listOfItems) {
            this.items = listOfItems;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_pager_item, container,false);
             ImageView ivTutorial= (ImageView)view.findViewById(R.id.iv_tutorial);
            ivTutorial.setImageResource(listDeImagenes.get(position));
                    ((ViewPager) container).addView(view);


                return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View)obj);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View)object;
            ((ViewPager) container).removeView(view);
        }

    }

}