package de.uniwue.jpp.exams;


public class ExamResult {

    int maxPoints; int points; int requiredPoints;

    public ExamResult(int maxPoints, int points, int requiredPoints) {

        if (maxPoints<0 || points<0 || requiredPoints<0){
            throw new IllegalArgumentException("parameters cannot be negative");
        }
        if (points>maxPoints){
            throw new IllegalArgumentException("points cannot be greater than maxPoints!");
        }
        if (requiredPoints>maxPoints){
            throw new IllegalArgumentException("requiredPoints cannot be greater than maxPoints!");
        }

        this.maxPoints = maxPoints;
        this.points = points;
        this.requiredPoints = requiredPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getPoints() {
        return points;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public boolean isPassed() {
        return ((double) points / maxPoints) * 100 >= ((double) requiredPoints / maxPoints) * 100;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExamResult ");
        sb.append(points).append("/").append(maxPoints).append(" points ");
        if (isPassed()) {
            sb.append("(passed)");
        } else {
            sb.append("(not passed)");
        }
        return sb.toString();
    }
}
