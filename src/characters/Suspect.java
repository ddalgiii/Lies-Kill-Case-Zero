package characters;

public class Suspect {
    private final String name;
    private final int age;
    private final String heritage;
    private final String role;
    private final String personality;
    private final String background;
    private final String relationships;
    private boolean isMurderer;
    
    public Suspect(String name, int age, String heritage, String role, String personality, String background, String relationships, boolean isMurderer) {
        this.name = name;
        this.age = age;
        this.heritage = heritage;
        this.role = role;
        this.personality = personality;
        this.background = background;
        this.relationships = relationships;
        this.isMurderer = isMurderer;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isMurderer() {
        return isMurderer;
    }
    
    public void setMurderer(boolean isMurderer) {
        this.isMurderer = isMurderer;
    }
    
    @Override
    public String toString() {
        return "Name: " + name + "\n" +
               "Age: " + age + "\n" +
               "Heritage: " + heritage + "\n" +
               "Role: " + role + "\n" +
               "Personality: " + personality + "\n" +
               "Background: " + background + "\n" +
               "Relationships: " + relationships + "\n" +
               "Murderer: " + (isMurderer ? "Yes" : "No") + "\n";
    }
}
