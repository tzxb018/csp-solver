package csp;

import java.lang.management.*;

/* Example of counting cpu/system/user time.
 * Use the getCpuTime/getUserTime/getSystemTime to capture the timestamp.
 * Capture timestamp before action.
 * Capture timestamp after action.
 * Your running time is between before-action timestamp and after-action timestamp.
 * See example in method work().
 * Tested with Java 11.
 */

public class Timer {

    protected long captureTime;
    protected long runningTime;

    public Timer() {
    }

    public void foo() {
        int s = 0;
        for (int i = 0; i < 1000000; i++) {
            s += 1;
        }
    }

    public void work() {
        captureTime = getCpuTime();
        foo();
        System.out.println("CPU time: " + (getCpuTime() - captureTime));
    }

    /** Get cpu time in nanoseconds. */
    public long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported())
            return 0L;
        return bean.getThreadCpuTime(java.lang.Thread.currentThread().getId());
    }

    /** Get user time in nanoseconds. */
    public long getUserTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported())
            return 0L;
        return bean.getThreadUserTime(java.lang.Thread.currentThread().getId());
    }

    /** Get system time in nanoseconds. */
    public long getSystemTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported())
            return 0L;
        return bean.getThreadCpuTime(java.lang.Thread.currentThread().getId())
                + bean.getThreadUserTime(java.lang.Thread.currentThread().getId());
    }

    public static void main(String[] args) {
        new Timer().work();
    }
}