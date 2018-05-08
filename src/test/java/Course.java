import java.util.ArrayList;
import java.util.List;

public class Course {
    String name ;
    String id ;
    ArrayList<String> sessions ;

    public Course() {}

    public Course(String name, String id) {
        this.name = name;
        this.id   = id  ;
        sessions = new ArrayList<>();
    }

    public void setSessions(ArrayList<String> sessions) {
        this.sessions = sessions;
    }

    public void addSession(String session) {
        this.sessions.add(session);
    }

    public String getName() {
        System.out.print("name : "+ this.name+"\n");
        return name;
    }

    public String getId() {
        System.out.print("ID : "+this.id+"  ");
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(String id){
        this.id = id;
    }

    public ArrayList<String> getSessions() {
        return sessions;
    }
    public boolean addToCurrentAndAssess(String id, String name, ArrayList<String> addedNames,ArrayList<String> addedIds){
        if (addedNames.contains(name) || addedIds.contains(id)){
            System.out.print(id+" | "+ name+ " False Already in the list \n");
            return false;
        }
        else {
            System.out.print(id+" | "+ name+ " True could be added \n");
            addedNames.add(name);
            addedIds.add(id);
            return true;
        }
    }

}
