package builder;

import generated.Department;
import generated.Group;
import generated.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by anastasia on 01.11.16.
 */
public class XMLCreator {
    public static void writeIntoXMLFile(String fileName, Department studyDepartment){

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert documentBuilder != null;
        Document document = documentBuilder.newDocument();
        Element mainRootElement = document.createElement("department");
        document.appendChild(mainRootElement);

        for (Group group: studyDepartment.getGroups()) {
            Element rootElement = document.createElement("groups");

            rootElement.setAttribute("name", group.getName());
            rootElement.setAttribute("faculty", group.getFaculty());
            rootElement.setAttribute("course", Integer.toString(group.getCourse()));

            for (Student student : group.getStudents()) {
                Element elementStudent = createStudentElement(student, document);
                rootElement.appendChild(elementStudent);
            }

            mainRootElement.appendChild(rootElement);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileWriter(fileName));
            transformer.transform(source, result);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static Element createStudentElement(Student student, Document document) {
        Element elementStudent = document.createElement("students");

        elementStudent.setAttribute("id", Integer.toString(student.getId()));
        elementStudent.setAttribute("firstName", student.getFirstName());
        elementStudent.setAttribute("lastName", student.getLastName());
        elementStudent.setAttribute("address", student.getAddress());
        elementStudent.setAttribute("phoneNumber", student.getPhoneNumber());

        return elementStudent;
    }
}
