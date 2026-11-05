package vn.quitsmok.domain;
/** Resolves daily totals without double-counting events or manufacturing timestamps. */
public final class DaySummary {
    public final int total;
    public final DataState state;
    public DaySummary(int observed, Integer reviewedTotal, int countAtReview, DataState reviewState) {
        if (observed < 0 || countAtReview < 0 || (reviewedTotal != null && reviewedTotal < countAtReview))
            throw new IllegalArgumentException("Invalid day totals");
        int delta = observed - countAtReview;
        total = reviewedTotal == null ? observed : Math.max(observed, reviewedTotal + delta);
        state = reviewState == null ? (observed == 0 ? DataState.UNKNOWN : DataState.OBSERVED)
                : (delta != 0 && reviewState != DataState.UNKNOWN ? DataState.OBSERVED : reviewState);
    }
}
