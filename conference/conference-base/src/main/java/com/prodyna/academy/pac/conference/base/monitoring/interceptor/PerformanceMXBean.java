package com.prodyna.academy.pac.conference.base.monitoring.interceptor;



import java.util.List;

public interface PerformanceMXBean {
	void reset();
	void report(String service, String method, long time, String success);
	List<Entry> getAll();
	int getCount();
	Entry getWorstByTime();
	Entry getWorstByAverage();
	Entry getWorstByCount();
	void dump();
	void dumpSorted();

}