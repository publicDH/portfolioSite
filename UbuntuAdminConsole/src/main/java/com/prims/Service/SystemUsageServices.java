package com.prims.Service;

import java.io.File;
import java.lang.management.ManagementFactory;

import java.lang.management.RuntimeMXBean;

import org.springframework.stereotype.Service;

import com.sun.management.OperatingSystemMXBean;

@Service
public class SystemUsageServices {
	
	public String[] getDiskSpace() {
		File root = null;
		try {
			root = new File("/");
			String[] list = new String[2];
			list[0] = toMB(root.getTotalSpace()); 
			list[1] = toMB(root.getFreeSpace());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
	public String toMB(long size) {
		return String.valueOf((int) (size / (1024 * 1024)));
	}
	
	public Integer getCPULoad() {
		OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory

			      .getOperatingSystemMXBean();

		int cpuLoad = 0;

			    while (cpuLoad * 100 == 0) {
			    	cpuLoad = (int) osbean.getCpuLoad();
			    }
			    
			    return (int)(cpuLoad * 100);
	}
	
	public Long getRamLoad() {
		OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory

			      .getOperatingSystemMXBean();
		
				Long Value = (long) 0;


			    while (Value == 0) {
			    	Value = osbean.getFreeMemorySize();
			    }
			    
			    return Value;
	}
	
	public Long getTotalRam() {
		OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory

			      .getOperatingSystemMXBean();
		
				Long Value = (long) 0;


			    while (Value == 0) {
			    	Value = osbean.getTotalMemorySize();
			    }
			    
			    return Value;
	}
}
