package common.util.response;

import common.model.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;


public class Response implements Serializable
{
    private Notification notification;

    private BaseEntity entityData;

    private ArrayList<BaseEntity> entitiesData;

    public Response(Notification notification)
    {
        this.notification = notification;
    }

    public Response(Notification notification, BaseEntity entityData)
    {
        this.notification = notification;
        this.entityData = entityData;
    }

    public Response(Notification notification, ArrayList<BaseEntity> entitiesData)
    {
        this.notification = notification;
        this.entitiesData = entitiesData;
    }

    public Notification getNotification()
    {
        return notification;
    }

    public void setNotification(Notification notification)
    {
        this.notification = notification;
    }

    public BaseEntity getEntityData()
    {
        return entityData;
    }

    public void setEntityData(BaseEntity entityData)
    {
        this.entityData = entityData;
    }

    public ArrayList<BaseEntity> getEntitiesData()
    {
        return entitiesData;
    }

    public void setEntitiesData(ArrayList<BaseEntity> entitiesData)
    {
        this.entitiesData = entitiesData;
    }
}
