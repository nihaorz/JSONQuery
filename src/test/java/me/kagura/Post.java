package me.kagura;

public class Post {
    public long tid;
    public long pid;
    public long cid;

    @Override
    public String toString() {
        return "Post{" +
                "tid=" + tid +
                ", pid=" + pid +
                ", cid=" + cid +
                '}';
    }
}
