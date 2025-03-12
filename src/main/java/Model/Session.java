package Model;

import java.util.Date;
import java.sql.Time;

public class Session {
    private int id;
    private int trainerId;
    private int memberId;
    private Date sessionDate;
    private Time sessionTime;

    // For convenience - references to related objects
    private Trainer trainer;
    private Member member;

    // Default constructor
    public Session() {
    }

    // Parameterized constructor
    public Session(int id, int trainerId, int memberId, Date sessionDate, Time sessionTime) {
        this.id = id;
        this.trainerId = trainerId;
        this.memberId = memberId;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Time getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Time sessionTime) {
        this.sessionTime = sessionTime;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        if (trainer != null) {
            this.trainerId = trainer.getId();
        }
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            this.memberId = member.getId();
        }
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", trainerId=" + trainerId +
                ", memberId=" + memberId +
                ", sessionDate=" + sessionDate +
                ", sessionTime=" + sessionTime +
                '}';
    }
}
