package com.skjanyou.util.plus.chain;

import java.util.List;

import com.skjanyou.util.CollectionUtil;

public class ChainProcessTest {
	static class Student {
		private String name;
		private int age;
	}
	
	static class StudentChain1 implements Chain<Student> {

		@Override
		public int getOrder() {
			return 1;
		}

		@Override
		public void doChain(ChainProcess<Student> chainProcess, Student student ) {
			System.out.println("chain 1");
			System.out.println("student:" + student.name + "," + student.age);
			student.age++;
			chainProcess.doChain(student);
		}
		
	}
	
	static class StudentChain2 implements Chain<Student> {

		@Override
		public int getOrder() {
			return 2;
		}

		@Override
		public void doChain(ChainProcess<Student> chainProcess, Student student ) {
			System.out.println("chain 2");
			System.out.println("student:" + student.name + "," + student.age);
			student.age++;
			chainProcess.doChain(student);			
		}
		
	}
	
	static class StudentChain3 implements Chain<Student> {

		@Override
		public int getOrder() {
			return 3;
		}

		@Override
		public void doChain(ChainProcess<Student> chainProcess, Student student ) {
			System.out.println("chain 3");
			System.out.println("student:" + student.name + "," + student.age);
			student.age++;
			chainProcess.doChain(student);
		}
		
	}
	
	public static void main(String[] args) {
		StudentChain1 chain1 = new StudentChain1();
		StudentChain2 chain2 = new StudentChain2();
		StudentChain3 chain3 = new StudentChain3();
		
		List<Chain<Student>> chains = CollectionUtil.of( chain1,chain2,chain3 );
		
		ChainHandler<Student> handler = new ChainHandler<>(chains);
		Student student = new Student();
		student.age = 10;
		student.name = "张三";
		handler.doChainProcess(student);
	}
}
