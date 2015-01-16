package de.sander.til;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;

public class Primes {

	private Map<Integer,Integer> primes;
	private Map<Integer,Map<Integer,Integer>> exponents; // (number, (prime_factor, exponent)) 
	private Map<Integer,Map<Integer,Integer>> matches;
	private Map<Integer,List<Integer>> voids;
	private Map<String,Polynomial> polys;
	private int prime_offset = 0, prime_count=0;
	private static Primes instance = null;
	
	class Polynomial {
		
		private int x_pos, y_pos, x_step, factor;
		
		public Polynomial(int xp, int yp, int xs, int f) {
			this.x_pos = xp;
			this.y_pos = yp;
			this.x_step = xs;
			this.factor = f;
		}

		public int getXPos() {
			return x_pos;
		}

		public int getYPos() {
			return y_pos;
		}

		public int getStep() {
			return x_step;
		}

		public int getFactor() {
			return factor;
		}
		
	}
	
	private Primes() {
		this.primes = new HashMap<Integer,Integer>();
		this.exponents = new HashMap<Integer,Map<Integer,Integer>>();
		this.matches = new HashMap<Integer,Map<Integer,Integer>>();
		this.voids = new HashMap<Integer,List<Integer>>();
		this.polys = new HashMap<String,Polynomial>();
	}
	
	public static Primes getInstance() {
		if(instance == null) {
			synchronized(Primes.class) {
				if(instance == null) {
					instance = new Primes();
				}
			}
		}
		return instance;
	}
	
	public static Primes _() {
		return Primes.getInstance();
	}
	
	// generate cache
	
	private void generatePrimes(int up) {
		for (int i = this.prime_offset+1; i <= up; ++i) {
			if (this.checkPrime(i)) {
				this.prime_count++;
				this.primes.put(i,this.prime_count);
			}
			this.prime_offset = i;
		}
	}
	
	private void generateExponents(int num) {
		Map<Integer,Integer> entry = new TreeMap<Integer,Integer>();
		this.generatePrimes(num);
		for (int i = 2; i <= num; ++i) {
			if (this.isPrime(i)) {
				int exp = this.getExp(i, num);
				if (exp > 0) entry.put(i,exp);
			}
		}
		this.exponents.put(num,entry);
	}
	
	private void generateMatches(int n2) {
		if (n2 % 2 == 1) return;
		Map<Integer,Integer> entry = new TreeMap<Integer,Integer>();
		int m_count = 1;
		this.generatePrimes(n2-3);
		for (int i = 3; i <= n2/2; ++i) {
			if (this.isPrime(i) && this.isPrime(n2-i)) {
				entry.put(i,m_count);
				entry.put(n2-i,m_count);
				++m_count;
			}
		}
		this.matches.put(n2,entry);
	}
	
	// query methods
	
	public boolean isPrime(int number) {
		if (number > this.prime_offset) this.generatePrimes(number);
		return this.primes.get(number) != null;
	}
	
	public int primesUntil(int number) {
		int p;
		if (this.isPrime(number)) p = number;
		else p = this.getPrevPrime(number);
		this.generatePrimes(p);
		return this.primes.get(p);
	}
	
	public int getNextPrime(int number) {
		if (number <= 2) return 2;
		do {
			++number;
		} while (this.isPrime(number)==false);
		return number;
	}
	
	public int getPrevPrime(int number) {
		if (number <= 2) return 2;
		do {
			--number;
		} while (this.isPrime(number)==false);
		return number;
	}
	
	public int getExponent(int fac, int number) {
		if (fac > number) return 0;
		Map<Integer,Integer> ret = this.getExponents(number);
		if (ret == null) return 0;
		Integer exp = ret.get(fac);
		if (exp == null) return 0;
		else return exp.intValue();
	}
	
	public Map<Integer,Integer> getExponents(int number) {
		if (number <= 0) return null;
		Map<Integer,Integer> ret = this.exponents.get(number);
		if (ret == null) {
			this.generateExponents(number);
			ret = this.exponents.get(number);
		}
		return ret;
	}
	
	public int getExponentCount(int number) {
		Map<Integer,Integer> m = this.getExponents(number);
		if (m != null) return m.size();
		return 0;
	}
	
	public int getExponentSum(int number) {
		Map<Integer,Integer> m = this.getExponents(number);
		if (m != null) {
			int ret = 0;
			Iterator iter = m.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry)iter.next();
				ret += (Integer)entry.getValue();
			}
			return ret;
		}
		return 0;
	}
	
	public int getMaxExpCount(int number) {
		int ret=0, temp=0;
		for (int i = 4; i <= number; ++i) {
			temp = this.getExponentCount(i);
			if (temp > ret) ret = temp;
		}
		return ret;
	}
	
	public int getMaxExpSum(int number) {
		int ret=0, temp=0;
		for (int i = 4; i <= number; ++i) {
			temp = this.getExponentSum(i);
			if (temp > ret) ret = temp;
		}
		return ret;
	}
	
	public boolean isMatch(int prime, int n2) {
		if (prime > n2) return false;
		Map<Integer,Integer> ret = this.getMatches(n2);
		if (ret == null) return false;
		Integer mat = ret.get(prime);
		if (mat == null) return false;
		else return mat.intValue() > 0;
	}
	
	public Map<Integer,Integer> getMatches(int n2) {
		Map<Integer,Integer> ret = this.matches.get(n2);
		if (ret == null) {
			this.generateMatches(n2);
			ret = this.matches.get(n2);
		}
		return ret;
	}
	
	public int getMatchCount(int number) {
		Map<Integer,Integer> ret = this.getMatches(number*2);
		if (ret == null) return 0;
		return ret.size();
	}
	
	public int getMaxMatchCount(int number) {
		int ret=0, temp=0;
		for (int i = 2; i <= number; ++i) {
			temp = this.getMatchCount(i);
			if (temp > ret) ret = temp;
		}
		return ret;
	}
	
	public int getFirstMatch(int number) {
		Map<Integer,Integer> ret = this.getMatches(number*2);
		if (ret == null) return 0;
		try {
			return ret.keySet().iterator().next();
		} catch (Exception e) {}
		return 0;
	}
	
	public int getMaxFirstMatch(int upto) {
		int max = 0, temp = 0;
		for (int i = 1; i <= upto; ++i) {
			temp = this.getFirstMatch(i);
			if (temp > max) max = temp;
		}
		return max;
	}
	
	private List<Integer> getVoids(int number) {
		List<Integer> void_list = this.voids.get(number);
		if (void_list == null) {
			void_list = new ArrayList<Integer>();
			for (int i = 1; i <= number/2; ++i) {
				if (((number+i)%(1+i*2)==0)) void_list.add(1+i*2);
			}
			this.voids.put(number, void_list);
		}
		return void_list;
	}
	
	public int getFirstVoid(int number) {
		if (this.isMatch(3, (number+1)*2)) return 0;
		List<Integer> void_list = this.getVoids(number);
		if (void_list != null && void_list.size() > 0) return void_list.get(0);
		else return 0;
	}
	
	public int getVoidCount(int number) {
		if (this.isMatch(3, (number+1)*2)) return 0;
		List<Integer> void_list = this.getVoids(number);
		if (void_list != null) return void_list.size();
		else return 0;
	}
	
	public int getMaxFirstVoid(int upto) {
		int max = 0, temp = 0;
		for (int i = 1; i <= upto; ++i) {
			temp = this.getFirstVoid(i);
			if (temp > max) max = temp;
		}
		return max;
	}
	
	public int getMaxVoidCount(int upto) {
		int max = 0, temp = 0;
		for (int i = 1; i <= upto; ++i) {
			temp = this.getVoidCount(i);
			if (temp > max) max = temp;
		}
		return max;
	}
	
	public Polynomial getPoly(int xp, int yp, int pdelta, int y_factor) {
		if (pdelta <= 0 || y_factor <= 0) return null;
		if (xp <= 0 || yp <= 0) return null;
		if (yp%xp != 0) return null;
		if ((double)yp/(double)xp <= 2.0) return null;
		String pos_key = ""+xp+"."+yp+"."+pdelta+"."+y_factor;
		if (this.polys.containsKey(pos_key)) return this.polys.get(pos_key);
		
		int base = 0, step = 0, y_val = 0;
		boolean left=false, right=false;
		while (left == false || right == false) {
			++base;
			step += pdelta;
			y_val = base*base*pdelta*y_factor;
			if (left == false) {
				if (xp - step <= 0) left = true;
				else if ((double)(yp-y_val)/(double)(xp-step) < 2.0) left = true;
				else if ((yp-y_val) % (xp-step) != 0) break;
			}
			if (right == false) {
				if ((double)(yp-y_val)/(double)(xp+step) < 2.0) right = true;
				if ((yp-y_val) % (xp+step) != 0) break;
			}
		}
		
		if (left && right && base >= 2) {
			Polynomial ret = new Polynomial(xp,yp,pdelta,y_factor);
			this.polys.put(pos_key, ret);
			return ret;
		}
		this.polys.put(pos_key, null);
		return null;
	}
	
	public void cancelPoly(int xp, int yp) {
		String pos_key = ""+xp+"."+yp;
		this.polys.put(pos_key, null);
	}
	
	// algorithms
	
	private boolean checkPrime(int n) {
		boolean cb = this.cPrime(n);
		return cb;
	}
	
	private boolean cPrime(int n) {
		if(n < 2) return false;
	    if(n == 2 || n == 3) return true;
	    if(n%2 == 0 || n%3 == 0) return false;
	    int sqrtN = (int)Math.sqrt(n)+1;
	    for(int i = 6; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return false;
	    }
	    return true;
	}
	
	private boolean bPrime(int n) {
		if(n < 2) return false;
	    if(n == 2 || n == 3) return true;
	    for(int i = 2; i <= (n/2+1); ++i) {
	        if (n%i==0) return false;
	    }
	    return true;
	}
	
	private int getExp(int pfac, int number) {
		if (pfac <= 0) return 0;
		if (pfac == 1) return 1;
		int exp = 0;
		while (number%pfac == 0) {
			number /= pfac;
			++exp;
		}
		return exp;
	}

}
