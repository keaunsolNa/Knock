package org.zerock.knock.component.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class StringDateConvertLongTimeStamp {

    private static final Logger logger = LoggerFactory.getLogger(StringDateConvertLongTimeStamp.class);

    public long Converter(String dateString) {

        logger.info("[{}]", dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("KST"));
        long result = 0;

        try
        {
            Date date = dateFormat.parse(dateString);

            logger.info("[{}]", date);
            result = date.getTime();
            logger.info("[{}]", result);

        }
        catch (ParseException e)
        {
            logger.info("[{}]", e.getMessage());
        }

        return result;
    }


}
