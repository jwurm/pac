package com.prodyna.academy.pac.base.monitoring.interceptor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Entry {
	private String service;
	private String method;
	private int count;
	private long min = Long.MAX_VALUE;
	private long max = Long.MIN_VALUE;
	private long sum;

	private Queue<Boolean> calls = new LinkedList<Boolean>();
	private int callsBufferLength = 100;

	public Entry(String service, String method) {
		super();
		this.service = service;
		this.method = method;
	}

	public void increment(long time, boolean success) {
		count++;
		sum += time;
		min = Math.min(min, time);
		max = Math.max(max, time);

		calls.add(success);
		if (calls.size() > callsBufferLength) {
			calls.poll();
		}
	}

	public float getAverage() {
		if (count > 0) {
			return (float) sum / (float) count;
		} else {
			return 0;
		}
	}

	public String getService() {
		return service;
	}

	public String getMethod() {
		return method;
	}

	public long getMax() {
		return max;
	}

	public long getMin() {
		return min;
	}
	
	public String getSuccessString(){
		StringBuilder sb = new StringBuilder();
		
		for (Boolean curr : calls) {
			if(curr){
				sb.append("-");
			}else{
				sb.append("X");
			}
		}
		return sb.toString();
	}

	public int getCount() {
		return count;
	}

}
