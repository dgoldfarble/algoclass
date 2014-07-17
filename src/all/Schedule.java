package all;

import sun.jvm.hotspot.ci.ciTypeArrayKlass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dgoldfarb on 7/10/14.
 */
public class Schedule {

    class Job implements Comparable<Job> {
        int length;
        int weight;
        int diff;
        float ratio;
        int completionTime;

        public Job(int w, int l, int d, float r) {
            this.length = l;
            this.weight = w;
            this.diff = d;
            this.ratio = r;
        }

        public void print() {
            System.out.println("[" + this.weight + ", " + this.length + ", " + this.diff
                    + ", " + this.ratio + ", " + this.completionTime + "]");
        }

        @Override
        public int compareTo(Job that) {
            if (Schedule.diff) {
                if (this.diff == that.diff) {
                    if (this.weight == that.weight) {
                        return 0;
                    } else if (this.weight < that.weight) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (this.diff < that.diff) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (this.ratio == that.ratio) {
                    if (this.weight == that.weight) {
                        return 0;
                    } else if (this.weight < that.weight) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (this.ratio < that.ratio) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    ArrayList<Job> tasksAndDiffs;
    int numTasks;
    public static boolean diff = true;

    public Schedule(String arg) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(arg)));
        String line = reader.readLine();
        this.numTasks = Integer.parseInt(line);
        tasksAndDiffs = new ArrayList<Job>();

        Job job;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            String[] bits = line.split(" ");
            int weight = Integer.parseInt(bits[0]);
            int length = Integer.parseInt(bits[1]);
            job = new Job(weight, length, weight - length, (float) weight / (float) length);
            tasksAndDiffs.add(job);
            i++;
        }
    }

    public static void main(String[] args) throws IOException {
        Schedule schedule = new Schedule(args[0]);
        schedule.order();
        long cost = schedule.cost();
        for (Job job : schedule.tasksAndDiffs) {
            job.print();
        }
        System.out.println(cost);
    }

    private long cost() {
        int completion_time = 0;
        long weightedSum = 0;
        for (Job job : tasksAndDiffs) {
            job.completionTime = completion_time + job.length;
            weightedSum += job.weight * job.completionTime;
            completion_time += job.length;
        }
        return weightedSum;
    }

    private void order() {
        Collections.sort(tasksAndDiffs);
        Collections.reverse(tasksAndDiffs);
    }
}
