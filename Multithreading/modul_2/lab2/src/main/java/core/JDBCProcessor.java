package core;

import generated.Department;
import generated.Group;
import generated.Student;

import java.io.IOException;
import java.util.List;

/**
 * Created by anastasia on 4/25/17.
 */
public class JDBCProcessor extends AbstractProcessor{
    public boolean readDataFromFile(String fileName) {
        return false;
    }

    public Department getDepartment() {
        return null;
    }

    public List<Group> getGroups() {
        return null;
    }

    public int getAmountOfGroups() {
        return 0;
    }

    public void checkValidData(Group group) throws Exception {

    }

    public void checkValidData(Student newStudent) throws Exception {

    }

    public void addGroup(Group group) throws Exception {

    }

    public void addStudent(Student student, String groupName) throws Exception {

    }

    public void changeGroupAttributeValue(String groupName, String attributeName, String attributeValue) throws Exception {

    }

    public void changeStudent(String groupName, int id, String attributeName, String attributeValue) throws Exception {

    }

    public Student getStudent(String groupName, int id) {
        return null;
    }

    public boolean deleteGroup(String name) {
        return false;
    }

    public boolean deleteGroup(Group group) {
        return false;
    }

    public boolean deleteStudent(String groupName, int studentIndex) {
        return false;
    }

    public Group getGroup(String groupName) {
        return null;
    }

    public List<Student> getStudentsFromGroup(String groupName) {
        return null;
    }

    public String groupToString(Group group) {
        return null;
    }

    public String studentToString(Student student) {
        return null;
    }

    public void saveDatabase(String fileName) throws IOException {

    }
}
