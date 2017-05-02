package builder;

import generated.Department;
import generated.Group;
import generated.Student;
import validator.SAXValidator;

import java.io.*;
import java.util.*;

/**
 * Created by anastasia on 4/17/17.
 */
public class XMLProcessor {
    private Department studyDepartment;

    public XMLProcessor() {
        studyDepartment = new Department();
    }

    public XMLProcessor(String fileName) {
        readDataFromFile(fileName);
    }

    public boolean readDataFromFile(String fileName) {
        if (!SAXValidator.check(fileName, "src/generated/schema1.xsd"))
            return false;
        DOMBuilder builder = new DOMBuilder();
        builder.buildDepartment(fileName);
        studyDepartment = builder.getDepartment();
        return true;
    }

    public Department getDepartment() {
        return studyDepartment;
    }

    public List<Group> getGroups() {
        return studyDepartment.getGroups();
    }

    public int getAmountOfGroups() {
        return studyDepartment.getGroups().size();
    }

    public void addGroupFromFile() throws Exception {
        int groupNumberInList = getAmountOfGroups();
        String studentBasicInfoFile = Marshal.dataDirectory + "Group" + groupNumberInList + "/studentsInfo";
        String groupInfoFile = Marshal.dataDirectory + "Group" + groupNumberInList + "/groupInfo";
        Group group = Marshal.createGroupFromFile(groupInfoFile);
        checkValidData(group);
        Marshal.addListOfStudentsFromFile(group, studentBasicInfoFile, 4 + new Random().nextInt(2));
        studyDepartment.getGroups().add(group);
    }

    private void checkValidData(Group group) throws Exception {
        for (Group temp : studyDepartment.getGroups()) {
            if (Objects.equals(temp.getName(), group.getName())) {
                throw new Exception("ERROR:\tDuplicates in groups are not allowed.");
            }
        }
    }

    private void checkValidData(Student newStudent) throws Exception {
        for (Group group : studyDepartment.getGroups()) {
            for (Student student : group.getStudents()) {
                assertNotEqual(student, newStudent);
            }
        }
    }

    public void addGroup(String name, String faculty, int course) throws Exception {
        Group group = createGroup(name, faculty, course);
        addGroup(group);
    }

    public Group createGroup(String name, String faculty, int course) {
        Group group = new Group();
        group.setName(name);
        group.setFaculty(faculty);
        group.setCourse(course);
        return group;
    }

    public void addGroup(Group group) throws Exception {
        checkValidData(group);
        studyDepartment.getGroups().add(group);
    }

    public void addStudentFromFileToGroup(int numberOfGroup, String infoFile)
            throws Exception {
        Scanner studentInfoScanner = new Scanner(new FileReader(infoFile));
        Student student = Marshal.createStudentFromFile(studentInfoScanner);
        checkValidData(student);
        student.setId(studyDepartment.getGroups().get(numberOfGroup).getStudents().size());
        studyDepartment.getGroups().get(numberOfGroup).getStudents().add(student);
    }

    private void assertNotEqual(Student firstStudent, Student secondStudent) throws Exception {
        String duplicateError = "ERROR: duplicates in student are not allowed.";
        if (Objects.equals(firstStudent.getFirstName(), secondStudent.getFirstName()) &&
                Objects.equals(firstStudent.getLastName(), secondStudent.getLastName()) &&
                Objects.equals(firstStudent.getPhoneNumber(), secondStudent.getPhoneNumber())) {
            throw new Exception(duplicateError);
        }
    }

    public void changeGroupAttributeValue(String groupName, String attributeName, String attributeValue) throws Exception {
        int index = studyDepartment.getGroups().indexOf(getGroupByUniqueName(groupName));
        changeGroupAttributeValue(index, attributeName, attributeValue);
    }

    private void changeGroupAttributeValue(int numberOfGroup, String attributeName, String attributeValue)
            throws Exception {
        attributeName = attributeName.toLowerCase();
        if (Objects.equals(attributeName, Group.class.getDeclaredField("course").getName())) {
            studyDepartment.getGroups().get(numberOfGroup).setCourse(Integer.valueOf(attributeValue));
        } else if (Objects.equals(attributeName, "faculty")) {
            studyDepartment.getGroups().get(numberOfGroup).setFaculty(attributeValue);
        } else if (Objects.equals(attributeName, "name")) {
            changeGroupName(numberOfGroup, attributeValue);
        } else throw new NoSuchFieldException("There is no " + attributeName + " field in Group class.");
    }

    private void changeGroupName(int numberOfGroup, String newName) throws Exception {
        Group group = studyDepartment.getGroups().get(numberOfGroup);
        Group newGroup = new Group();
        newGroup.setName(newName);
        checkValidData(newGroup);
        group.setName(newName);
    }

    public void changeStudent(String groupName, int id, String attributeName, String attributeValue) throws Exception {
        Student student = getStudent(groupName, id);

        if (student == null)
            throw new NoSuchElementException("Student with this id does not exist");
        attributeName = attributeName.toLowerCase();
        if (attributeName.equals("group")) {
            moveStudentToGroup(groupName, attributeValue, student);
        } else {
            setStudentAttribute(student, attributeName, attributeValue);
        }
    }

    private void moveStudentToGroup(String currentGroupName, String newGroupName, Student student) throws Exception {
        deleteStudentByIndex(currentGroupName, student.getId());
        addStudent(student, newGroupName);
    }

    private void setStudentAttribute(Student student, String attribute, String value) throws Exception {
        if (attribute.equals("firstname")) {
            student.setFirstName(value);
        } else if (attribute.equals("lastname")) {
            student.setLastName(value);
        } else if (attribute.equals("address")) {
            student.setAddress(value);
        } else if (attribute.equals("phonenumber")) {
            student.setPhoneNumber(value);
        } else if (attribute.equals("id")) {
            student.setId(Integer.parseInt(value));
            checkValidData(student);
        } else throw new NoSuchElementException("Field does not exist");
    }

    private Student getStudent(String groupName, int id) {
        Group group = getGroupByUniqueName(groupName);
        for (int i = 0; i < group.getStudents().size(); i++) {
            if (group.getStudents().get(i).getId() == id) {
                return group.getStudents().get(i);
            }
        }
        return null;
    }

    public boolean deleteGroup(String name) {
        Group group = getGroupByUniqueName(name);
        return deleteGroup(group);
    }

    public boolean deleteGroup(Group group) {
        return studyDepartment.getGroups().remove(group);
    }

    public boolean deleteStudentByIndex(String groupName, int studentIndex) {
        Group group = getGroupByUniqueName(groupName);
        int groupIndex = studyDepartment.getGroups().indexOf(group);
        return studyDepartment.getGroups().get(groupIndex).getStudents().remove(studentIndex) != null;
    }

    public Group getGroupByUniqueName(String groupName) {
        for (Group group : studyDepartment.getGroups()) {
            if (Objects.equals(group.getName(), groupName)) {
                return group;
            }
        }
        return null;
    }

    public List<Student> getStudentsFromGroupByUniqueName(String groupName) {
        Group group = getGroupByUniqueName(groupName);
        if (group != null) {
            return group.getStudents();
        }
        return null;
    }

    public String groupToString(Group group) {
        return group.getName() + "\t" + group.getFaculty() + "\t" + group.getCourse();
    }

    public String studentToString(Student student) {
        return student.getId() + " "
                + student.getFirstName() + " "
                + student.getLastName() + " "
                + student.getPhoneNumber() + " "
                + student.getAddress();
    }

    public void saveToFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Group group : studyDepartment.getGroups()) {
            writer.write(groupToString(group) + "\n");
            for (Student student : group.getStudents()) {
                writer.write("\t" + studentToString(student) + "\n");
            }
            writer.write("\n");
        }
        writer.close();
    }

    public void writeToFile(Group group, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(groupToString(group) + "\n");
        writer.close();
    }

    public void writeToFile(List<Student> students, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Student student : students) {
            writer.write(studentToString(student) + "\n");
        }
        writer.close();
    }

    public void addStudent(String groupName, String firstName, String lastName, String address, String phoneNumber) throws Exception {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setAddress(address);
        student.setPhoneNumber(phoneNumber);

        addStudent(student, groupName);
    }

    public void addStudent(Student student, String groupName) throws Exception {
        checkValidData(student);
        int id = getGroupByUniqueName(groupName).getStudents().size();
        student.setId(id);
        getGroupByUniqueName(groupName).getStudents().add(student);
    }
}
