package vn.quitsmok.domain;
/** Experimental mathematics. Not activated as a treatment schedule in v0.1. */
public final class PlanMath {
    private static final double[] RATIOS = {1, .8, .6, .4, .25, .125};
    public static int budget(int baseline, double progress) {
        if (baseline < 0 || !Double.isFinite(progress) || progress < 0 || progress > 5)
            throw new IllegalArgumentException("Invalid baseline or progress");
        int k = Math.min((int) progress, 4);
        double fraction = progress - k;
        double ratio = RATIOS[k] + fraction * (RATIOS[k+1] - RATIOS[k]);
        return (int) Math.ceil(baseline * ratio);
    }
    public static double advance(double progress, double dailyStep, double lambda, boolean gatePassed) {
        if (!Double.isFinite(progress) || progress < 0 || progress > 5 || !Double.isFinite(dailyStep)
                || dailyStep < 0 || !Double.isFinite(lambda) || lambda < 0 || lambda > 1)
            throw new IllegalArgumentException("Invalid progression");
        return gatePassed ? Math.min(5, progress + dailyStep * lambda) : progress;
    }
    public static double intervalMinutes(int awakeMinutes, int budget) {
        if (awakeMinutes <= 0 || awakeMinutes > 1440 || budget <= 0)
            throw new IllegalArgumentException("Positive waking window and budget required");
        return (double) awakeMinutes / budget;
    }
    private PlanMath() {}
}
