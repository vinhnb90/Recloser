package esolutions.com.recloser.Entity;

import java.util.HashMap;

/**
 * Created by VinhNB on 2/15/2017.
 */

public class HeaderNavigationMenuEntity {
    private final String menuLv1;
    private final String keyMenuLv1;
    private HashMap<String, String> menuLv2;

    private HeaderNavigationMenuEntity(HeaderMenuBuilder builder) {
        this.keyMenuLv1 = builder.keyMenuLv1;
        this.menuLv1 = builder.menuLv1Nested;
        this.menuLv2 = builder.menuLv2Nested;
    }

    public String getKeyMenuLv1() {
        return keyMenuLv1;
    }

    public String getMenuLv1() {
        return menuLv1;
    }

    public HashMap<String, String> getMenuLv2() {
        return menuLv2;
    }

    public static class HeaderMenuBuilder {
        private final String menuLv1Nested;
        private final String keyMenuLv1;
        private HashMap<String, String> menuLv2Nested;

        public HeaderMenuBuilder( String keyMenuLv1, String menuLv1Nested) {
            this.menuLv1Nested = menuLv1Nested;
            this.keyMenuLv1 = keyMenuLv1;
        }

        public HeaderMenuBuilder setMenuLv2(HashMap<String, String> menuLv2Nested) {
            this.menuLv2Nested = menuLv2Nested;
            return this;
        }

        public HeaderMenuBuilder addElementMenuLv2(String key, String menuLv2) {
            if (this.menuLv2Nested == null)
                this.menuLv2Nested = new HashMap<String, String>();

            if (key == null || key.isEmpty())
                return this;

            if (menuLv2 == null || menuLv2.isEmpty())
                return this;
            this.menuLv2Nested.put(key, menuLv2);
            return this;
        }

        public HeaderMenuBuilder addSetMenuLv2(HashMap<String, String> setMenuLv2Nested) {
            if (this.menuLv2Nested == null)
                this.menuLv2Nested = new HashMap<String, String>();

            if (setMenuLv2Nested == null || setMenuLv2Nested.isEmpty())
                return this;

            this.menuLv2Nested.putAll(setMenuLv2Nested);
            return this;
        }

        public void remove(String keyMenuLv2) {
            if (this.menuLv2Nested == null) {
                this.menuLv2Nested = new HashMap<String, String>();
                return;
            }

            if (keyMenuLv2 == null || keyMenuLv2.isEmpty())
                return;

            for (String elementLv2 :
                    this.menuLv2Nested.keySet()) {
                if (elementLv2.equalsIgnoreCase(keyMenuLv2)) {
                    this.menuLv2Nested.remove(keyMenuLv2);
                }
            }
        }

        public HeaderNavigationMenuEntity build() {
            return new HeaderNavigationMenuEntity(this);
        }
    }
}
