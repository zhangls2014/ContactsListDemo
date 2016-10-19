package cn.tech.zhangls.mddemo.home.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.tech.zhangls.mddemo.R;
import cn.tech.zhangls.mddemo.home.adapter.FragmentAdapter;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * 权限请求代码
     */
    private static final int PERMISSION_REQUEST_CODE_1 = 10011;
    public static boolean REQUEST_CONTACT_LIST = false;
    /**
     * 保存需要申请的权限的数组
     */
    String[] permissionStrArr;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ac_home_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.ac_home_view_pager);
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(3);//设置缓存的页数
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(String.format(Locale.CHINA, "第%02d页", i));
        }
        TabLayout tableLayout = (TabLayout) findViewById(R.id.ac_home_tab);
        if (viewPager != null) {
            viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this, list));
            if (tableLayout != null) {
                tableLayout.setupWithViewPager(viewPager);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(false);//保存Activity的状态
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.add_contacts:
                //获取权限，读取联系人
                permissionStrArr = new String[1];
                permissionStrArr[0] = "READ_CONTACTS";
                getPermission(permissionStrArr);
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 获取权限
     * @param permission 权限
     */
    protected void getPermission(String[] permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (checkSelfPermission(Manifest.permission.READ_CONTACTS)) {
                case PackageManager.PERMISSION_GRANTED:
                    // 已有授权，读取联系人
                    Toast.makeText(this, "您已经拥有该权限", Toast.LENGTH_SHORT).show();

                    break;
                case PackageManager.PERMISSION_DENIED:
                    // 没有权限：尚未请求过权限，或者请求授权被拒绝，或者曾经授权过，
                    // 但被用户在设置中禁用权限
                    requestPermissions(permission, PERMISSION_REQUEST_CODE_1);
                    break;
                default:
                    // 其实只会返回上述两种情况
                    break;
            }
        } else {
            Toast.makeText(this, "您的手机系统版本过低，不能获取运行时权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_1) {
            if (permissions.length == 1
                    && permissions[0].equals(permissionStrArr[0])
                    && grantResults.length == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权请求被通过，读取通讯录
                    Toast.makeText(this, "您的授权请求已经通过", Toast.LENGTH_SHORT).show();
                } else {
                    // 授权请求被拒绝
                    Toast.makeText(this, "您的授权请求被拒绝，无法读取联系人信息", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 其他情况
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }
}
