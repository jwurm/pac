package com.prodyna.academy.pac.base.monitoring.interceptor;



import java.util.List;

public interface PerformanceMXBean {
	void reset();
	void report(String service, String method, long time, boolean success);
	List<Entry> getAll();
	int getCount();
	Entry getWorstByTime();
	Entry getWorstByAverage();
	Entry getWorstByCount();
	void dump();

}
