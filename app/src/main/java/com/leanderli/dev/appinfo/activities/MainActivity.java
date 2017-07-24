package com.leanderli.dev.appinfo.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.leanderli.dev.appinfo.R;
import com.leanderli.dev.appinfo.adapter.AppInfosAdapter;
import com.leanderli.dev.appinfo.common.ParamterConstant;
import com.leanderli.dev.appinfo.model.bean.AppInfo;
import com.leanderli.dev.appinfo.utils.AppFilterXmlUtil;
import com.leanderli.dev.appinfo.utils.SDCardUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_APPLY_ICON_THEME = "com.teslacoilsw.launcher.APPLY_ICON_THEME";
    private static final String NOVA_PACKAGE = "com.teslacoilsw.launcher";
    private static final String EXTRA_ICON_THEME_PACKAGE = "com.teslacoilsw.launcher.extra.ICON_THEME_PACKAGE";

    private ListView appInfoListView = null;
    private List<AppInfo> appInfos = null;
    private AppInfosAdapter infosAdapter = null;
    private TextView installAppCounts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appInfoListView = (ListView) this.findViewById(R.id.appinfo_list);
        installAppCounts = (TextView) this.findViewById(R.id.tv_install_app_counts);
        init();
    }

    private void init() {
        appInfos = getAppInfos();
        if (null != appInfos && 0 != appInfos.size()) {
            updateUI(appInfos);
        }
    }

    public void updateUI(List<AppInfo> appInfos) {
        infosAdapter = new AppInfosAdapter(getApplication(), appInfos);
        installAppCounts.setText(appInfos.size() + "个");
        appInfoListView.setAdapter(infosAdapter);
    }

    public List<AppInfo> getAppInfos() {
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(packageManager));
//        if (appInfos != null) {
//            appInfos.clear();
//            for (ResolveInfo reInfo : resolveInfoList) {
//                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
//                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
//                String appLabel = (String) reInfo.loadLabel(packageManager); // 获得应用程序的Label
//                Drawable icon = reInfo.loadIcon(packageManager); // 获得应用程序图标
//                // 为应用程序的启动Activity 准备Intent
//                Intent launchIntent = new Intent();
//                launchIntent.setComponent(new ComponentName(pkgName,
//                        activityName));
//                // 创建一个AppInfo对象，并赋值
//                AppInfo appInfo = new AppInfo();
//                appInfo.setAppName(appLabel);
//                appInfo.setPackageName(pkgName);
//                appInfo.setDrawable(icon);
//                appInfo.setAppIntent(launchIntent);
//                appInfos.add(appInfo); // 添加至列表中
//            }
//        }

//        PackageManager pm = getApplication().getPackageManager();
//        List<PackageInfo>  packgeInfos = pm.getInstalledPackages(0);
//        appInfos = new ArrayList<AppInfo>();
//        /* 获取应用程序的名称，不是包名，而是清单文件中的labelname
//            String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
//            appInfo.setAppName(str_name);
//         */
//        for(PackageInfo packgeInfo : packgeInfos){
//            String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
//            String packageName = packgeInfo.packageName;
//            Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
//            AppInfo appInfo = new AppInfo(appName, packageName,drawable);
//            appInfos.add(appInfo);
//        }
//        return appInfos;

        PackageManager packageManager = getApplication().getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        appInfos = new ArrayList<AppInfo>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = packageInfoList.get(i);
            ApplicationInfo applicationInfo = pak.applicationInfo;
            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 第三方应用
                String appName = pak.applicationInfo.loadLabel(packageManager).toString();
                String packageName = pak.packageName;
                String appIntent = applicationInfo.name;
                Drawable drawable = pak.applicationInfo.loadIcon(packageManager);
                AppInfo appInfo = new AppInfo(appName, packageName, drawable, appIntent);
                appInfos.add(appInfo);
            }
        }

        return appInfos;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.abouts) {
            Toast.makeText(this, "关于软件", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.exportData) {
            if (null == appInfos) {
                init();
            } else {
                exportTemplate();
            }
        } else if (id == R.id.apply_app_icon) {
            applyAppIconToLauncher();
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyAppIconToLauncher() {
        Intent intent = new Intent(ACTION_APPLY_ICON_THEME);
        intent.setPackage(NOVA_PACKAGE);
        intent.putExtra(EXTRA_ICON_THEME_PACKAGE, getPackageName());
        startActivity(intent);
    }

    private void exportTemplate() {
        SDCardUtil sdCardUtil = new SDCardUtil();
        AppFilterXmlUtil appFilterXmlUtil = new AppFilterXmlUtil();
        String appRootPath = sdCardUtil.createAppFolder();
        File templateDirPath = new File(appRootPath + File.separator + ParamterConstant.SECONDARY_FOLDER_NAME);
        if (!templateDirPath.exists() && !templateDirPath.isDirectory()) {
            templateDirPath.mkdirs();
        }
        File templateFile = new File(templateDirPath + File.separator + ParamterConstant.TEMPLATE_FILE_NAME);
        if (!templateFile.exists() && !templateFile.isFile()) {
            try {
                templateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        appFilterXmlUtil.CreateXMLByDOM4J(templateFile, appInfos);
    }
}
