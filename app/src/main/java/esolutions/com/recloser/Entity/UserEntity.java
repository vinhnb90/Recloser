package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 2/15/2017.
 */

public class UserEntity {
    private final String UserName;
    private final String Password;
    private String FullName;
    private String Email;
    private String PhoneNumber;
    private final boolean Status;
    private String UsedApp;
    private final int UserType;
    private final boolean MobileVersion;

    private UserEntity(UserBuilder builder) {
        this.UserName = builder.UserName;
        this.Password = builder.Password;
        this.Status = builder.Status;
        this.UserType = builder.UserType;
        this.MobileVersion = builder.MobileVersion;
        this.FullName = (builder.FullName == null) ? "" : builder.FullName;
        this.Email = (builder.Email == null) ? "" : builder.Email;
        this.PhoneNumber = (builder.PhoneNumber == null) ? "" : builder.PhoneNumber;
        this.UsedApp = (builder.UsedApp == null) ? "" : builder.UsedApp;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getFullName() {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public boolean isStatus() {
        return Status;
    }

    public String getUsedApp() {
        return UsedApp;
    }

    public int getUserType() {
        return UserType;
    }

    public boolean isMobileVersion() {
        return MobileVersion;
    }

    public static class UserBuilder {
        private final String UserName;
        private final String Password;
        private String FullName;
        private String Email;
        private String PhoneNumber;
        private final boolean Status;
        private String UsedApp;
        private final int UserType;
        private final boolean MobileVersion;

        public UserBuilder(String userName, String password, boolean status, int userType, boolean mobileVersion) {
            UserName = userName;
            Password = password;
            Status = status;
            UserType = userType;
            MobileVersion = mobileVersion;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public void setPhoneNumber(String phoneNumber) {
            PhoneNumber = phoneNumber;
        }

        public void setUsedApp(String usedApp) {
            UsedApp = usedApp;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
