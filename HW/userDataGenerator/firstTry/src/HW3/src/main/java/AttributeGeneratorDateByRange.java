import java.util.Calendar;
import java.util.GregorianCalendar;


public class AttributeGeneratorDateByRange extends AttributeGenerator {

    private Calendar edgeLeft = new GregorianCalendar();
    private Calendar edgeRight = new GregorianCalendar();

    private double ageMaxPopulation = 45;  // imho
    private double yearGaussianMean = new GregorianCalendar().get(Calendar.YEAR) - this.ageMaxPopulation;
    private double yearGaussianStd = 20;   // chosen after a couple of experiments

    private int monthUpperBound = Calendar.DECEMBER + 1;

    public AttributeGeneratorDateByRange() { super(); }

    @Override
    public void initializeRange(Object edgeLeft, Object edgeRight) {
        this.initializeLeftEdge((Calendar) edgeLeft);
        this.initializeRightEdge((Calendar) edgeRight);
    }

    @Override
    public Object generate() {
        int year = this.getBoundedGaussianValueForYear();
        int month = Utils.randomInt(this.monthUpperBound);
        int dayOfMonth = Utils.randomInt(this.getDayOfMonthUpperBound(month));

        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        AttributeDate result = new AttributeDate(calendar);

        return (Object) result;
    }

    private void initializeLeftEdge(Calendar other) {
        this.initializeEdge(this.edgeLeft, other);
    }

    private void initializeRightEdge(Calendar other) {
        this.initializeEdge(this.edgeRight, other);
    }

    private void initializeEdge(Calendar calendarToBeInitialized, Calendar other) {
        calendarToBeInitialized.set(Calendar.YEAR, other.get(Calendar.YEAR));
        calendarToBeInitialized.set(Calendar.MONTH, other.get(Calendar.MONTH));
        calendarToBeInitialized.set(Calendar.DAY_OF_MONTH, other.get(Calendar.DAY_OF_MONTH));
    }

    private int getBoundedGaussianValueForYear() {
        int yearField = Calendar.YEAR;

        double yearDouble = Utils.randomGaussian(this.yearGaussianMean, this.yearGaussianStd);
        int yearInt = (int) yearDouble;

        int yearBoundLower = this.edgeLeft.get(yearField);
        int yearBoundUpper = this.edgeRight.get(yearField);

        if (yearInt < yearBoundLower) {
            return yearBoundLower;
        } else if (yearInt >= yearBoundUpper) {
            return yearBoundUpper - 1;
        } else {
            return yearInt;
        }
    }

    private int getDayOfMonthUpperBound(int month) {
        if (month == Calendar.FEBRUARY) {
            return 29;  // 29 -- don't bother
        } else {
            return 30;  // same for 31
        }
    }
}
