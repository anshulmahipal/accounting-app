package common.model.entity;

import common.model.BaseEntity;

public class Product extends BaseEntity
{
    private String name;
    private double cost;
    private String group;

    public Product(int id)
    {
        super(id);
    }

    public Product(String name, double cost, String group)
    {
        this.name = name;
        this.cost = cost;
        this.group = group;
    }

    public Product(int id, String name, double cost, String group)
    {
        super(id);
        this.name = name;
        this.cost = cost;
        this.group = group;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getCost()
    {
        return cost;
    }

    public void setCost(double cost)
    {
        this.cost = cost;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        return obj instanceof Product && obj.hashCode() == id;
    }

    @Override
    public String toString()
    {
        return "Product [productID="+id+", productName="+name+", productGroup="+group+", productCost="+cost+"]";
    }
}
