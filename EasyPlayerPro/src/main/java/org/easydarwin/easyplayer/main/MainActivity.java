package org.easydarwin.easyplayer.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import com.juntai.wisdom.basecomponent.base.BaseMvpActivity;

import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.base.customview.CustomViewPager;
import org.easydarwin.easyplayer.base.customview.MainPagerAdapter;
import org.easydarwin.easyplayer.main.fragments.filerecord.FileRecordFragment;
import org.easydarwin.easyplayer.main.fragments.HomePageFragment;
import org.easydarwin.easyplayer.main.fragments.MyCenterFragment;

public class MainActivity extends BaseMvpActivity<MainPagePresent> implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MainPageContract.IMainPageView {
    private MainPagerAdapter adapter;
    private LinearLayout mainLayout;
    private CustomViewPager mainViewpager;

    private TabLayout mainTablayout;
    private String[] title = new String[]{"聊天", "通讯录", "我的"};
    private int[] tabDrawables = new int[]{R.drawable.home_index, R.drawable.home_index, R.drawable.home_msg};
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    //


    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mainViewpager = findViewById(R.id.main_viewpager);
        mainTablayout = findViewById(R.id.main_tablayout);
        mainLayout = findViewById(R.id.main_layout);
        mainViewpager.setScanScroll(false);

        initTab();
        initLeftBackTv(false);
        setTitleName("聊天");
        setRightTvDrawable(R.drawable.new_fast_white);
        mImmersionBar.reset().statusBarDarkFont(false).statusBarColor(R.color.colorAccent).init();
    }

    @Override
    public void initData() {
    }


    public void initTab() {
        mFragments.append(0, new HomePageFragment());//
        mFragments.append(1, new FileRecordFragment());//
        mFragments.append(2, new MyCenterFragment());//设置
        //
        mainViewpager.setOffscreenPageLimit(3);
        adapter = new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext(), title, tabDrawables,
                mFragments);
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOffscreenPageLimit(title.length);
        /*viewpager切换监听，包含滑动点击两种*/
        mainViewpager.addOnPageChangeListener(this);
        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab tab = mainTablayout.newTab();
            if (tab != null) {
                if (i == title.length - 1) {
                    tab.setCustomView(adapter.getTabView(i, true));
                } else {
                    tab.setCustomView(adapter.getTabView(i, false));
                }
                mainTablayout.addTab(tab);
            }
        }

        mainTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewpager.setCurrentItem(tab.getPosition(), false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //        mainTablayout.newTab();
        /*viewpager切换默认第一个*/
        mainViewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case 0:
                setRightTvDrawable(R.drawable.new_fast_white);
                setTitleName("聊天");
                break;
            case 1:
                setTitleName("文件");
                getTitleRightTv().setVisibility(View.GONE);
                break;
            case 2:
                setTitleName("设置");
                getTitleRightTv().setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }


    @Override
    protected MainPagePresent createPresenter() {
        return new MainPagePresent();
    }
}
