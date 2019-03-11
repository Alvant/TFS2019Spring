package ru.alexeev.tfs.qa.userdatagenerator.attribute;


import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class AttributeDate extends Attribute {

    private Calendar calendar = new GregorianCalendar();
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");

    public AttributeDate(Calendar calendar) {
        this.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public AttributeDate(String date, String dateFormat) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        this.calendar.setTime(simpleDateFormat.parse(date));
    }

    public AttributeDate(int year, int month, int dayOfMonth) {
        this.setDate(year, month, dayOfMonth);
    }

    @Override
    public String toString() {
        return this.dateFormat.format(this.calendar.getTime());
    };

    public Calendar getDate() {
        return this.calendar;
    }

    private void setDate(int year, int month, int dayOfMonth) {
        this.calendar.set(Calendar.YEAR, year);
        this.calendar.set(Calendar.MONTH, month);
        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
}
