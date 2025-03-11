package characters;

public class Suspect {
    private String name;
    private String role;
    private String motive;
    private boolean isGuilty;
    private String personality; //to define each NPCs personality YAY

    public Suspect(String name, String role, String motive, String personality) {
        this.name = name;
        this.role = role;
        this.motive = motive;
        this.personality = personality;
        this.isGuilty = false;
    }

    // Getters and setters for the fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public boolean isGuilty() {
        return isGuilty;
    }

    public void setGuilty(boolean isGuilty) {
        this.isGuilty = isGuilty;
    }
    
    public String getPersonality() {
        return personality;
    }

    public void setSalutation(String salutation) {
        // Implementation for setting salutation
    }
}
