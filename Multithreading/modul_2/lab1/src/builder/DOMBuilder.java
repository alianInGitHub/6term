package builder;

import generated.Department;
import generated.Group;
import generated.Student;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by anastasia on 4/17/17.
 */
public class DOMBuilder {
    private DocumentBuilder builder;
    private Department studyDepartment;

    public DOMBuilder() {
        studyDepartment = new Department();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void buildDepartment(String filename) {
        Document document = null;
        try {
            document = builder.parse(filename);
            Element root = document.getDocumentElement();
            NodeList groupNodes = root.getElementsByTagName(Department.class.getDeclaredField("groups").getName());
            for(int i = 0; i < groupNodes.getLength(); i++) {
                Group group = buildGroup(groupNodes.item(i));
                studyDepartment.getGroups().add(group);
            }
        } catch (SAXException | IOException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public Group buildGroup(Node groupNode) throws NoSuchFieldException {
        Group group = new Group();

        NamedNodeMap attributes = groupNode.getAttributes();
        group.setName(attributes.getNamedItem(Group.class.getDeclaredField("name").getName()).getTextContent());
        group.setFaculty(attributes.getNamedItem(Group.class.getDeclaredField("faculty").getName()).getTextContent());
        group.setCourse(Integer.valueOf(attributes.getNamedItem(Group.class.getDeclaredField("course").getName()).getTextContent()));

        NodeList studentNodes = ((Element)groupNode).getElementsByTagName(Group.class.getDeclaredField("students").getName());
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Student student = buildStudent(studentNodes.item(i));
            group.getStudents().add(student);
        }
        return  group;
    }

    public Student buildStudent(Node studentNode) throws NoSuchFieldException {
        Student student = new Student();
        NamedNodeMap attributes = studentNode.getAttributes();
        student.setId(Integer.valueOf(attributes.getNamedItem(Student.class.getDeclaredField("id").getName()).getTextContent()));
        student.setFirstName(attributes.getNamedItem(Student.class.getDeclaredField("firstName").getName()).getTextContent());
        student.setLastName(attributes.getNamedItem(Student.class.getDeclaredField("lastName").getName()).getTextContent());
        student.setAddress(attributes.getNamedItem(Student.class.getDeclaredField("address").getName()).getTextContent());
        student.setPhoneNumber(attributes.getNamedItem(Student.class.getDeclaredField("phoneNumber").getName()).getTextContent());
        return student;
    }
    public Department getDepartment() { return studyDepartment; }

}
