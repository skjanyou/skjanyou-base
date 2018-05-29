package com.skjanyou.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

public interface FileObject extends Serializable {
    // 返回url
    URL getURL();

    // 返回绝对路径
    String getAbsolutePath();

    // 返回路径
    String getPath();

    // 返回文件名
    String getFileName();

    // 返回扩展名
    String getExtName();

    // 返回是否是目录，如果是目录，则getInputStream无效。
    boolean isFolder();

    // 是否是在压缩包内的文件
    boolean isInPackage();

    // 是否存在
    boolean isExist();

    // 返回修改时间
    long getLastModifiedTime();

    // 返回文件大小
    long getSize();

    // 返回输入流
    InputStream getInputStream();

    // 返回输出流
    OutputStream getOutputStream();

    // 返回上级文件
    FileObject getParent();

    // 设置上级文件
    void setParent(FileObject fileObject);

    // 返回下级文件列表
    List<FileObject> getChildren();

    // 获取参数名称指定的fileobject
    FileObject getChild(String fileName);
    
    /**
     * 对FileObject对象执行清理操作，清理完成后，可能会导致此对象不再可用
     */    
    void clean();

    /**
     * 根据路径查找子对象
     *
     * @param path
     * @return
     */
    FileObject getFileObject(String path);    
}
