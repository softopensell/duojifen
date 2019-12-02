package com.platform.util;
public class SynTest2 {
	public static void main(String[] argv) {
		SynTest2 syn1=new SynTest2();
		SynTest2 syn2=new SynTest2();
		new TestThread2(syn1).start();
		new TestThread2(syn1).start();
		new TestThread2(syn2).start();
	}
	public void loop() {
		System.out.println("thread name：" + Thread.currentThread().getName());
		synchronized (SynTest.class) {
			System.out.println(" thread name："
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
class TestThread2 extends Thread {
	private SynTest2 syn;
 
	public TestThread2(SynTest2 syn) {
		super();
		this.syn = syn;
	}
 
	public void run() {
		syn.loop();
	}
}
