package de.sander.til;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Primes {

	private Map<Integer,Integer> primes;
	private Map<Integer,Map<Integer,Integer>> exponents; // (number, (prime_factor, exponent)) 
	private Map<Integer,Map<Integer,Integer>> matches;
	private int prime_offset = 0, prime_count=0;
	private static Primes instance = null;
	
	private Primes() {
		this.primes = new HashMap<Integer,Integer>();
		this.exponents = new HashMap<Integer,Map<Integer,Integer>>();
		this.matches = new HashMap<Integer,Map<Integer,Integer>>();
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
		HashMap<Integer,Integer> entry = new HashMap<Integer,Integer>();
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
		HashMap<Integer,Integer> entry = new HashMap<Integer,Integer>();
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
	
	public int getMatchCount(int n2) {
		Map<Integer,Integer> ret = this.getMatches(n2);
		if (ret == null) return 0;
		return ret.size();
	}
	
	public int getMaxMatchCount(int n2) {
		int ret=0, temp=0;
		for (int i = 4; i <= n2; ++i) {
			temp = this.getMatchCount(i);
			if (temp > ret) ret = temp;
		}
		return ret;
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
