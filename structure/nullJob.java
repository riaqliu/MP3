package structure;

public class nullJob extends job {

    public nullJob() {
        super(0, 0, 0);
    }

    public nullJob(int start) {
        super(0, 0, 0);
        this.start = start;
    }

    public nullJob(int start, int size) {
        super(0, 0, size);
        this.start = start;
    }

    public void setSize(int size){
        this.size = size;
    }

    @Override
    public boolean use(){
        return false;
    }
    
    @Override
    public String toString(){
        return "Empty";
    }
}
