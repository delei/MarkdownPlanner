package org.xumingmingv.markdownplanner.web;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.xumingmingv.markdownplanner.model.IProject;
import org.xumingmingv.markdownplanner.model.task.AtomicTask;
import org.xumingmingv.markdownplanner.model.task.Task;

public class Converters {
    public static TaskVO convert(Task task) {
        TaskVO taskVO = new TaskVO();

        taskVO.setId(task.getId());
        taskVO.setParentId(task.getParentId());
        taskVO.setName(task.getName());
        taskVO.setOwner(task.getOwner());
        taskVO.setBooleanComposite(false);
        taskVO.setStartDate(task.getStartDate().toString());
        taskVO.setEndDate(task.getEndDate().toString());

        String progress = task.getProgress() + "%";
        if (task.isDelayed()) {
            progress += "(期望:" + task.getExpectedProgress() + "%)";
        }
        taskVO.setProgress(progress);
        taskVO.setRawProgress(task.getProgress());

        taskVO.setManDays(String.format("%.1f", task.getCost() / 2.0));
        // compute background color
        String bgColorClass = "markdownplanner-bg-secondary";
        if (task.isStarted()) {
            bgColorClass = "markdownplanner-bg-primary";
            if (task.isCompleted()) {
                bgColorClass = "markdownplanner-bg-success";
            } else if (task.isDelayed()) {
                bgColorClass = "markdownplanner-bg-warning";
            }
        } else {
            if (task.isDelayed()) {
                bgColorClass = "markdownplanner-bg-danger";
            }
        }
        taskVO.setBgColorClass(bgColorClass);
        taskVO.setBooleanComposite(task.isComposite());
        if (!task.isComposite()) {
            taskVO.setLineNumber(((AtomicTask)task).getLineNumber());
        }
        return taskVO;
    }

    public static ProjectVO convert(IProject project) {
        ProjectVO projectVO = new ProjectVO();
        projectVO.setTasks(
            project.getTasks().stream()
                .map(Converters::convert)
                .collect(Collectors.toList())
        );
        projectVO.setTotalManDays(project.getTotalStat().getTotalCost() / 2);
        projectVO.setMen(project.getMen());
        LocalDate projectStartDate = project.getProjectStartDate();
        projectVO.setProjectStartDate(projectStartDate == null ? null : projectStartDate.toString());
        LocalDate projectEndDate = project.getProjectEndDate();
        projectVO.setProjectEndDate(projectEndDate == null ? null : projectEndDate.toString());
        projectVO.setUserStats(project.getUserStats());

        return projectVO;
    }
}
