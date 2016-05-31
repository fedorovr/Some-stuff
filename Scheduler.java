import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by roman on 26.05.2016.
 */

interface Task {
    void execute();

    Set<Task> dependencies();
}

class CustomTask implements Task {
    private String str;
    private Set<Task> dependencies;

    public CustomTask(String str) {
        this.str = str;
        this.dependencies = new HashSet<Task>();
    }

    public CustomTask addDependency(Task task) {
        this.dependencies.add(task);
        return this;
    }

    @Override
    public void execute() {
        System.out.println(this.str);
    }

    @Override
    public Set<Task> dependencies() {
        return dependencies;
    }
}

// Don't support circular dependencies.
public class Scheduler {
    private ArrayList<Task> tasks;
    private Set<Task> alreadyExecutedTasks;

    public Scheduler(Task... tasks) {
        this.tasks = new ArrayList<Task>();
        this.alreadyExecutedTasks = new HashSet<Task>();
        Collections.addAll(this.tasks, tasks);
    }

    private void executeTaskAndDependencies(Task task) {
        if (!alreadyExecutedTasks.contains(task)) {
            alreadyExecutedTasks.add(task);
            for (Task dependentTask : task.dependencies()) {
                executeTaskAndDependencies(dependentTask);
            }
            task.execute();
        }
    }

    public void execute() {
        for (Task task : tasks) {
            executeTaskAndDependencies(task);
        }
    }

    public static void main(String[] args) {
        CustomTask taskE = new CustomTask("E");
        CustomTask taskK = new CustomTask("K");
        CustomTask taskF = new CustomTask("F");
        taskF.addDependency(taskE).addDependency(taskK);
        CustomTask taskD = new CustomTask("D");
        taskD.addDependency(taskE);
        CustomTask taskC = new CustomTask("C");
        taskC.addDependency(taskE).addDependency(taskD).addDependency(taskF);
        CustomTask taskA = new CustomTask("A");
        taskA.addDependency(taskD);

        Scheduler s = new Scheduler(taskA, taskC, taskE);
        s.execute();
    }
}

