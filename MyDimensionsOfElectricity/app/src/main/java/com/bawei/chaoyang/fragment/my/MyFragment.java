package com.bawei.chaoyang.fragment.my;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.chaoyang.R;
import com.bawei.chaoyang.activity.LoginActivity;
import com.bawei.chaoyang.base.BaseFragment;
import com.bawei.chaoyang.bean.LoginBean;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFragment extends BaseFragment {

    @BindView(R.id.my_image_header)
    SimpleDraweeView myImageHeader;
    @BindView(R.id.my_text_name)
    TextView myTextName;
    @BindView(R.id.my_image_personal)
    ImageView myImagePersonal;
    @BindView(R.id.my_view_personal)
    View myViewPersonal;
    @BindView(R.id.my_text_personal)
    TextView myTextPersonal;
    @BindView(R.id.my_image_circle)
    ImageView myImageCircle;
    @BindView(R.id.my_view_circle)
    View myViewCircle;
    @BindView(R.id.my_text_circle)
    TextView myTextCircle;
    @BindView(R.id.my_image_foot)
    ImageView myImageFoot;
    @BindView(R.id.my_view_foot)
    View myViewFoot;
    @BindView(R.id.my_text_foot)
    TextView myTextFoot;
    @BindView(R.id.my_image_wallet)
    ImageView myImageWallet;
    @BindView(R.id.my_view_wallet)
    View myViewWallet;
    @BindView(R.id.my_text_wallet)
    TextView myTextWallet;
    @BindView(R.id.my_image_site)
    ImageView myImageSite;
    @BindView(R.id.my_view_site)
    View myViewSite;
    @BindView(R.id.my_text_address)
    TextView myTextAddress;
    Unbinder unbinder;

    protected int getlayoutResId() {
        return R.layout.my_fragment;
    }

    @Override
    protected void initView(View view) {
      //  EventBus.getDefault().register(this);
    }


    @Override
    protected void initData() {
        myTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEventData(LoginBean loginBean) {
      //  LoginBean.ResultBean result = loginBean.getResult();
        //String nickName = result.getNickName();
        //Toast.makeText(getActivity(), nickName, Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences user = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String nickName = user.getString("nickName", "登录/注册");
        String headPic = user.getString("headPic", "");
        myTextName.setText(nickName);
        myImageHeader.setImageURI(headPic);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
     //   EventBus.getDefault().unregister(this);
    }
}
