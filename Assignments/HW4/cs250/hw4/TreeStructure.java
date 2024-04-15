package cs250.hw4;

public interface TreeStructure {

    public void insert(Integer num);

    public Boolean remove(Integer num);

    public Long get(Integer num);

    public Integer findMaxDepth();

    public Integer findMinDepth();
    
}