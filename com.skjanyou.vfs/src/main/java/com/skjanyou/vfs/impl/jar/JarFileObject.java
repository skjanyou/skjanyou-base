package com.skjanyou.vfs.impl.jar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import com.skjanyou.vfs.FileObject;
import com.skjanyou.vfs.util.StreamUtil;

public class JarFileObject implements FileObject {
	private static final long serialVersionUID = "JarFileObject".hashCode();


    private List<FileObject> children;
    private JarFile jarFile = null;
    private File file = null;
    private JarEntry jarEntry = null;
    private File cacheFile = null;

    public JarFileObject( String resource) {
        try {
            this.file = new File(resource);
            if (file.exists()) {
                this.jarFile = new JarFile(resource);
            }
        } catch (IOException e) {
        }
    }

    public JarFileObject(JarFileObject parent, JarEntry entry) {
        this.jarFile = parent.jarFile;
        this.file = parent.file;
        this.jarEntry = entry;
    }

    public void clean() {
        try {
            jarFile.close();
        } catch (IOException e) {
        }
    }

    public String getFileName() {
        if (jarEntry != null) {
            String[] names = jarEntry.getName().split("/");
            return names[names.length - 1];
        } else {
            return file.getName();
        }
    }

    public String getPath() {
        if (jarEntry != null) {
            return "/" + jarEntry.getName();
        }
        return "/";
    }

    public String getAbsolutePath() {
        String path = file.getAbsolutePath();
        if (File.separatorChar == '\\') {
            path = path.replaceAll("\\\\", "/");
        }
        if (jarEntry != null) {
            path = path + "!/" + jarEntry.getName();
        }
        return path;
    }

    public String getExtName() {
        String name;
        if (jarEntry != null) {
            if (!jarEntry.isDirectory()) {
                name = jarEntry.getName();
            } else {
                return null;
            }
        } else {
            name = file.getName();
        }
        int lastIndexOfDot = name.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            // 如果不存在
            return null;
        }
        return name.substring(lastIndexOfDot + 1);
    }

    public long getSize() {
        if (jarEntry != null) {
            return jarEntry.getSize();
        }
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    public InputStream getInputStream() {
        try {
            if (jarEntry != null) {
                if (cacheFile == null || !cacheFile.exists()
                        || cacheFile.length() != this.getSize()) {
                    createCacheFile();
                    FileOutputStream out = new FileOutputStream(cacheFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(
                            jarFile.getInputStream(jarEntry));
                    StreamUtil.io(bufferedInputStream, out, true, true);
                }
                return new BufferedInputStream(new FileInputStream(cacheFile));
            }
            if (file.exists() && file.isFile()) {
                return new JarInputStream(new FileInputStream(file));
            } else {
            	throw new RuntimeException("获取文件流失败");
            }
        } catch (Exception e) {
        	throw new RuntimeException("获取文件流失败",e);
        }
    }

    public String toString() {
        return getURL().toString();
    }

    public boolean isFolder() {
        if (jarEntry != null) {
            return jarEntry.isDirectory();
        }
        return true;
    }

    public List<FileObject> getChildren() {
        if (children == null) {
            if (!file.exists()) {
                return null;
            }
            createJarEntry();
        }
        return children;
    }

    private void createJarEntry() {
        children = new ArrayList<FileObject>();
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();
            if (getParent() == null) {
                String[] names = entry.getName().split("/");
                // 如果当前是jar文件，如果
                if (names.length == 1) {
                    addSubItem(entry);
                }
            } else {
                // 如果不是根目录
                String parentName = jarEntry.getName();
                if (!entry.getName().equals(jarEntry.getName())
                        && entry.getName().startsWith(parentName)) {
                    String fn = entry.getName().substring(
                            parentName.length());
                    String[] names = fn.split("/");
                    if (names.length == 1) {
                        addSubItem(entry);
                    }
                }
            }
        }
    }

    private void addSubItem(JarEntry entry) {
        JarFileObject jarFileObject = new JarFileObject(this, entry);
        jarFileObject.setParent(this);
        children.add(jarFileObject);
    }

    public long getLastModifiedTime() {
        if (jarEntry != null) {
            return jarEntry.getTime();
        }
        return file.lastModified();
    }

    public boolean isExist() {
        if (jarEntry != null) {
            return true;
        }
        return file.exists();
    }

    public boolean isInPackage() {
        return jarFile != null;
    }

    public FileObject getChild(String fileName) {
        if (getChildren() != null) {
            for (FileObject fileObject : getChildren()) {
                if (fileObject.getFileName().equals(fileName)) {
                    return fileObject;
                }
            }
        }
        return null;
    }

    public URL getURL() {
    	URL url = null;
        try {
        	url = file.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return url;
    }

    public OutputStream getOutputStream() {
        try {
            if (jarEntry != null) {
                if (cacheFile == null || !cacheFile.exists()
                        || cacheFile.length() != this.getSize()) {
                    createCacheFile();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(
                            jarFile.getInputStream(jarEntry));
                    FileOutputStream out = new FileOutputStream(cacheFile);
                    StreamUtil.io(bufferedInputStream, out, true, true);
                }
                return new BufferedOutputStream(new FileOutputStream(cacheFile));
            }
            if (file.exists() && file.isFile()) {
                return new JarOutputStream(new FileOutputStream(file));
            } else {
                throw new RuntimeException(file.getAbsolutePath()
                        + "不存在，或不是文件。");
            }
        } catch (Exception e) {
            throw new RuntimeException(file.getAbsolutePath()
                    + "获取outputStream出错，原因" + e);
        }
    }

    private void createCacheFile() {
        String tempPath = System.getProperty("java.io.tmpdir");
        if (!tempPath.endsWith(File.separator)) {
            tempPath = tempPath + File.separator;
        }
        tempPath = tempPath + getExtName() + File.separator;
        File tempPathFile = new File(tempPath);
        if (!tempPathFile.exists()) {
            tempPathFile.mkdirs();
        }
        cacheFile = new File(tempPath + getFileName() + "_"
                + getLastModifiedTime());
    }

	@Override
	public FileObject getParent() {
		return null;
	}

	@Override
	public void setParent(FileObject fileObject) {
		
	}

	@Override
	public FileObject getFileObject(String path) {
		return null;
	}
}
