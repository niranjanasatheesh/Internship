package hello;

public class Greeting {
    private final long id;
    private final String url;

    public Greeting(long id , String url){
        this.id = id;
        this.url = url;
    }
    public long getId(){
        return id;
    }
    public String geturl(){
        return url;
    }
}
