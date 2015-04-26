package de.sander.til;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrimeTest {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PrimeTest.class.getSimpleName());
	
	class Point {
		public double x, y;
		
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static void test() {
		
		/*
		int temp;
		for (int n = 4; n <= 200; ++n) {
			System.out.println("-------- Pi("+n+") -------");
			boolean q = false;
			for (int i = 3; i <= n; ++i) {
				System.out.println("i = "+i);
				for (int k = 2; k <= i-1; ++k) {
					temp = k*(i+1-k);
					if (!q && temp > n && k == 2) {
						System.out.println("\n???????????????????????????????????????????????????????");
						q = true;
					}
					System.out.print(" "+temp);
				}
				System.out.println();
			}
		}*/
		
		TreeMap<Integer, TreeMap<Integer,Integer>> points = new TreeMap<Integer, TreeMap<Integer,Integer>>();
		HashMap<Integer,HashMap<Integer,Integer>> polys = new HashMap<Integer,HashMap<Integer,Integer>>();
		int n;
		for (int i = 3; i <= 300; ++i) {
			HashMap<Integer,Integer> poly = new HashMap<Integer,Integer>();
			//System.out.println("----- i: "+i);
			for (int k = 2; k <= i-1; ++k) {
				n = k*(i+1-k);
				//System.out.print(" "+n);
				poly.put(k, n);
				if (points.containsKey(n) == false) points.put(n, new TreeMap<Integer,Integer>());
				if (n <= 300) points.get(n).put(i,k);
			}
			//System.out.println();
			polys.put(i,poly);
		}
		
		Iterator<Entry<Integer, TreeMap<Integer, Integer>>> iter = points.entrySet().iterator();
		System.out.println("n	i	k");
		while (iter.hasNext()) {
			Map.Entry<Integer, TreeMap<Integer, Integer>> entry = (Map.Entry<Integer, TreeMap<Integer, Integer>>)iter.next();
			Integer nk = (Integer)entry.getKey();
			TreeMap<Integer,Integer> np = (TreeMap<Integer,Integer>)entry.getValue();
			Iterator<Entry<Integer, Integer>> inner = np.entrySet().iterator();
			while (inner.hasNext()) {
				Map.Entry<Integer, Integer> ie = (Map.Entry<Integer, Integer>)inner.next();
				Integer i = (Integer)ie.getKey();
				Integer k = (Integer)ie.getValue();
				System.out.println(""+nk+"	"+i+"	"+(((i/2+1)-k)*(-1)));
			}
		}
		
	}

}
