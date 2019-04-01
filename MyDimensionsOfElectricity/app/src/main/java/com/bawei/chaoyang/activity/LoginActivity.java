package com.bawei.chaoyang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.chaoyang.R;
import com.bawei.chaoyang.api.ApiService;
import com.bawei.chaoyang.api.Apis;
import com.bawei.chaoyang.base.BaseActivity;
import com.bawei.chaoyang.bean.LoginBean;
import com.bawei.chaoyang.presenter.IpresenterImpl;
import com.bawei.chaoyang.utils.PhoneRegular;
import com.bawei.chaoyang.view.MyView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements MyView  {

    @BindView(R.id.login_edit_phone)
    EditText loginEditPhone;
    @BindView(R.id.login_view_phone)
    View loginViewPhone;
    @BindView(R.id.login_image_phone)
    ImageView loginImagePhone;
    @BindView(R.id.login_edit_pass)
    EditText loginEditPass;
    @BindView(R.id.login_view_pass)
    View loginViewPass;
    @BindView(R.id.login_image_pass_lock)
    ImageView loginImagePassLock;
    @BindView(R.id.login_image_pass_eye)
    ImageView loginImagePassEye;
    @BindView(R.id.login_box_remember)
    CheckBox loginBoxRemember;
    @BindView(R.id.login_text_reg)
    TextView loginTextReg;
    @BindView(R.id.login_button_login)
    Button loginButtonLogin;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    IpresenterImpl ipresenter;
    private LoginBean loginBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }
//      初始化
    @Override
    protected void initView(Bundle savedInstanceState) {
        sp =getSharedPreferences("User",MODE_PRIVATE);
        edit = sp.edit();

        initPresenter();
    }
 //         绑定presenter
        private void initPresenter() {
        ipresenter = new IpresenterImpl(this);
    }

    @Override
    protected void initData() {

        //触摸显示密码
        touchEye();
        //记住密码
        getEdit();
        //登录
        clickMain();
        loginTextReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册界面
                touchRegister();
            }
        });
    }
public LoginBean getLoginBean(){
        return loginBean;
}
    private void touchEye() {
        loginImagePassEye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    loginEditPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    loginEditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });
    }
    private void getEdit() {
        boolean box_ischeck = sp.getBoolean("box_ischeck", false);
        if (box_ischeck){
            String phone = sp.getString("phone", null);
            String pass = sp.getString("pass", null);
            loginEditPhone.setText(phone);
            loginEditPass.setText(pass);
            loginBoxRemember.setChecked(true);
        }
    }
    private void touchRegister() {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void clickMain() {
        loginButtonLogin.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String phone = loginEditPhone.getText().toString();
                String pwd = loginEditPass.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(PhoneRegular.isPhone(phone))){
                    Toast.makeText(LoginActivity.this, "请确认手机号是否正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length()<6){
                    Toast.makeText(LoginActivity.this, "密码不能少于六位", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLoginData(phone,pwd);
            }
        });
    }

    private void getLoginData(String phone, String pwd) {
        Map<String ,String> map=new HashMap<>();
        map.put("phone",phone);
        map.put("pwd",pwd);
        ipresenter.postRequestIpresenter(Apis.LOGIN_URL,map,LoginBean.class);
    }


    @Override
    public void success(Object object) {
//        使用两元操作符进行对比
        if (object instanceof LoginBean){
            loginBean = (LoginBean) object;
            String message = loginBean.getMessage();
            Log.i("xxxxx", "success: "+message);
            if (loginBean.getStatus().equals("0000")){
                LoginBean.ResultBean result = loginBean.getResult();
                edit.putString("userId",result.getUserId()+"");
                edit.putString("sessionId",result.getSessionId());
                edit.putString("nickName",result.getNickName());
                edit.putString("headPic",result.getHeadPic());
                edit.commit();
                EventBus.getDefault().postSticky(result);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "请确认手机号密码是否正确", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {

    }
//        解绑
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ipresenter.deatch();
    }
}
