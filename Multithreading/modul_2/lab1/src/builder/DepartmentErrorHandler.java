package builder;

import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by anastasia on 4/18/17.
 */
public class DepartmentErrorHandler extends DefaultHandler {
    private Logger logger = Logger.getLogger(DepartmentErrorHandler.class);

    public void warning(SAXParseException e){
        logger.warn(getLineAddress(e) + " - " + e.getMessage());
    }
    public void error(SAXParseException e){
        logger.error(getLineAddress(e) + " - " + e.getMessage());
    }
    public void fatalError(SAXParseException e){
        logger.fatal(getLineAddress(e) + " - " + e.getMessage());
    }
    private String getLineAddress(SAXParseException e){
        return e.getLineNumber() + ":" + e.getColumnNumber();
    }
}
