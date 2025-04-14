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
    
    public Suspect(String name, int age, String socialStanding, String title, String personality, String backstory, String motivations, boolean isMurderer) {
        this.name = name;
        this.age = age;
        this.socialStanding = socialStanding;
        this.title = title;
        this.personality = personality;
        this.backstory = backstory;
        this.motivations = motivations;
        this.isMurderer = isMurderer;
    }
    
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSocialStanding() {
        return socialStanding;
    }

    public String getTitle() {
        return title;
    }

    public String getPersonality() {
        return personality;
    }

    public String getBackstory() {
        return backstory;
    }

    public String getMotivations() {
        return motivations;
    }

    
    public boolean isMurderer() {
        return isMurderer;
    }
    
    public void setMurderer(boolean isMurderer) {
        this.isMurderer = isMurderer;
    }
    
    @Override
    public String toString() {
        return "Suspect{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", socialStanding='" + socialStanding + '\'' +
                ", title='" + title + '\'' +
                ", personality='" + personality + '\'' +
                ", backstory='" + backstory + '\'' +
                ", motivations='" + motivations + '\'' +
                ", isMurderer=" + isMurderer +
                '}';
    }
}
