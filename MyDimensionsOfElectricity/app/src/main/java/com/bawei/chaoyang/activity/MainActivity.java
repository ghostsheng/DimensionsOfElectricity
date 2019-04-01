package com.bawei.chaoyang.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bawei.chaoyang.R;
import com.bawei.chaoyang.base.BaseActivity;
import com.bawei.chaoyang.bean.CreationMsgBean;
import com.bawei.chaoyang.bean.ShowShopMsgBean;
import com.bawei.chaoyang.customview.CustomViewpager;
import com.bawei.chaoyang.fragment.bill.BillFragment;
import com.bawei.chaoyang.fragment.circle.CircleFragment;
import com.bawei.chaoyang.fragment.home.HomeFragment;
import com.bawei.chaoyang.fragment.my.MyFragment;
import com.bawei.chaoyang.fragment.shop.ShopFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_viewpager)
    CustomViewpager mainViewpager;
    @BindView(R.id.main_lin)
    LinearLayout mainLin;
    @BindView(R.id.main_image_bottom)
    ImageView mainImageBottom;
    @BindView(R.id.main_button_home)
    RadioButton mainButtonHome;
    @BindView(R.id.main_button_circle)
    RadioButton mainButtonCircle;
    @BindView(R.id.main_button_shop)
    RadioButton mainButtonShop;
    @BindView(R.id.main_button_bill)
    RadioButton mainButtonBill;
    @BindView(R.id.main_button_my)
    RadioButton mainButtonMy;
    @BindView(R.id.main_buttom_group)
    RadioGroup mainButtomGroup;
    private List<Fragment> list;
    private HomeFragment homeFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        list=new ArrayList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        //添加fragment
        addFragment();

        //viewpager的监听
        setViewpager();

        //radiogroup的监听
        setRadioGroup();
    }
    private void setRadioGroup() {
        mainButtomGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.main_button_home:
                        mainViewpager.setCurrentItem(0);
                        break;
                    case R.id.main_button_circle:
                        mainViewpager.setCurrentItem(1);
                        break;
                    case R.id.main_button_shop:
                        mainViewpager.setCurrentItem(2);
                        break;
                    case R.id.main_button_bill:
                        mainViewpager.setCurrentItem(3);
                        break;
                    case R.id.main_button_my:
                        mainViewpager.setCurrentItem(4);
                        break;
                    default:break;
                }
            }
        });
    }

    private void setViewpager() {
        mainViewpager.addOnPageChangeListener(new CustomViewpager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        mainButtomGroup.check(R.id.main_button_home);
                        EventBus.getDefault().post(new ShowShopMsgBean("moren","select"));
                        break;
                    case 1:
                        mainButtomGroup.check(R.id.main_button_circle);
                        break;
                    case 2:
                        mainButtomGroup.check(R.id.main_button_shop);
                        break;
                    case 3:
                        mainButtomGroup.check(R.id.main_button_bill);
                        break;
                    case 4:
                        mainButtomGroup.check(R.id.main_button_my);
                        break;
                    default:break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void addFragment() {
        homeFragment = new HomeFragment();
        list.add(homeFragment);
        list.add(new CircleFragment());
        list.add(new ShopFragment());
        list.add(new BillFragment());
        list.add(new MyFragment());
        mainViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return list.get(i);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }

    /*
     * 重写onTouchEvent方法监听
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击空白区域系统软键盘消失
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (MainActivity.this.getCurrentFocus() != null) {
                if (MainActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getComid(CreationMsgBean msgBean) {
        if (msgBean.getFlag().equals("send")) {
            mainViewpager.setCurrentItem(3);
            EventBus.getDefault().postSticky("open");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
