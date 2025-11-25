package de.uniwue.jpp.exams;

import java.util.Objects;

public class Student {

    String name;
    int matriculation;
    int age;
    public Student(String name, int matriculation, int age) {
        if (name==null){
            throw new NullPointerException();
        }
        if (name.isEmpty()){
            throw new IllegalArgumentException("name cannot be empty!");
        }
        this.name=name;
        this.matriculation=matriculation;
        this.age=age;
    }

    public String getName() {
        return name;
    }

    public int getMatriculation() {
        return matriculation;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return matriculation == student.matriculation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matriculation);
    }

    @Override
    public String toString() {
       return "Student "+"\""+name+"\"";
    }

}
