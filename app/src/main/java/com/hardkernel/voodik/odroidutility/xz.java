package com.hardkernel.voodik.odroidutility;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class xz {



    {
        try {
            String ro_build_date_utc = "1541688153";
            long ro_build_date_utc_long = Long.parseLong(ro_build_date_utc);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date update_date = dateFormat.parse("20181108");
            long xz = update_date.getTime()/1000;
            System.out.println("Sum of x+y = " + ((xz - ro_build_date_utc_long)>86400?"need":"notneed"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
