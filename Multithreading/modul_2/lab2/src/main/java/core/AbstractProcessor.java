package core;

import generated.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by anastasia on 4/25/17.
 */
public abstract class AbstractProcessor {
    public abstract boolean readDataFromFile(String fileName);

    public abstract Department getDepartment();

    public abstract List<Group> getGroups();

    public abstract int getAmountOfGroups();

    public abstract void checkValidData(Group group) throws Exception;

    public abstract void checkValidData(Student newStudent) throws Exception;

    public Group createGroup(String name, String faculty, int course) {
        Group group = new Group();
        group.setName(name);
        group.setFaculty(faculty);
        group.setCourse(course);
        return group;
    }

    public Student createStudent(String firstName, String lastName, String address, String phoneNumber) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setAddress(address);
        student.setPhoneNumber(phoneNumber);
        return student;
    }

    public abstract void addGroup(Group group) throws Exception;

    public abstract void addStudent(Student student, String groupName) throws Exception;

    private void assertNotEqual(Student firstStudent, Student secondStudent) throws Exception {
        String duplicateError = "ERROR: duplicates in student are not allowed.";
        if (firstStudent.getFirstName().equals(secondStudent.getFirstName()) &&
                firstStudent.getLastName().equals(secondStudent.getLastName()) &&
                firstStudent.getPhoneNumber().equals(secondStudent.getPhoneNumber())) {
            throw new Exception(duplicateError);
        }
    }

    public abstract void changeGroupAttributeValue(String groupName, String attributeName, String attributeValue) throws Exception;

    public abstract void changeStudent(String groupName, int id, String attributeName, String attributeValue) throws Exception;

    public abstract Student getStudent(int id);

    public abstract boolean deleteGroup(String name);

    public abstract boolean deleteGroup(Group group);

    public abstract boolean deleteStudent(String groupName, int studentIndex);

    public abstract Group getGroup(String groupName);

    public abstract List<Student> getStudentsFromGroup(String groupName);

    public String groupToString(Group group) {
        String info = group.getName() + "\t" + group.getFaculty() + "\t" + group.getCourse() + "\n";
        for (int i = 0; i < group.getStudents().size(); i++) {
            info += "\t" + studentToString(group.getStudents().get(i));
        }
        return info;
    }

    public  String studentToString(Student student) {
        return student.getId() + "\t" + student.getFirstName() + "\t" + student.getLastName() + "\t" +
                student.getPhoneNumber() + "\t" + student.getAddress() + "\n";
    }

    public abstract void saveDatabase(String fileName) throws IOException;

}
