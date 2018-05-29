package com.skjanyou.vfs;

public interface FileSystemProvider {
    /**
     * 是否匹配
     *
     * @param resource
     * @return 如果返回true，表示此提供者可以处理，返回false表示不能处理
     */
    boolean isMatch(String resource);

    /**
     * 返回处理的模式
     *
     * @return
     */
    String getSchema();

    /**
     * 解析资源，并返回文件对象
     *
     * @param resource
     * @return
     */
    FileObject resolver(String resource);
}
