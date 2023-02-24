import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Queue {
	boolean[] flag;
	AtomicInteger next = new AtomicInteger(0);;
	ThreadLocal<Integer> index = new ThreadLocal<Integer>();
	int n;
	public Queue(int num) {
		n = num;
		flag = new boolean[num];
		flag[0] = true;
	}
	public void lock() {
		int mySlot = next.getAndIncrement();
		index.set(mySlot);
		while (! flag[mySlot % n]) {};
		flag[mySlot % n] = false;
	}
	public void unlock() {
		flag[(index.get() + 1) % n] = true;
	}
}

public class Homework2Number2 {
	private static AtomicInteger counter = new AtomicInteger(0);
	private static int number = 100;
	private static Queue q = new Queue(number);
	public static AtomicInteger guest;
	private static boolean [] visited = new boolean[number]; 
	private static Thread[] threadArray;
	private static AtomicBoolean flag = new AtomicBoolean(false);
	public static void main(String [] args) {
		long start = System.currentTimeMillis();
		threadArray = new Thread[number];
		guest = new AtomicInteger((int)(Math.random() * number));
		for (int i = 0; i < threadArray.length; i++) {
			final int threadi = i;
			threadArray[i] = new Thread(new Runnable() {
				public void run() {
					while (counter.get() < number && !flag.get()) {
						if (guest.get() == threadi) {
							q.lock();
							q.unlock();
							int random = (int)(Math.random() * 2);
							if (random == 0) {
								visited[guest.get()] = true;
								counter.getAndIncrement();
							}
							guest = new AtomicInteger((int)(Math.random() * number));
							if (counter.get() == number) {
								flag.set(true);
								long end = System.currentTimeMillis();
								System.out.println((end - start)/1000.00 + " seconds");
							}
						}
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
	}
	
	
}
