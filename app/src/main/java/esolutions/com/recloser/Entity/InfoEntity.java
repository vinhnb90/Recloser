package esolutions.com.recloser.Entity;

/**
 * Created by VinhNB on 3/10/2017.
 */

public class InfoEntity {
    private final String FullName;
    private final String Email;
    private final String PhoneNumber;

    public InfoEntity(InfoBuilder infoBuilder) {
        this.FullName = infoBuilder.name;
        this.Email = infoBuilder.phone;
        this.PhoneNumber = infoBuilder.email;
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

    private static class InfoBuilder {
        private final String name;
        private final String phone;
        private final String email;

        public InfoBuilder(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        public InfoEntity build() {
            return new InfoEntity(this);
        }
    }

}
