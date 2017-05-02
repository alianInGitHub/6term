package validator;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

/**
 * Created by anastasia on 4/18/17.
 */
public class SAXValidator {
    public static boolean check(String xmlName, String xsdName){
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory factory = SchemaFactory.newInstance(language);
        File schemaLocation = new File(xsdName);
        try {
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlName);
            validator.validate(source);
            System.out.println(xmlName + " is valid!");
            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
