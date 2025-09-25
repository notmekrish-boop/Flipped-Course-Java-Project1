package edu.ccrm.domain;
public class MaxCreditLimitExceededException extends RuntimeException {
    private final int currentCredits, attemptedCredits, maxAllowed;
    public MaxCreditLimitExceededException(int current, int attempted, int max) {
        super(String.format("Credit limit exceeded: Current=%d, Attempted=%d, Max=%d", current, attempted, max));
        this.currentCredits = current; this.attemptedCredits = attempted; this.maxAllowed = max;
    }
    public int getCurrentCredits() { return currentCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
    public int getMaxAllowed() { return maxAllowed; }
}
