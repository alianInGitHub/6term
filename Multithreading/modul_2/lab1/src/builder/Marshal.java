package builder;

import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.Scanner;

/**
 * Created by anastasia on 03.11.16.
 */
public class Marshal {
    public static String dataDirectory = "src/data/";
    private static String groupInfoFile = dataDirectory + "Group0/groupInfo";
    private static String studentBasicInfoFile = dataDirectory +"Group0/studentsInfo";
    private static String departmentXMLFile = "src/Department.xml";

    public static void main(String[] args) throws JAXBException, IOException {
        //generate start xml file
        Department studyDepartment = new Department();
        Group group = createGroupFromFile(groupInfoFile);
        addListOfStudentsFromFile(group, studentBasicInfoFile,4);
        studyDepartment.getGroups().add(group);
        createDepartmentXmlFile(departmentXMLFile, studyDepartment);
    }

    public static void createDepartmentXmlFile(String fileName, Department studyDepartment) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Department.class);
        Marshaller marshaller = context.createMarshaller();
        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(studyDepartment, new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Group createGroupFromFile(String groupInfoFile) throws IOException {
        Scanner groupReader = new Scanner(new FileReader(groupInfoFile));
        Group group = new Group();
        if (groupReader.hasNext()) {
            group.setCourse(Integer.valueOf(groupReader.next()));
        }
        if (groupReader.hasNext()) {
            group.setName(groupReader.next());
        }
        if (groupReader.hasNext()) {
            group.setFaculty(groupReader.next());
        }
        return group;
    }

    public static void addListOfStudentsFromFile(Group group, String basicInfoFile, int amountOfStudents) throws IOException {
        Scanner studentInfoScanner = new Scanner(new FileReader(basicInfoFile));

        for (int i = 0; i < amountOfStudents; i++) {
            Student student = createStudentFromFile(studentInfoScanner);
            student.setId(i);
            group.getStudents().add(student);
        }
        studentInfoScanner.close();
    }

    public static Student createStudentFromFile(Scanner studentInfoScanner) throws IOException {
        Student student = new Student();
        if (studentInfoScanner.hasNext()) {
            student.setLastName(studentInfoScanner.next());
        }
        if (studentInfoScanner.hasNext()) {
            student.setFirstName(studentInfoScanner.next());
        }
        if (studentInfoScanner.hasNext()) {
            student.setPhoneNumber(studentInfoScanner.next());
        }
        student.setAddress(studentInfoScanner.nextLine());

        return student;
    }
}
