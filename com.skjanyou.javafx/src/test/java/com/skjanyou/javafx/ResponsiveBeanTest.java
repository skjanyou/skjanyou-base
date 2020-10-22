package com.skjanyou.javafx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.skjanyou.javafx.core.BeanProperty;
import com.skjanyou.javafx.core.BeanPropertyHelper;

public class ResponsiveBeanTest {
	public static class Student {
		String name;
		String age;
		boolean human;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAge() {
			return age;
		}
		public void setAge(String age) {
			this.age = age;
		}
		public boolean isHuman() {
			return human;
		}
		public void setHuman(boolean human) {
			this.human = human;
		}
	}
	public static void main(String[] args) {
		BeanProperty beanProperty = new BeanPropertyHelper(Student.class).builder();
		beanProperty.getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt);
			}
		});
		
		
		
		Student student = (Student) beanProperty.getBean();
		student.setAge("100");
		
	}

}
