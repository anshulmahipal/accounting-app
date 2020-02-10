package common.model.entity;

import common.model.BaseEntity;

public class Contractor extends BaseEntity
{
    private String name, adress, phone, requisites, type;

    public Contractor(String name, String adress, String phone, String requisites, String type)
    {
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.requisites = requisites;
        this.type = type;
    }

    public Contractor(int id, String name, String adress, String phone, String requisites, String type)
    {
        super(id);
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.requisites = requisites;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAdress()
    {
        return adress;
    }

    public void setAdress(String adress)
    {
        this.adress = adress;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getRequisites()
    {
        return requisites;
    }

    public void setRequisites(String requisites)
    {
        this.requisites = requisites;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }


    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        return obj instanceof Contractor && obj.hashCode() == id;
    }

    @Override
    public String toString()
    {
        return "Contractor [contractorID="+id+", contractorName="+name+", contractorAdress="+adress+", contractorPhone="+phone
                +", contractorRequisites="+requisites+", contractorType="+type+"]";
    }
}
