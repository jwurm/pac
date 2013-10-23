package com.prodyna.academy.pac.conference.base.monitoring.interceptor;

import java.util.LinkedList;
import java.util.Queue;

import com.prodyna.academy.pac.conference.base.BusinessException;

public class Entry {
	private String service;
	private String method;
	private int count;
	private long min = Long.MAX_VALUE;
	private long max = Long.MIN_VALUE;
	private long sum;

	private Queue<String> calls = new LinkedList<String>();
	private int callsBufferLength = 100;

	public Entry(String service, String method) {
		super();
		this.service = service;
		this.method = method;
	}

	@Override
	public String toString() {
		return "Entry [service=" + service + ", method=" + method + ", count="
				+ count + ", min=" + min + ", max=" + max + ", sum=" + sum
				+ ", calls=" + calls + ", callsBufferLength="
				+ callsBufferLength + "]";
	}

	public void increment(long time, String successType) {
		count++;
		sum += time;
		min = Math.min(min, time);
		max = Math.max(max, time);

		calls.add(successType);
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

	/**
	 * Builds a string that codes the success information of the past requests.
	 * Codes: Success: - Business exception: O Any other, usually technical
	 * exception: X
	 * 
	 * @return String
	 */
	public String getSuccessString() {
		StringBuilder sb = new StringBuilder();

		for (String curr : calls) {
			sb.append(curr);
		}
		return sb.toString();
	}

	public int getCount() {
		return count;
	}

}
