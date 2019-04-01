package com.bawei.chaoyang.fragment.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bawei.chaoyang.R;
import com.bawei.chaoyang.activity.OrderActivity;
import com.bawei.chaoyang.adapter.ShopCarAdater;
import com.bawei.chaoyang.api.Apis;
import com.bawei.chaoyang.base.BaseFragment;
import com.bawei.chaoyang.bean.AddShopBean;
import com.bawei.chaoyang.bean.SelectShopBean;
import com.bawei.chaoyang.bean.ShopSelectListBean;
import com.bawei.chaoyang.presenter.IpresenterImpl;
import com.bawei.chaoyang.view.MyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShopFragment extends BaseFragment implements MyView {


    Unbinder unbinder;
    @BindView(R.id.shop_recy)
    RecyclerView shopRecy;
    @BindView(R.id.shop_view_bottom)
    View shopViewBottom;
    @BindView(R.id.shop_text_bottom)
    TextView shopTextBottom;
    @BindView(R.id.shop_box_all)
    CheckBox shopBoxAll;
    @BindView(R.id.shop_text_all)
    TextView shopTextAll;
    @BindView(R.id.shop_text_allpr)
    TextView shopTextAllpr;
    @BindView(R.id.shop_text_allprice)
    TextView shopTextAllprice;
    @BindView(R.id.shop_text_go)
    TextView shopTextGo;
    private IpresenterImpl ipresenter;
    private ShopCarAdater adater;

    @Override
    protected int getlayoutResId() {
        return R.layout.shop_fragment;
    }

    @Override
    protected void initView(View view) {
        shopRecy.setLayoutManager(new LinearLayoutManager(getActivity()));
        ipresenter = new IpresenterImpl(this);
        ipresenter.getRequestIpresenter(Apis.SHOW_SELECT_SHOP_URL, SelectShopBean.class);
    }

    @Override
    protected void initData() {

    }



    @Override
    public void success(Object object) {
//        使用两元进行判断
        if (object instanceof SelectShopBean) {
            SelectShopBean selectShopBean = (SelectShopBean) object;
            List<SelectShopBean.ResultBean> result = selectShopBean.getResult();
            Log.i("xxxx", "success: " + result);
            adater = new ShopCarAdater(getActivity(), result);
            shopRecy.setAdapter(adater);
            //结算总价格,数量
            getpriceCount();
            shopBoxAll.setChecked(false);
            //点击复选框进行判断
            boxOnclick();
            shopTextGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),OrderActivity.class);
                    startActivity(intent);
                }
            });
        }


    }
    private void boxOnclick() {
        shopBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断全选时商品的状态
                checkAll(shopBoxAll.isChecked());
                adater.notifyDataSetChanged();
            }
        });
    }
    List<SelectShopBean.ResultBean> shop_list=new ArrayList<>();
    private void checkAll(boolean checked) {
        double totalPrice=0;
        int num=0;

        for (int i=0;i<shop_list.size();i++){
            //遍历商品，改变状态
            shop_list.get(i).setIscheck(checked);
            totalPrice=totalPrice+(shop_list.get(i).getPrice()*shop_list.get(i).getCount());
            num=num+shop_list.get(i).getCount();
        }

        if (checked){
            shopTextAllprice.setText(""+totalPrice);
            shopTextGo.setText("去结算("+num+")");
        }else{
            shopTextAllprice.setText("0");
            shopTextGo.setText("去结算");
        }
    }


    private void getpriceCount() {
        adater.setOnClick(new ShopCarAdater.ShopClick() {
            @Override
            public void shopPrice(List<SelectShopBean.ResultBean> list) {
                //在这里重新遍历已经改变状态后的数据
                //这里不能break跳出，因为还有需要计算后面点击商品的价格和数量，所以必须跑完整个循环
                double totalPrice=0;
                //勾选商品的数量，不是该商品购买的数量
                int num=0;
                //所有商品总数，和上面的数量做比对，如果两者相等，则说明全选
                int totalNum=0;
                for (int i=0;i<list.size();i++){
                    totalNum=totalNum+list.get(i).getCount();
                    if (list.get(i).isIscheck()){
                        totalPrice=totalPrice+list.get(i).getPrice()*list.get(i).getCount();
                        num=num+list.get(i).getCount();
                    }

                }
                if (num<totalNum){
                    shopBoxAll.setChecked(false);
                }else{
                    shopBoxAll.setChecked(true);
                }
                shopTextAllprice.setText(""+totalPrice);
                shopTextGo.setText("去结算("+num+")");
                //添加购物车的集合
                List<ShopSelectListBean> addlist=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    int commodityId = list.get(i).getCommodityId();
                    int count = list.get(i).getCount();
                    addlist.add(new ShopSelectListBean(Integer.valueOf(commodityId),count));
                }
                String data="[";
                for (ShopSelectListBean bean : addlist){
                    data+="{\"commodityId\":"+bean.getCommodityId()+",\"count\":"+bean.getCount()+"},";
                }
                String substring = data.substring(0, data.length() - 1);
                substring+="]";
                Map<String,String> params = new HashMap<>();
                params.put("data",substring);

                ipresenter.putRequestIpresenter(Apis.SHOW_ADD_SHOP_URL,params,AddShopBean.class);
                if (list.size()==0){
                    shopBoxAll.setChecked(false);
                }
            }
        });
        adater.setRemove(new ShopCarAdater.RemoveCallBack() {
            @Override
            public void removeposition(List<SelectShopBean.ResultBean> list, int position) {
                //在这里重新遍历已经改变状态后的数据
                //这里不能break跳出，因为还有需要计算后面点击商品的价格和数量，所以必须跑完整个循环
                double totalPrice=0;
                //勾选商品的数量，不是该商品购买的数量
                int num=0;
                //所有商品总数，和上面的数量做比对，如果两者相等，则说明全选
                int totalNum=0;
                for (int i=0;i<list.size();i++){
                    totalNum=totalNum+list.get(i).getCount();
                    if (list.get(i).isIscheck()){
                        totalPrice=totalPrice+list.get(i).getPrice()*list.get(i).getCount();
                        num=num+list.get(i).getCount();
                    }

                }
                if (num<totalNum){
                    shopBoxAll.setChecked(false);
                }else{
                    shopBoxAll.setChecked(true);
                }
                shop_list.remove(position);
                shopTextAllprice.setText(""+totalPrice);
                shopTextGo.setText("去结算("+num+")");
                //添加购物车的集合
                List<ShopSelectListBean> addlist=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    int commodityId = list.get(i).getCommodityId();
                    int count = list.get(i).getCount();
                    addlist.add(new ShopSelectListBean(Integer.valueOf(commodityId),count));
                }
                String data="[";
                for (ShopSelectListBean bean : addlist){
                    data+="{\"commodityId\":"+bean.getCommodityId()+",\"count\":"+bean.getCount()+"},";
                }
                String substring = data.substring(0, data.length() - 1);
                substring+="]";
                Map<String,String> params = new HashMap<>();
                params.put("data",substring);

                ipresenter.putRequestIpresenter(Apis.SHOW_ADD_SHOP_URL,params,AddShopBean.class);
                if (list.size()==0){
                    shopBoxAll.setChecked(false);
                }
            }
        });
    }
    @Override
    public void failure(String error) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
