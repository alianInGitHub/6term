package core;

import generated.Department;
import generated.Group;
import generated.Student;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anastasia on 4/25/17.
 */
public class JDBCProcessor extends AbstractProcessor{
    private static final String URL = "jdbc:mysql://localhost:3306/department";
    private static final String URL_FIXED = URL + "?useUnicode=true&useSSL=false&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";


    private Connection connection;

    public JDBCProcessor() {
        try {
            createDatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDatabaseConnection() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);
        connection = DriverManager.getConnection(URL_FIXED, USER, PASSWORD);
        if (!connection.isClosed()) {
            System.out.println("Connection to database is established.");
        }
    }

    public void closeDatabase() {
        try {
            connection.close();
            if (connection.isClosed()) {
                System.out.println("Connection to database is closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean readDataFromFile(String fileName) {
        return false;
    }

    public Department getDepartment() {
        return null;
    }

    public List<Group> getGroups() {
        try {
            return readGroups();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Group> readGroups() throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM groups;");
        if (result.isClosed())
            return null;
        List<Group> groupList= new ArrayList<>(result.getFetchSize());
        while (result.next()) {
            Group group = readGroupFromDatabase(result);
            groupList.add(group);
        }
        statement.close();
        return groupList;
    }

    private Group readGroupFromDatabase(ResultSet result) throws SQLException {
        Group group = new Group();
        group.setName(result.getString("name"));
        group.setFaculty(result.getString("faculty"));
        group.setCourse(result.getInt("course"));
        List<Student> studentList = getStudentsFromGroup(group.getName());
        for (int i = 0; i < studentList.size(); i++) {
            group.getStudents().add(studentList.get(i));
        }
        return group;
    }

    public int getAmountOfGroups() {
        int amount = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT COUNT(*) FROM groups;"
            );
            amount = result.getInt(0);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public void checkValidData(Group group) throws Exception {
        Statement statement= connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM groups WHERE (name='" + group.getName() +
                "') AND (faculty='" + group.getFaculty() + "');");
        if (result.next()) {
            statement.close();
            throw new Exception("Group already exist");
        }
    }

    public void checkValidData(Student student) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(
                "SELECT * FROM students WHERE (firstname='" + student.getFirstName() + "') AND (lastname='"
                        + student.getLastName() + "') AND (address='" + student.getAddress() + "');"
        );
        if (result.next()) {
            statement.close();
            throw new Exception("Student already exist");
        }
        statement.close();
    }

    public void addGroup(Group group) throws Exception {
        checkValidData(group);
        Statement statement = connection.createStatement();
        statement.execute(
                "INSERT INTO groups (name, faculty, course) VALUES('" + group.getName() +
                        "', '" + group.getFaculty() + "', '" + group.getCourse() + "');"
        );
        statement.close();
    }

    public void addStudent(Student student, String groupName) throws Exception {
        checkValidData(student);
        int groupId = getGroupId(groupName);
        Statement statement = connection.createStatement();
        statement.execute(
                "INSERT INTO students (firstname, lastname, group_id, phone, address) VALUES ('" +
                        student.getFirstName() + "', '" + student.getLastName() + "', '" + groupId +
                        "', '" + student.getPhoneNumber() + "', '" + student.getAddress() + "');"
        );
        statement.close();
    }

    private int getGroupId(String groupName) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM groups WHERE name='" + groupName + "';");
        if (!result.next())
            throw new Exception("There is no such group in database");
        int id = result.getInt("id");
        statement.close();
        return id;
    }

    public void changeGroupAttributeValue(String groupName, String attributeName, String attributeValue) throws Exception {
        Statement statement = connection.createStatement();
        if (!isValidGroupAttribute(attributeName))
            throw new Exception("Cannot resolve attribute " + attributeName);
        statement.executeUpdate(
                "UPDATE groups SET " + attributeName + "='" + attributeValue + "' WHERE name='" + groupName +"';"
        );
        statement.close();
    }

    private boolean isValidGroupAttribute(String attributeName) {
        return attributeName.equals("faculty") || attributeName.equals("course") || attributeName.equals("name");
    }

    public void changeStudent(String groupName, int id, String attributeName, String attributeValue) throws Exception {
        Statement statement = connection.createStatement();
        if (!isValidStudentAttribute(attributeName)) {
            throw new Exception("Cannot resolve attribute " + attributeName);
        }
        if (attributeName.equals("group")) {
            attributeName = "group_id";
            attributeValue = Integer.toString(getGroupId(attributeValue));
        }
        statement.executeUpdate(
                "UPDATE students SET " + attributeName + "='" + attributeValue + "' WHERE id='" + id +"';"
        );
        statement.close();
    }

    private boolean isValidStudentAttribute(String attributeName) {
        attributeName = attributeName.toLowerCase();
        if (attributeName.equals("firstname") || attributeName.equals("lastname") ||
                attributeName.equals("address") || attributeName.equals("phone") || attributeName.equals("group"))
            return true;
        return false;
    }

    public Student getStudent(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM students WHERE id='" + id + "';");
            if (!result.next()) {
                statement.close();
                return null;
            }
            Student student = readStudentFromDatabase(result);
            statement.close();
            return student;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Student readStudentFromDatabase(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setFirstName(resultSet.getString("firstname"));
        student.setLastName(resultSet.getString("lastname"));
        student.setPhoneNumber(resultSet.getString("phone"));
        student.setAddress(resultSet.getString("address"));
        return student;
    }

    public boolean deleteGroup(String name) {
        try {
            Statement statement = connection.createStatement();
            System.out.println(name);
            statement.executeUpdate(
                    "DELETE FROM groups WHERE (name='" + name + "');"
            );
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteGroup(Group group) {
        return deleteGroup(group.getName());
    }

    public boolean deleteStudent(String groupName, int studentIndex) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM students WHERE (id='" + studentIndex + "');");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Group getGroup(String groupName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM groups WHERE name='" + groupName + "';");
            if (!result.next()) {
                statement.close();
                return null;
            }
            Group group = readGroupFromDatabase(result);
            statement.close();
            return group;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> getStudentsFromGroup(String groupName) {
        try {
            Statement statement = connection.createStatement();
            int groupId = getGroupId(groupName);
            ResultSet result = statement.executeQuery("SELECT * FROM students WHERE group_id='" + groupId + "';");
            List<Student> studentList = readStudents(result);
            statement.close();
            return studentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Student> readStudents(ResultSet result) throws SQLException {
        List<Student> studentList = new ArrayList<>(result.getFetchSize());
        while (result.next()) {
            Student student = readStudentFromDatabase(result);
            studentList.add(student);
        }
        return studentList;
    }

    public String studentToString(Student student) {
        return getStudentGroupName(student) + "\t" + super.studentToString(student);
    }

    private String getStudentGroupName(Student student) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT groups.name " +
                            "FROM groups INNER JOIN students ON groups.id = students.group_id " +
                            "WHERE (students.id='" + student.getId() + "');"
            );
            if (!result.next()) {
                statement.close();
                return null;
            }
            String name = result.getString("name");
            statement.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDatabase(String fileName) throws IOException {

    }
}
