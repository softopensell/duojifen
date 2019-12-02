package com.platform.util;

public class SynTest {
		public static void main(String[] argv) {
			new TestThread("12345678asdf").start();
			new TestThread("12345678asdf").start();
		}
	public void loop(String random) {
		System.out.println("thread name：" + Thread.currentThread().getName()
				+ " 传入的random:" + random);
		synchronized (random) {
			System.out.println("thread name："
					+ Thread.currentThread().getName() + " 开始执行循环");
			for (int i = 0; i < 10; i++) {
				System.out.println("thread name："
						+ Thread.currentThread().getName() + " i=" + i);
			}
			System.out.println("thread name："
					+ Thread.currentThread().getName() + " 执行循环结束");
 
		}
 
	}
 
}
class TestThread extends Thread {
	private String name;
 
	public TestThread(String name) {
		super();
		this.name = name;
	}
 
	public void run() {
		SynTest syntest = new SynTest();
		syntest.loop(name);
	}
 
}
