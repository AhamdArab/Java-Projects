package de.uniwue.jpp.exams;

import java.util.Collection;
import java.util.Map;

public class ExamDatabase {

    Map<Student, Map<Exam, ExamResult>> data;
    public ExamDatabase(Map<Student, Map<Exam, ExamResult>> data) {
        this.data=data;
    }

    public Student getStudent(int matriculation) {
        return data.keySet().stream()
                .filter(s -> s.getMatriculation() == matriculation)
                .findFirst()
                .orElse(null);
    }

    public Map<Exam, ExamResult> getAllExamWithResultsOf(Student stud) {
        if (stud == null) {
            throw new NullPointerException("Student must not be null");
        }
        return data.get(stud);
    }

    public Collection<Exam> getAllExamsOf(Student stud) {
        if (stud == null) {
            throw new NullPointerException("Student is null");
        }
        Map<Exam, ExamResult> examResultMap = data.get(stud);
        if (examResultMap == null) {
            return null;
        }
        return examResultMap.keySet();
    }

    public Collection<Exam> getAllExamsOf(int matriculation) {
        Student student = getStudent(matriculation);
        if (student == null) {
            return null;
        } else {
            Map<Exam, ExamResult> examResultMap = data.get(student);
            if (examResultMap == null) {
                return null;
            } else {
                return examResultMap.keySet();
            }
        }
    }

}
