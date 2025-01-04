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

        if (dateString == null || dateString.isEmpty())
        {
            logger.error("[{}]", "parameter is null");
            return 0;
        }

        SimpleDateFormat dateFormat = dateString.length() == 7 ? new SimpleDateFormat("yyyy.MM") : new SimpleDateFormat("yyyy.MM.dd");
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
