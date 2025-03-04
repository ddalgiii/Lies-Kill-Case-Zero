package characters;

import java.util.List;
import java.util.Objects;

public class Suspect {
    private String name;
    @SuppressWarnings("unused")
    private String role;
    private String motive;
    private boolean guilty;
    private List<String> alibis;

    public Suspect(String name, String role, String motive){
        this.name=name;
        this.role=role;
        this.motive=motive;
        this.guilty=false;
    }

    public void setGuilty(boolean guilty){
        this.guilty=guilty;
    }
    public boolean isGuilty(){
        return guilty;
    }
    public String getAlibi(){
        return alibis.get((int)(Math.random()*alibis.size())); //Random alibi selection
    }
    public String getName(){
        return name;
    }
    public String getMotive(){
        return motive;
    }
     @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Suspect suspect = (Suspect) obj;
        return Objects.equals(name, suspect.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
