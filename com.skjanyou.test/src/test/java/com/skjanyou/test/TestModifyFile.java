package com.skjanyou.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestModifyFile {
	private static SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private static String time = "2021-05-01 01:20:20";
	private static Date date = null;
	private static Calendar end = null;
	public static void main(String[] args)  throws Exception {
		date = lsdStrFormat.parse(time);
		end = Calendar.getInstance();
		end.setTime(date);

		List<File> list = new ArrayList<>();
		File dir = new File("D:\\工作空间\\长亮网金\\长沙银行催收POC\\sql_pl");
		
		dirFile(dir,list);
		writeIn(dir,list);
	}

	private static void dirFile(File dir, List<File> list) throws Exception {
		File[] files = dir.listFiles();
		for (File file : files) {
			if( file.isDirectory() ) {
				dirFile(file,list);
			} else {
				if( file.getName().endsWith(".sql") ) {
					long lastModify = file.lastModified();
					Calendar cal = Calendar.getInstance();  
					cal.setTimeInMillis(lastModify);

					if( end.after(cal) ) {
						System.out.println(file);
						list.add(file);
					};

				}
			}

		}
	}

	private static void writeIn(File dir, List<File> list) throws Exception {
		System.out.println("---开始处理文件");
		for (File file : list) {
			System.out.println("开始处理" + file);
			// 文件头添加
			String tableName = file.getName().split(".sql")[0];
			InsertContent.insert(file, 0, "delete from " + tableName + ";\r\n");
			
			// 末尾添加
			OutputStream out = new FileOutputStream(file,true);
			OutputStreamWriter write = new OutputStreamWriter(out);
			BufferedWriter bw = new BufferedWriter(write);

			bw.write("\r\n");
			bw.write("commit;\r\n");
			bw.write("quit;");
			bw.flush();
			bw.close();
		}
	}


	static class InsertContent  {
		public static void insert(File fileName,long pos,String insertContent) throws IOException {
			RandomAccessFile raf=null;
			//创建临时文件保存插入点后数据
			File tmp=File.createTempFile("tmp", null);
			FileOutputStream tmpout=null;
			FileInputStream tmpin=null;
			tmp.deleteOnExit();//jvm退出的时候删除临时文件
			try {
				raf=new RandomAccessFile(fileName,"rw");
				tmpout=new FileOutputStream(tmp);
				tmpin=new FileInputStream(tmp);
				raf.seek(pos);
				//将插入点后的内容读入临时文件中保存
				byte[] bbuf=new byte[64];
				int hasRead=0;
				while((hasRead=raf.read(bbuf))>0) {
					tmpout.write(bbuf, 0, hasRead);
				}
				//把文件记录指针定位到pos
				raf.seek(pos);
				raf.write(insertContent.getBytes());
				while((hasRead=tmpin.read(bbuf))>0) {
					raf.write(bbuf, 0, hasRead);
				}

			}finally {
				raf.close();
			}

		}
	}
}
