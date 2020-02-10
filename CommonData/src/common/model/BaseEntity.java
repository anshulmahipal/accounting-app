package common.model;

import java.io.Serializable;

public class BaseEntity implements Serializable
{
    protected int id;

    public BaseEntity() {};

    public BaseEntity(int id)
    {
        this.id = id;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    @Override
    public int hashCode()
    {
        return id;
    }
}
