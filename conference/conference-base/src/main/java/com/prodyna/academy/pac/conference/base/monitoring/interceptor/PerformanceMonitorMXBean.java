package com.prodyna.academy.pac.conference.base.monitoring.interceptor;

import java.util.List;

/**
 * Performance Monitor interface definition
 * 
 * @author Jens Wurm
 * 
 */
public interface PerformanceMonitorMXBean {
	/**
	 * Clears all data
	 */
	void reset();

	/**
	 * Enters a new report of a service invocation
	 * @param service Name of the service
	 * @param method Name of the method
	 * @param time Duration of the invocation
	 * @param success Success indicator string
	 */
	void report(String service, String method, long time, String success);

	/**
	 * Retrieves all entries
	 * @return
	 */
	public List<Entry> getAll();

	/**
	 * Returns the number of entries
	 * @return long
	 */
	public int getCount();

	/**
	 * Returns the entry that took up the most time in total
	 * @return Entry
	 */
	public Entry getWorstByTime();

	/**
	 * Returns the entry that has the longest average invocation time
	 * @return Entry
	 */
	public Entry getWorstByAverage();
	/**
	 * Returns the entry that has the highest call count
	 * @return Entry
	 */
	public Entry getWorstByCount();

	/**
	 * Dumps all the data into the log file
	 */
	public void dump();


	/**
	 * Dumps all the data into the log file, sorted by service name and method name
	 */
	public void dumpSorted();

}
