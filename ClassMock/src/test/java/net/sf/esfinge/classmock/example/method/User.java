package net.sf.esfinge.classmock.example.method;

public class User {

    private String roleName;

    private String userName;

    public User(final String userName, final String roleName) {

        super();
        this.userName = userName;
        this.roleName = roleName;
    }

    public String getUserName() {

        return this.userName;
    }

    public void setUserName(final String userName) {

        this.userName = userName;
    }

    public String getRoleName() {

        return this.roleName;
    }

    public void setRoleName(final String roleName) {

        this.roleName = roleName;
    }
}