package org.easydarwin.easyplayer.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.regmode.RegLatestContact;
import com.regmode.Utils.RegOperateManager;

import org.easydarwin.easyplayer.PlayListActivity;
import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.util.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  导航页面
 * @date 2021/7/4 11:19
 */
public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerview;
    private LinearLayout mMenuQuitLl;
    private MyMenuAdapter mMenuAdapter;
    public static final int NAME_CAMERA = 0;
    public static final String APP_PACKAGE_NAME = "MonitorAB.Android";
    public static final int NAME_GLASSES = 1;//眼镜
    private long mExitTime;//声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        RegOperateManager.getInstance(this).setCancelCallBack(new RegLatestContact.CancelCallBack() {
            @Override
            public void toFinishActivity() {
                finish();
            }

            @Override
            public void toDoNext() {

            }
        });
        initView();
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void initView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mMenuQuitLl = (LinearLayout) findViewById(R.id.menu_quit_ll);
        mMenuQuitLl.setOnClickListener(this);
        mMenuAdapter = new MyMenuAdapter(R.layout.item_my_center_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mRecyclerview.addItemDecoration(new GridDividerItemDecoration(this));
        mRecyclerview.setAdapter(mMenuAdapter);
        mMenuAdapter.setNewData(getMineMenus());
        mMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        launchapp(NavigationActivity.this);
                        break;
                    case 1:
                        startActivity(new Intent(NavigationActivity.this, PlayListActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void onBackPressed() {
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //大于2000ms则认为是误操作，使用Toast进行提示
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            //并记录下本次点击“返回键”的时刻，以便下次进行判断
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @return
     */
    protected List<MenuBean> getMineMenus() {
        List<MenuBean> arrays = new ArrayList<>();
        arrays.add(new MenuBean(NAME_CAMERA, "摄像机", R.mipmap.camera_icon));
        arrays.add(new MenuBean(NAME_GLASSES, "眼镜", R.mipmap.glasses_icon));
        return arrays;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.menu_quit_ll:
                finish();
                break;
        }
    }

    //跳转页面的方法
    private void launchapp(Context context) {
        //判断当前手机是否有要跳入的app
        if (isAppInstalled(context, APP_PACKAGE_NAME)) {
            //如果有根据包名跳转
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE_NAME));
        } else {
            //如果没有，走进入系统商店找到这款APP，提示你去下载这款APP的程序
            Toast.makeText(this, "您还没有安装AB软件", Toast.LENGTH_SHORT).show();
        }
    }

    //这里是判断APP中是否有相应APP的方法
    private boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
