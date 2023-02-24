import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Homework2 {
	private static AtomicInteger counter = new AtomicInteger(0);
	private static int number = 100;
	private static AtomicInteger leader;
	private static boolean exist = false;
	public static AtomicInteger guest;
	private static boolean [] visited = new boolean[number]; 
	private static Thread[] threadArray;
	private static AtomicBoolean flag = new AtomicBoolean(false);
	
	public static void main(String [] args) {
		long start = System.currentTimeMillis();
		threadArray = new Thread[number];
		guest = new AtomicInteger((int)(Math.random() * number));
		leader = guest;
		for (int i = 0; i < threadArray.length; i++) {
			final int threadi = i;
			threadArray[threadi] = new Thread(new Runnable() {
				public void run() {
					while(!flag.get()) {
							try {
								maze(threadi);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							guest = new AtomicInteger((int)(Math.random() * number));
					}					
				}
			});
		}
		for (int i = 0; i < threadArray.length; i++) {
			threadArray[i].start();
		}
		for (int i = 0; i < threadArray.length; i++) {
			try {
				threadArray[i].join();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start)/1000.00 + " seconds");
	}
	
	public static synchronized void maze(int threadi) throws InterruptedException {
		if (guest.get() == threadi) {
			Thread.sleep(0, 1);
			if (counter.get() == number - 1) {
				if (!flag.get())
					flag.set(true);
				return;
			}
			if (visited[guest.get()] && guest.get() != leader.get()) {}			
			else if (guest.get() == leader.get() && counter.get() == 0) {
				exist = false;
				counter.getAndIncrement();
				visited[leader.get()] = true;
			}
			else if (guest.get() == leader.get() && exist == true) {
				exist = false;
				counter.getAndIncrement();
			}
			else if (visited[guest.get()] == false && exist == false) {
				exist = true;
				visited[guest.get()] = true;
			}
		}		
	}
}
