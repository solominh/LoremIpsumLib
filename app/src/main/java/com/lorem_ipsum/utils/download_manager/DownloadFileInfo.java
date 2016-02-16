package com.lorem_ipsum.utils.download_manager;

import java.io.Serializable;

/**
 * Created by hoangminh on 2/3/16.
 */
public class DownloadFileInfo implements Serializable {
    public String appName;
    public String title;
    public String url;
    public String filePath;

    public DownloadFileInfo(String appName, String title, String url, String filePath) {
        this.appName = appName;
        this.title = title;
        this.url = url;
        this.filePath = filePath;
    }
}
