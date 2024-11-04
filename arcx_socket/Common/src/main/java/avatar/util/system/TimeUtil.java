package avatar.util.system;

import avatar.global.enumMsg.system.SearchTimeEnum;
import avatar.util.checkParams.ErrorDealUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**

 */
public class TimeUtil {

    /**

     * @param time
     * @return
     */
    public static Date strToDate(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(time);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return null;
    }

    /**

     */
    public static String dateToStr(Date time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    /**

     * @return
     */
    public static String getNowTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**

     * @param time
     * @return
     */
    public static long strToLong(String time){
        long retTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(!StrUtil.checkEmpty(time)) {
                Date dateTime = sdf.parse(time);
                retTime = dateTime.getTime();
            }
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @return
     */
    public static Date getTimeRange(int range){
        Calendar c = Calendar.getInstance();
        Date m = null;
        if(range == SearchTimeEnum.ONE_MONTH.getCode()){
            
            c.setTime(new Date());
            c.add(Calendar.MONTH, -SearchTimeEnum.ONE_MONTH.getMonth());
            m = c.getTime();
        }else if(range == SearchTimeEnum.THREE_MONTH.getCode()){
            
            c.setTime(new Date());
            c.add(Calendar.MONTH, -SearchTimeEnum.THREE_MONTH.getMonth());
            m = c.getTime();
        }else if(range == SearchTimeEnum.HALF_YEAR.getCode()){
            
            c.setTime(new Date());
            c.add(Calendar.MONTH, -SearchTimeEnum.HALF_YEAR.getMonth());
            m = c.getTime();
        }
        return m;
    }

    /**

     * @return
     */
    public static String getNowDay(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date());
    }

    /**

     * @param time
     * @return
     */
    public static String getTimeForDay(String time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String day = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            day = sdfDay.format(date);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return day;
    }

    /**

     * @param time
     * @return
     */
    public static String longToDay(long time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return sdfDay.format(date);
    }

    /**

     */
    public static String longToSimpleDay(long time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(time);
        return sdfDay.format(date);
    }

    /**

     * @param time
     * @return
     */
    public static String strToDay(String time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String str = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            str = sdfDay.format(date);
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return str;
    }

    /**

     * @param time
     * @return
     */
    public static String longToStr(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**

     * @param time
     * @return
     */
    public static String strToYear(String time){
        String year = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = sdf.parse(time);
            year=String.format("%tY", dateTime);
        }catch(Exception e){
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**

     * @returntest
     */
    public static long getNowTime(){
        return (new Date()).getTime();
    }

    /**

     * @return
     */
    public static String getNMonth(String time, int month){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date m = null;
        try {
            c.setTime(sdf.parse(time));
            c.add(Calendar.MONTH, month);
            m = c.getTime();
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return sdf.format(m);
    }

    
    public static Date getFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);



        Date date = calendar.getTime();
        return date;
    }

    /**

     * @return
     */
    public static Date repairTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date date = calendar.getTime();
        return date;
    }

    /**

     * @return
     */
    public static Date completeTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 00);
        Date date = calendar.getTime();
        return date;
    }

    public static Date getFirstDay1(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 48);
        calendar.set(Calendar.SECOND, 00);
        Date date = calendar.getTime();
        return date;
    }

    
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**

     * @param time
     * @return
     */
    public static int getTimeYear(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = 0;
        try {
            Date date = sdf.parse(time);
            year = Integer.parseInt(String.format("%tY", date));
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**

     * @param time
     * @return
     */
    public static int getTimeMonth(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = 0;
        try {
            Date date = sdf.parse(time);
            year = Integer.parseInt(String.format("%tm", date));
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**

     * @param time
     * @return
     */
    public static int getTimeDay(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = 0;
        try {
            Date date = sdf.parse(time);
            year = Integer.parseInt(String.format("%td", date));
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**

     * @return
     */
    public static int getTodayYear(){
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.YEAR);
        return day;
    }

    /**

     * @return
     */
    public static int getTodayMonth(){
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.MONTH)+1;
        return day;
    }

    /**

     * @return
     */
    public static int getTodayDate(){
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.DATE);
        return day;
    }

    /**

     * @return
     */
    public static String getTodayTime(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date())+" 00:00:00";
    }

    /**

     * @return
     */
    public static String getTodayDay(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date());
    }

    /**

     * @return
     */
    public static long getTodayLongTime(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            String time = sdfDay.format(new Date()) + " 00:00:00";
            retTime = sdf.parse(time).getTime();
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }


    /**

     * @return
     */
    public static int getTodatWeek(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**

     * @return
     */
    public static String getNearlyMonday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String retDay;
        c.setTime(new Date());
        int dayOfWeek = getTodatWeek();
        
        if(dayOfWeek==2){
            retDay = sdfDay.format(new Date())+" 00:00:00";
        }else{
            int diffWeek = 2-dayOfWeek;
            c.add(Calendar.DAY_OF_MONTH, diffWeek);
            Date date = c.getTime();
            retDay = sdfDay.format(date)+" 00:00:00";
        }
        return retDay;
    }

    /**

     * @param time
     * @return
     */
    public static long getNextDay(String time){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.DATE,1);
            Date retDate = c.getTime();
            retTime = retDate.getTime();
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @param time
     * @param hour
     * @return
     */
    public static long getNHour(String time, int hour) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,hour);
            Date retDate = c.getTime();
            retTime = retDate.getTime();
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @param time
     * @param hour
     * @return
     */
    public static String getBeforeNHour(String time, int hour) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retTime = "";
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,hour*-1);
            Date retDate = c.getTime();
            retTime = sdf.format(retDate);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @param time
     * @param hour
     * @return
     */
    public static String getDayBeforeNHour(String time, int hour) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retTime = "";
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,hour*-1);
            Date retDate = c.getTime();
            retTime = sdfDay.format(retDate);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @param time
     * @param minute
     * @return
     */
    public static String getDayBeforeNMinute(String time, int minute) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retTime = "";
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.MINUTE,minute*-1);
            Date retDate = c.getTime();
            retTime = sdf.format(retDate);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @param createTime
     * @return
     */
    public static long strToDayLongTime(String createTime) {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            Date newDate = sdfDay.parse(createTime);
            String newTime = sdfDay.format(newDate)+" 00:00:00";
            retTime = sdf.parse(newTime).getTime();
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**

     * @return
     */
    public static String billTime() {
        SimpleDateFormat hfbSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return hfbSimpleDateFormat.format(date);
    }

    /**

     */
    public static String loadPayTime(){
        
        Date date = new Date();
        long time = date.getTime();
        
        String dateline = time + "";
        dateline = dateline.substring(0, 10);
        return dateline;
    }
}
