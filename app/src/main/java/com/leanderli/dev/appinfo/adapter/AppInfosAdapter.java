package com.leanderli.dev.appinfo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.leanderli.dev.appinfo.R;
import com.leanderli.dev.appinfo.model.bean.AppInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/6/24.
 */
public class AppInfosAdapter extends BaseAdapter {

    Context context;
    List<AppInfo> appInfos;

    public AppInfosAdapter() {
    }

    public AppInfosAdapter(Context context, List<AppInfo> infos) {
        this.context = context;
        this.appInfos = infos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<AppInfo> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != appInfos) {
            return appInfos.size();
        }
        return count;
    }

    @Override
    public Object getItem(int index) {
        return appInfos.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.app_info_item, null);
            viewHolder.appIconImg = (ImageView) convertView.findViewById(R.id.app_icon);
            viewHolder.appNameText = (TextView) convertView.findViewById(R.id.app_info_name);
            viewHolder.appPackageText = (TextView) convertView.findViewById(R.id.app_info_package_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null != appInfos) {
            viewHolder.appIconImg.setBackground(appInfos.get(position).getDrawable());
            viewHolder.appNameText.setText(appInfos.get(position).getAppName());
            viewHolder.appPackageText.setText(appInfos.get(position).getPackageName());
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView appIconImg;
        TextView appNameText;
        TextView appPackageText;
    }
}
