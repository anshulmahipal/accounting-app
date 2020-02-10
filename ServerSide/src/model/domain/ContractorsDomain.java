package model.domain;

import common.model.BaseEntity;
import common.model.entity.Contractor;
import common.util.response.Notification;
import common.util.response.Response;
import model.mapper.ContractorDataMapper;

import java.util.ArrayList;

public class ContractorsDomain
{
    public static Response getContractorsList()
    {
        ArrayList<BaseEntity> entities = ContractorDataMapper.findAll();

        if(entities.isEmpty())
            return new Response(Notification.GET_ERROR);
        else
            return new Response(Notification.GET_SUCCESS,entities);
    }

    public static Response updateContractor(Contractor contractor)
    {
        Contractor contractorDuplicate = ContractorDataMapper.findByName(contractor.getName());
        if(contractorDuplicate!=null && contractorDuplicate.getId()!=contractor.getId())
            return new Response(Notification.UPDATE_ERROR);

        contractorDuplicate = ContractorDataMapper.findByPhone(contractor.getPhone());
        if(contractorDuplicate!=null && contractorDuplicate.getId()!=contractor.getId())
            return new Response(Notification.UPDATE_ERROR);

        contractorDuplicate = ContractorDataMapper.findByRequisites(contractor.getRequisites());
        if(contractorDuplicate!=null && contractorDuplicate.getId()!=contractor.getId())
            return new Response(Notification.UPDATE_ERROR);


        int rowsAffected = ContractorDataMapper.update(contractor);

        if(rowsAffected == -1)
            return new Response(Notification.UPDATE_ERROR);
        else
            return new Response(Notification.UPDATE_SUCCESS);
    }

    public static Response addContractor(Contractor contractor)
    {
        Contractor contractorDuplicate = ContractorDataMapper.findByName(contractor.getName());
        if(contractorDuplicate!=null)
            return new Response(Notification.ADD_ERROR);

        contractorDuplicate = ContractorDataMapper.findByPhone(contractor.getPhone());
        if(contractorDuplicate!=null)
            return new Response(Notification.ADD_ERROR);

        contractorDuplicate = ContractorDataMapper.findByRequisites(contractor.getRequisites());
        if(contractorDuplicate!=null)
            return new Response(Notification.ADD_ERROR);


        int rowsAffected = ContractorDataMapper.add(contractor);

        if(rowsAffected == -1)
            return new Response(Notification.ADD_ERROR);
        else
            return new Response(Notification.ADD_SUCCESS);
    }

    public static Response removeContractor(Contractor contractor)
    {
        int rowsAffected = ContractorDataMapper.delete(contractor);

        if(rowsAffected == -1)
            return new Response(Notification.REMOVE_ERROR);
        else
            return new Response(Notification.REMOVE_SUCCESS);
    }
}
