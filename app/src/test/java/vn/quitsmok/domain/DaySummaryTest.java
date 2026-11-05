package vn.quitsmok.domain;
import org.junit.Test;
import static org.junit.Assert.*;
public class DaySummaryTest {
    @Test public void absentDayIsUnknownNotAbstinence(){DaySummary s=new DaySummary(0,null,0,null);assertEquals(DataState.UNKNOWN,s.state);}
    @Test public void confirmedZeroMustBeExplicit(){assertEquals(DataState.CONFIRMED,new DaySummary(0,0,0,DataState.CONFIRMED).state);}
    @Test public void dailyTotalDoesNotDoubleCountLogs(){assertEquals(10,new DaySummary(4,10,4,DataState.CONFIRMED).total);}
    @Test public void laterLogPreservesAggregateAndReopensReview(){DaySummary s=new DaySummary(5,10,4,DataState.CONFIRMED);assertEquals(11,s.total);assertEquals(DataState.OBSERVED,s.state);}
    @Test public void undoDoesNotEraseUnloggedAggregate(){assertEquals(9,new DaySummary(3,10,4,DataState.CONFIRMED).total);}
    @Test public void unknownRemainsUnknownAfterLogging(){DaySummary s=new DaySummary(5,null,4,DataState.UNKNOWN);assertEquals(5,s.total);assertEquals(DataState.UNKNOWN,s.state);}
    @Test public void recallIsNotConfirmation(){assertEquals(DataState.RECALLED,new DaySummary(0,8,0,DataState.RECALLED).state);}
    @Test(expected=IllegalArgumentException.class)public void contradictoryTotalsRejected(){new DaySummary(4,2,4,DataState.CONFIRMED);}
}
