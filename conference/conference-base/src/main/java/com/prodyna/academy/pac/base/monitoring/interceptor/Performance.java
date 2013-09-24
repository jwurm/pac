package com.prodyna.academy.pac.base.monitoring.interceptor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;

public class Performance implements PerformanceMXBean {

	@Inject
	private Logger log;

	public static final String OBJECT_NAME = "monitoring:service=performance";
	private HashMap<String, Entry> entries = new HashMap<String, Entry>();

	@Override
	public void reset() {
		entries.clear();

	}

	@Override
	public void report(String service, String method, long time, boolean success) {
		String key = buildKey(service, method);
		Entry entry = entries.get(key);
		if (entry == null) {
			entry = new Entry(service, method);
			entries.put(key, entry);
		}
		entry.increment(time, success);
	}

	private String buildKey(String service, String method) {
		return service + "." + method;
	}

	@Override
	public List<Entry> getAll() {
		return new ArrayList<Entry>(entries.values());
	}

	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Entry getWorstByTime() {
		float max = 0L;
		Entry maxEntry = null;
		for (java.util.Map.Entry<String, Entry> curr : entries.entrySet()) {
			float time = curr.getValue().getAverage()
					* curr.getValue().getCount();
			if (time > max) {
				max = time;
				maxEntry = curr.getValue();
			}
		}
		return maxEntry;
	}

	@Override
	public Entry getWorstByAverage() {
		float max = 0L;
		Entry maxEntry = null;
		for (java.util.Map.Entry<String, Entry> curr : entries.entrySet()) {
			if (curr.getValue().getAverage() > max) {
				max = curr.getValue().getAverage();
				maxEntry = curr.getValue();
			}
		}
		return maxEntry;
	}

	@Override
	public Entry getWorstByCount() {
		int max = 0;
		Entry maxEntry = null;
		for (java.util.Map.Entry<String, Entry> curr : entries.entrySet()) {
			if (curr.getValue().getCount() > max) {
				max = curr.getValue().getCount();
				maxEntry = curr.getValue();
			}
		}
		return maxEntry;
	}

	@Override
	public void dump() {
		log.info(entries.values().toString());

	}

	public Entry get(String service, String method) {
		return entries.get(buildKey(service, method));
	}

}