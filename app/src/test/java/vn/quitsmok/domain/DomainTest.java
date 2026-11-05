package vn.quitsmok.domain;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.*;
public class DomainTest {
    @Test public void stageBudgetsAndRounding(){int[] expected={20,16,12,8,5,3};for(int i=0;i<6;i++)assertEquals(expected[i],PlanMath.budget(20,i));assertEquals(18,PlanMath.budget(20,.5));}
    @Test public void zeroBaselineHasNoSmokingBudget(){assertEquals(0,PlanMath.budget(0,0));}
    @Test public void noAutomaticZeroAtFinalStage(){assertEquals(1,PlanMath.budget(1,5));}
    @Test public void missingDataHoldsEvenWithPositiveSelfReport(){assertEquals(SafetyGate.Decision.HOLD,SafetyGate.evaluate(false,false,false,SafetyGate.Answer.NO,SafetyGate.Answer.NO));}
    @Test public void compensationAndUnknownHold(){for(SafetyGate.Answer a:new SafetyGate.Answer[]{SafetyGate.Answer.YES,SafetyGate.Answer.UNKNOWN})assertEquals(SafetyGate.Decision.HOLD,SafetyGate.evaluate(false,false,true,a,SafetyGate.Answer.NO));}
    @Test public void disruptiveSymptomsHold(){assertEquals(SafetyGate.Decision.HOLD,SafetyGate.evaluate(false,false,true,SafetyGate.Answer.NO,SafetyGate.Answer.YES));}
    @Test public void urgentTakesPriority(){assertEquals(SafetyGate.Decision.URGENT_SUPPORT,SafetyGate.evaluate(true,true,false,SafetyGate.Answer.UNKNOWN,SafetyGate.Answer.UNKNOWN));}
    @Test public void specialistReviewBeforeProgress(){assertEquals(SafetyGate.Decision.REVIEW,SafetyGate.evaluate(false,true,true,SafetyGate.Answer.NO,SafetyGate.Answer.NO));}
    @Test public void continueRequiresAllInputs(){assertEquals(SafetyGate.Decision.CONTINUE,SafetyGate.evaluate(false,false,true,SafetyGate.Answer.NO,SafetyGate.Answer.NO));}
    @Test public void holdNeverAdvances(){assertEquals(2,PlanMath.advance(2,.2,1,false),0);assertEquals(2,PlanMath.advance(2,.2,0,true),0);}
    @Test public void noAccelerationToDeadline(){assertEquals(2.1,PlanMath.advance(2,.2,.5,true),.00001);}
    @Test(expected=IllegalArgumentException.class)public void cannotAccelerateBeyondBase(){PlanMath.advance(1,.2,2,true);}
    @Test(expected=IllegalArgumentException.class)public void noDivisionByZero(){PlanMath.intervalMinutes(960,0);}
    @Test(expected=IllegalArgumentException.class)public void noNanProgress(){PlanMath.budget(20,Double.NaN);}
    @Test public void midnightDoesNotCreateNewWakingDay(){ZoneId z=ZoneId.of("Asia/Ho_Chi_Minh");LocalTime wake=LocalTime.of(7,0);
        assertEquals(LocalDate.of(2026,11,4),JournalDay.at(Instant.parse("2026-11-04T18:00:00Z"),z,wake));
        assertEquals(LocalDate.of(2026,11,5),JournalDay.at(Instant.parse("2026-11-05T00:00:00Z"),z,wake));}
    @Test public void budgetNeverIncreasesAlongTrajectory(){for(int b=0;b<100;b++){int last=b;for(int i=0;i<=500;i++){int next=PlanMath.budget(b,i/100.0);assertTrue(next<=last);last=next;}}}
}
