package de.uniwue.jpp.exams;

import java.util.HashMap;
import java.util.Map;

public class ExamDatabaseUtil {

    private ExamDatabaseUtil() {}

    public static Map<Student, Map<Exam, ExamResult>> createSampleData() {
        Student max=new Student("Max Mustermann",123456,20);
        Student erika=new Student("Erika Musterfrau", 123123,23);
        Student juan = new Student("Juan Nadie", 124578, 27);

        // Create exams
        Exam pp = new Exam(2021, false, "Programmierpraktikum");
        Exam gdp = new Exam(2021, true, "Grundlagen der Programmierung");
        Exam swt = new Exam(2021, false, "Softwaretechnik");

        // Create exam results for each student and exam
        ExamResult maxPPResult = new ExamResult(90, 90, 45);
        ExamResult maxGdpResult = new ExamResult(60, 54, 30);
        ExamResult maxSwtResult = new ExamResult(60, 59, 30);

        ExamResult erikaPPResult = new ExamResult(90, 37, 45);
        ExamResult erikaGdpResult = new ExamResult(60, 33, 30);
        ExamResult erikaSwtResult = new ExamResult(60, 39, 30);

        ExamResult juanPPResult = new ExamResult(90, 63, 45);
        ExamResult juanGdpResult = new ExamResult(60, 5, 30);
        ExamResult juanSwtResult = new ExamResult(60, 51, 30);

        return new HashMap<>() {{
            put(max, new HashMap<>() {{
                put(pp, maxPPResult);
                put(gdp, maxGdpResult);
                put(swt, maxSwtResult);
            }});
            put(erika, new HashMap<>() {{
                put(pp, erikaPPResult);
                put(gdp, erikaGdpResult);
                put(swt, erikaSwtResult);
            }});
            put(juan, new HashMap<>() {{
                put(pp, juanPPResult);
                put(gdp, juanGdpResult);
                put(swt, juanSwtResult);
            }});
        }};

    }
}
