package ccr4ft3r.appetite.util;

import ccr4ft3r.appetite.ModConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {

    private static final Logger LOGGER = LogManager.getLogger(ModConstants.MOD_ID);

    public static Logger getLogger(){
        return LOGGER;
    }
}