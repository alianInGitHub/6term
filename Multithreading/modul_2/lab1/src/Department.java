import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by anastasia on 4/17/17.
 */

@XmlRootElement
@XmlType(name = "department", propOrder = "groups")
public class Department {
    @XmlElement(name = "groups")
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    @XmlRootElement(name = "group")
    private class Group {
        @XmlID
        @XmlAttribute
        private String name;
        @XmlAttribute
        private String faculty;
        @XmlAttribute
        private int course;
        @XmlElement
        private List<Student> students;

        @XmlRootElement(name = "student")
        private class Student {
            @XmlAttribute(required = true)
            private int id;
            @XmlAttribute
            private String firstName;
            @XmlAttribute
            private String lastName;
            @XmlAttribute
            private String address;
            @XmlAttribute
            private String phoneNumber;
        }
    }
}
