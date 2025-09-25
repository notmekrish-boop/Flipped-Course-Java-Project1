package edu.ccrm.domain;

public enum Grade {
    S(10, "Outstanding"), A(9, "Excellent"), B(8, "Very Good"), 
    C(7, "Good"), D(6, "Average"), E(5, "Pass"), F(0, "Fail");
    
    private final int points;
    private final String description;
    
    Grade(int points, String description) {
        this.points = points;
        this.description = description;
    }
    
    public int getPoints() { return points; }
    public String getDescription() { return description; }
    
    public static Grade fromScore(double score) {
        if (score >= 90) return S;
        if (score >= 80) return A;
        if (score >= 70) return B;
        if (score >= 60) return C;
        if (score >= 50) return D;
        if (score >= 40) return E;
        return F;
    }
}