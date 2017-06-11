package servidor.torcedor.digital;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerTest {

	public static void main(String[] args) {

		new Thread(() -> {
			test1 t = new test1();
			t.start();
			test2 t2 = new test2();
			t2.start();
			test3 t3 = new test3();
			t3.start();
		}).start();

	}

}

class test1 {

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("oi blz!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 5, TimeUnit.SECONDS);

	}

}

class test2 {

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {


			@Override
			public void run() {
				try {
					
					System.out.println("kkkk ...");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);

	}

}

class test3 {

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("CARACA!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 10, TimeUnit.SECONDS);

	}

}
