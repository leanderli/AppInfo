package com.leanderli.dev.appinfo.utils;

import android.util.Log;
import com.leanderli.dev.appinfo.model.bean.AppInfo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */
public class AppFilterXmlUtil {

    // <item component="ComponentInfo{com.android.chrome/com.google.android.apps.chrome.Main}" drawable="chrome" />
    // item.addAttribute("component", "ComponentInfo{" + appInfoList.get(i).getPackageName() + "}");
    // item.addAttribute("drawable", appInfoList.get(i).getPackageName().substring(appInfoList.get(i).getPackageName().lastIndexOf(".") + 1));

    public void CreateXMLByDOM4J(File dest, List<AppInfo> appInfoList) {
        // 创建Document对象
        Document document = DocumentHelper.createDocument();
        // 创建根节点
        Element rss = document.addElement("resources");
        // 创建item子节点
        Element item = null;
        for (int i = 0; i < appInfoList.size(); i++) {
            item = rss.addElement("item");
            item.addAttribute("component", "ComponentInfo{" + appInfoList.get(i).getPackageName() + "/" + appInfoList.get(i).getAppIntent() + "}");
            item.addAttribute("drawable", "huaji");
        }
        // 创建输出格式(OutputFormat对象)
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置输出文件的编码
        format.setEncoding("UTF-8");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dest);
            // 创建XMLWriter对象
            XMLWriter writer = new XMLWriter(fileOutputStream, format);
            //设置不自动进行转义
            writer.setEscapeText(false);
            // 生成XML文件
            writer.write(document);
            //关闭XMLWriter对象
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
