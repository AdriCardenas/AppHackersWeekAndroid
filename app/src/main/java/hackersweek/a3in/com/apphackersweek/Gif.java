package hackersweek.a3in.com.apphackersweek;

/**
 * Created by Adrian on 26/02/2018.
 */


public class Gif {
    private String title;
    private String url;

    public Gif(){}

    public Gif(String title, String url){
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
