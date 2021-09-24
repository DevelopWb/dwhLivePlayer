package org.easydarwin.easyplayer.main.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.juntai.wisdom.basecomponent.base.BaseMvpFragment;
import com.juntai.wisdom.basecomponent.utils.ToastUtils;

import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.main.MainPageContract;
import org.easydarwin.easyplayer.main.MainPagePresent;
import org.easydarwin.easyplayer.util.SPUtil;


/**
 * @aouther tobato
 * @description 描述  homepage
 * @date 2021/4/18 14:59
 */
public class MyCenterFragment extends BaseMvpFragment<MainPagePresent> implements MainPageContract.IMainPageView,
        View.OnClickListener {


    private TextView mRegistCodeValue;
    private EditText mPushServerIpEt;
    private Switch mUdpSwitch;
    private Switch mCodecSwitch;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    protected MainPagePresent createPresenter() {
        return new MainPagePresent();
    }


    @Override
    public void onSuccess(String tag, Object o) {
    }

    @Override
    public void onError(String tag, Object o) {
        ToastUtils.error(mContext, String.valueOf(o));
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.my_center;
    }

    @Override
    protected void initView() {

        mRegistCodeValue = (TextView) getView(R.id.regist_code_value);
        mPushServerIpEt = (EditText) getView(R.id.push_server_ip_et);
        mUdpSwitch = (Switch) getView(R.id.udp_switch);
        mCodecSwitch = (Switch) getView(R.id.codec_switch);

        mUdpSwitch.setChecked(SPUtil.getUDPMode(mContext));
        mCodecSwitch.setChecked(SPUtil.getMediaCodec(mContext));

        mUdpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtil.setUDPMode(mContext, isChecked);
            }
        });

        mCodecSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtil.setMediaCodec(mContext, isChecked);
            }
        });
    }

    @Override
    protected void initData() {

    }

}
