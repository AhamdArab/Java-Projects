package de.uniwue.jpp.exams;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;

import java.util.*;

public class ExamDatabaseOWM {

    Map<Student, Map<Exam, ExamResult>> data;

    public ExamDatabaseOWM(Map<Student, Map<Exam, ExamResult>> data) {
        this.data = data;
    }

    public OptionalWithMessage<Student> getStudent(int matriculation) {
        Optional<Student> student = data.keySet().stream()
                .filter(s -> s.getMatriculation() == matriculation)
                .findFirst();

        if (student.isPresent()) {
            return OptionalWithMessage.of(student.get());
        } else {
            return OptionalWithMessage.ofMsg("No student with matriculation " + matriculation);
        }
    }

    public OptionalWithMessage<Map<Exam, ExamResult>> getAllExamsWithResultsOf(Student stud) {
        Map<Exam, ExamResult> examResults = data.get(stud);
        return examResults != null ? OptionalWithMessage.of(examResults) : OptionalWithMessage.ofMsg("Student is unknown!");
    }

    public OptionalWithMessage<Collection<Exam>> getAllExamsOf(Student stud) {
        if (stud == null) {
            throw new NullPointerException("Student must not be null.");
        }
        Collection<Exam> exams = data.getOrDefault(stud, new HashMap<>()).keySet();
        if (exams.isEmpty()) {
            return OptionalWithMessage.ofMsg("Student is unknown!");
        } else {
            return OptionalWithMessage.of(exams);
        }
    }

    public OptionalWithMessage<Collection<Exam>> getAllExamsOf(int matriculation) {
        Student student = data.keySet().stream()
                .filter(s -> s.getMatriculation() == matriculation)
                .findFirst()
                .orElse(null);

        if (student == null) {
            return OptionalWithMessage.ofMsg("No student with matriculation " + matriculation);
        }

        return OptionalWithMessage.of(data.get(student).keySet());
    }

    public static OptionalWithMessage<ExamResult> getResultOf(Exam exam, Map<Exam, ExamResult> results) {

        if (exam == null || results == null) {
            throw new NullPointerException("exam and results map cannot be null");
        }
        ExamResult result = results.get(exam);
        return OptionalWithMessage.ofNullable(result, "No result for this exam!");
    }

    public OptionalWithMessage<ExamResult> getResult(int matriculation, Exam exam) {
        OptionalWithMessage<Student> student = getStudent(matriculation);
        if (student.isPresent()) {
            Map<Exam, ExamResult> results = data.get(student.get());
            if (results != null && results.containsKey(exam)) {
                return OptionalWithMessage.of(results.get(exam));
            } else {
                return OptionalWithMessage.ofMsg("No result for this exam!");
            }
        } else {
            return OptionalWithMessage.ofMsg("No student with matriculation " + matriculation);
        }
    }

    public OptionalWithMessage<String> getNameOf(int matriculation) {
        OptionalWithMessage<Student> student = getStudent(matriculation);
        if (student.isPresent()) {
            return OptionalWithMessage.of(student.get().getName());
        } else {
            return OptionalWithMessage.ofMsg("No student with matriculation " + matriculation);
        }
    }

    public boolean hasPassed(int matriculation, Exam exam) {
        ExamResult examResult = getResult(matriculation, exam).orElse(null);
        return examResult != null && examResult.isPassed();
    }

    public Collection<Student> hasPassed(Collection<Integer> matriculations, Exam exam) {
        List<Student> students = new ArrayList<>();
        for (int matriculation : matriculations) {
            OptionalWithMessage<ExamResult> result = getResult(matriculation, exam);
            if (result.isPresent() && result.get().isPassed()) {
                OptionalWithMessage<Student> student = getStudent(matriculation);
                if (student.isPresent()) {
                    students.add(student.get());
                }
            }
        }
        return students;
    }

    public static OptionalWithMessage<Double> getAvg(Collection<Integer> nums) {
        double avg = nums.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(Double.NaN);
        if (Double.isNaN(avg)) {
            return OptionalWithMessage.ofMsg("Cannot calculate average of no values!");
        } else {
            return OptionalWithMessage.of(avg);
        }
    }


    public OptionalWithMessage<Double> getAvgAge(List<Integer> matriculations) {
        if (matriculations.isEmpty()) {
            return OptionalWithMessage.ofMsg("Cannot calculate average of no values!");
        }
        List<OptionalWithMessage<Integer>> ages = new ArrayList<>();
        for (int matriculation : matriculations) {
            OptionalWithMessage<Student> optStudent = getStudent(matriculation);
            OptionalWithMessage<Integer> optAge = optStudent.map(Student::getAge);
            ages.add(optAge);
        }
        List<String> errorMessages = new ArrayList<>();
        List<Integer> validAges = new ArrayList<>();
        for (int i = 0; i < ages.size(); i++) {
            OptionalWithMessage<Integer> optAge = ages.get(i);
            if (optAge.isPresent()) {
                validAges.add(optAge.get());
            } else {
                int matriculation = matriculations.get(i);
                errorMessages.add("No student with matriculation " + matriculation);
            }
        }
        if (!errorMessages.isEmpty()) {
            String message = String.join("\n", errorMessages);
            return OptionalWithMessage.ofMsg(message);}
        double sum = 0;
        for (int age : validAges) {
            sum += age;
        }
        double average = sum / validAges.size();
        return OptionalWithMessage.of(average);
    }
}
