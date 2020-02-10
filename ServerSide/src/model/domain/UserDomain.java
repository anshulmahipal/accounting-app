package model.domain;

import common.model.entity.User;
import common.util.response.Notification;
import common.util.response.Response;
import model.mapper.UserDataMapper;

public class UserDomain
{
    public static Response login(User user)
    {
        User authUser = UserDataMapper.find(user.getEmail(),user.getPassword());
        if(authUser == null)
        {
            return new Response(Notification.LOGIN_ERROR);
        }
        else
        {
            return new Response(Notification.LOGIN_SUCCESS, authUser);
        }
    }

    public static Response register(User user)
    {
        User userDuplicate = UserDataMapper.find(user.getEmail());

        if(userDuplicate!=null)
            return new Response(Notification.REGISTER_ERROR);


        int rowsAffected = UserDataMapper.add(user);

        if(rowsAffected==-1)
            return new Response(Notification.REGISTER_ERROR);
        else
            return new Response(Notification.REGISTER_SUCCESS);
    }

    public static Response updateUser(User user)
    {
        User userDuplicate = UserDataMapper.find(user.getEmail());

        if(userDuplicate!=null && userDuplicate.getId()!=user.getId())
            return new Response(Notification.UPDATE_ERROR);


        int rowsAffected = UserDataMapper.update(user);

        if(rowsAffected ==-1)
            return new Response(Notification.UPDATE_ERROR);
        else
            return new Response(Notification.UPDATE_SUCCESS);
    }

    public static Response removeUser(User user)
    {
        int rowsAffected = UserDataMapper.delete(user);

        if(rowsAffected == -1)
            return new Response(Notification.REMOVE_ERROR);
        else
            return new Response(Notification.REMOVE_SUCCESS);
    }

    private UserDomain(){};
}
