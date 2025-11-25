package de.uniwue.jpp.exams;

import java.util.Objects;

public class Exam {

    int year;
    boolean isWinterTerm;
    String name;

    public Exam(int year, boolean isWinterTerm, String name) {
        if (name==null){
            throw new NullPointerException();
        }
        if (name.isEmpty()){
            throw new IllegalArgumentException("name cannot be empty!");
        }
        this.year = year;
        this.isWinterTerm = isWinterTerm;
        this.name = name;
    }

    public int getYear() {
       return year;
    }

    public boolean isWinterTerm() {
        return isWinterTerm;
    }

    public String getName() {
       return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return year == exam.year && isWinterTerm == exam.isWinterTerm && Objects.equals(name, exam.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, isWinterTerm, name);
    }

    @Override
    public String toString() {
        if (!isWinterTerm)
            return "Exam "+"\""+name+"\" "+year+" SS";
        else
            return "Exam "+"\""+name+"\" "+year+" WS";

    }
}
