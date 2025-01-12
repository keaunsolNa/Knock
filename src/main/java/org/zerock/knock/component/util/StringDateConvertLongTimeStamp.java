package org.zerock.knock.component.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author nks
 * @apiNote 여러 종류의 String Type 문자열을 받아 Date Parsing 한 후, epochTime 반환
 */
@Component
public class StringDateConvertLongTimeStamp {

    private static final Logger logger = LoggerFactory.getLogger(StringDateConvertLongTimeStamp.class);

    /**
     * String 형태의 문자열을 epochTime 반환
     * @param dateString 변환할 String
     * @return epochTime
     */
    public long Converter(String dateString) {

        if (dateString == null || dateString.isEmpty())
        {
            logger.error("[{}]", "parameter is null");
            return 0;
        }
        SimpleDateFormat dateFormat;
        switch (dateString.length())
        {
            case 4 : dateFormat = new SimpleDateFormat("yyyy"); break;
            case 6 : dateFormat = new SimpleDateFormat("yyyyMM"); break;
            case 7 : dateFormat = new SimpleDateFormat("yyyy.MM"); break;
            case 8 : dateFormat = new SimpleDateFormat("yyyyMMdd"); break;
            case 10 : dateFormat = new SimpleDateFormat("yyyy.MM.dd"); break;

            default:
                logger.error("[{}]", "parameter is Illegal : " + dateString + "\t " + dateString.length());

                return 0;
        }

        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("KST"));
        long result = 0;

        try
        {
            Date date = dateFormat.parse(dateString);
            result = date.getTime();
        }
        catch (ParseException e)
        {
            logger.info("[{}]", e.getMessage());
        }

        return result;
    }

    /**
     * epochTime 받아 yyyy.MM.dd 형태 문자열로 반환
     * @param time 변환할 epochTime
     * @return yyyy.MM.dd 문자열
     */
    public String Converter(long time)
    {

        if (time == 0)
        {
            logger.error("[{}]", "parameter is null");
            return "개봉 예정";
        }

        Instant instant = Instant.ofEpochMilli(time);

        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return date.format(formatter);

    }

}
