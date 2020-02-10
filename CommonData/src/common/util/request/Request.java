package common.util.request;

import common.model.BaseEntity;

import java.io.Serializable;

public class Request implements Serializable
{
    private BaseEntity entityData;
    private Command command;

    public Request(Command command)
    {
        this.command = command;
        entityData = null;
    }

    public Request(Command command, BaseEntity entityData)
    {
        this.command = command;
        this.entityData = entityData; //clone constructor?
    }

    public BaseEntity getEntityData()
    {
        return entityData;
    }

    public void setEntityData(BaseEntity entityData)
    {
        this.entityData = entityData;
    }

    public Command getCommand()
    {
        return command;
    }

    public void setCommand(Command command)
    {
        this.command = command;
    }
}
