package common.model.entity;

import common.model.BaseEntity;

public class User extends BaseEntity
{
    private String email, password, surname, name, patronymic, workplace, city, phone;
    private int tax,adding;
    private boolean isAdmin;

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, boolean isAdmin)
    {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(int userID, String email, String password, boolean isAdmin) //minimum required info
    {

        super(userID);
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }


    public User(User user)
    {
       this.id = user.getId();
       this.email = user.getEmail();
       this.password = user.getPassword();
       this.surname = user.getSurname();
       this.name = user.getName();
       this.patronymic = user.getPatronymic();
       this.workplace = user.getWorkplace();
       this.city = user.getCity();
       this.phone = user.getPhone();
       this.tax = user.getTax();
       this.adding = user.getAdding();
       this.isAdmin = user.isAdmin();

    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public void setAdmin(boolean admin)
    {
        isAdmin = admin;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPatronymic()
    {
        return patronymic;
    }

    public void setPatronymic(String patronymic)
    {
        this.patronymic = patronymic;
    }

    public String getWorkplace()
    {
        return workplace;
    }

    public void setWorkplace(String workplace)
    {
        this.workplace = workplace;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public int getTax()
    {
        return tax;
    }

    public void setTax(int tax)
    {
        this.tax = tax;
    }

    public int getAdding()
    {
        return adding;
    }

    public void setAdding(int adding)
    {
        this.adding = adding;
    }

    @Override
    public String toString()
    {
        return "User [userID="+ id+", password="+password+", email="+email+", surname="+surname+", name="+name
                +", patronymic="+patronymic+", workplace="+workplace+", city="+city+", phone="+phone+", isAdmin="+isAdmin+"]";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        return obj instanceof User && obj.hashCode() == id;
    }
}
