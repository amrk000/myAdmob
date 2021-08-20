package amrk000.myadmob;

public class BannerModel {
    private String name;
    private int width;
    private int height;

    public BannerModel(String name,int width,int height){
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public BannerModel(){
        this.name = "Banner";
        this.width = 0;
        this.height = 0;
    }

    public BannerModel setName(String name) {
        this.name = name;
        return this;
    }

    public BannerModel setHeight(int height) {
        this.height = height;
        return this;
    }

    public BannerModel setWidth(int width) {
        this.width = width;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
