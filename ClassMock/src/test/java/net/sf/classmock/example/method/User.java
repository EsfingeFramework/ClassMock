package net.sf.classmock.example.method;

public class User {
	
	private String roleName;
	private String userName;
	
	public User(String userName, String roleName) {
		super();
		this.userName = userName;
		this.roleName = roleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
