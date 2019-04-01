package com.bawei.chaoyang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.chaoyang.R;
import com.bawei.chaoyang.api.Apis;
import com.bawei.chaoyang.bean.RegisterBean;
import com.bawei.chaoyang.presenter.Ipresenter;
import com.bawei.chaoyang.presenter.IpresenterImpl;
import com.bawei.chaoyang.utils.PhoneRegular;
import com.bawei.chaoyang.view.MyView;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements MyView {

    @BindView(R.id.reg_edit_phone)
    XEditText regEditPhone;
    @BindView(R.id.reg_image_phone)
    ImageView regImagePhone;
    @BindView(R.id.reg_edit_pass)
    EditText regEditPass;
    @BindView(R.id.reg_image_pass_lock)
    ImageView regImagePassLock;
    @BindView(R.id.reg_image_pass_eye)
    ImageView regImagePassEye;
    @BindView(R.id.reg_text_login)
    TextView regTextLogin;
    @BindView(R.id.reg_button_login)
    Button regButtonLogin;
    private Ipresenter ipresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initPresenter();
    }

    private void initPresenter() {
        ipresenter = new IpresenterImpl(this);
    }

    @OnClick({R.id.reg_image_pass_eye, R.id.reg_button_login, R.id.reg_text_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_image_pass_eye:
                regImagePassEye.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN){
                            regEditPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }else if (event.getAction()==MotionEvent.ACTION_UP){
                            regEditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        return true;
                    }
                });
                break;
            case R.id.reg_button_login:
                String phone = regEditPhone.getText().toString();
                String pwd = regEditPass.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.isEmpty()){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(PhoneRegular.isPhone(phone))){
                    Toast.makeText(this, "请确定手机号格式是否正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length()<6){
                    Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                getRegisterData(phone,pwd);
                break;
            case R.id.reg_text_login:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getRegisterData(String phone, String pwd) {
        Map<String , String> map=new HashMap<>();
        map.put("phone",phone);
        map.put("pwd",pwd);
        ipresenter.postRequestIpresenter(Apis.REGISTER_URL,map,RegisterBean.class);
    }

    @Override
    public void success(Object object) {

//            使用两元运算符进行判断
        if (object instanceof RegisterBean){
            RegisterBean registerBean= (RegisterBean) object;
            if (registerBean.getStatus().equals("0000")){
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "恭喜您注册成功，登录开始您的购买之旅", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, registerBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {

    }
}
