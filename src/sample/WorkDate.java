package sample;

import com.sun.org.apache.xerces.internal.util.Status;

import java.sql.Date;
import java.sql.Time;

public class WorkDate {
    private Date date;
    private Time time;
    private Status status;
    private int employee_id;
    private int employee_pr_id;

    public WorkDate(Date date, Time time, Status status, int employee_id, int employee_pr_id) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.employee_id = employee_id;
        this.employee_pr_id = employee_pr_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getEmployee_pr_id() {
        return employee_pr_id;
    }

    public void setEmployee_pr_id(int employee_pr_id) {
        this.employee_pr_id = employee_pr_id;
    }
}
