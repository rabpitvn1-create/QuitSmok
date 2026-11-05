package vn.quitsmok.domain;
/** Behavioral routing only; not a diagnostic tool. Unknown answers never pass. */
public final class SafetyGate {
    public enum Answer { NO, YES, UNKNOWN }
    public enum Decision { CONTINUE, HOLD, REVIEW, URGENT_SUPPORT }
    public static Decision evaluate(boolean urgent, boolean specialistReview,
            boolean completeData, Answer compensation, Answer disruptiveSymptoms) {
        if (urgent) return Decision.URGENT_SUPPORT;
        if (specialistReview) return Decision.REVIEW;
        if (!completeData || compensation != Answer.NO || disruptiveSymptoms != Answer.NO)
            return Decision.HOLD;
        return Decision.CONTINUE;
    }
    private SafetyGate() {}
}
