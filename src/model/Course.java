package model;

import java.util.Objects;

public class Course {
    private final String code;
    private String name;
    private int sks;
    private int semester;
    private String major;
    private String desc;

    public Course(String code, String name, int sks, int semester, String major, String desc) {
        this.code = code;
        this.name = name;
        this.sks = sks;
        this.semester = semester;
        this.major = major;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getSks() {
        return sks;
    }

    public int getSemester() {
        return semester;
    }

    public String getMajor() {
        return major;
    }

    public String getDesc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course)) return false;

        Course other = (Course) obj;
        return Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code;
    }
}