package common.model.dto;

import common.model.BaseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Stats extends BaseEntity
{
    //dto object represents not an entity but a view of few entities
    //in particular case supply/sale statistic view needs: supply/sale entity, supply/sale content entity, contractor entity

    //id from base entity class - id for userID (fk for supply/sale entity to show only logged user supplies/sales)

    //supply/sale entity (without fk)

    private int statsID;
    private Timestamp statsDate;
    private double statsTotal; //calculated column for view representation

    //part of contractor entity
    private String contractorName; //finds contractorID dor DB by specified name, name is just for view representation

    // one stat can contain multiple products & etc.
    private ArrayList<StatsContent> contents;

    public Stats(int id, Timestamp statsDate, double statsTotal, String contractorName, ArrayList<StatsContent> contents)
    {
        super(id);
        this.statsDate = statsDate;
        this.statsTotal = statsTotal;
        this.contractorName = contractorName;
        this.contents = contents;
    }

    public Stats(int id, int statsID, Timestamp statsDate, double statsTotal, String contractorName, ArrayList<StatsContent> contents)
    {
        super(id);
        this.statsID = statsID;
        this.statsDate = statsDate;
        this.statsTotal = statsTotal;
        this.contractorName = contractorName;
        this.contents = contents;
    }

    public int getStatsID()
    {
        return statsID;
    }

    public void setStatsID(int statsID)
    {
        this.statsID = statsID;
    }

    public Timestamp getStatsDate()
    {
        return statsDate;
    }

    public void setStatsDate(Timestamp statsDate)
    {
        this.statsDate = statsDate;
    }

    public double getStatsTotal()
    {
        return statsTotal;
    }

    public void setStatsTotal(double statsTotal)
    {
        this.statsTotal = statsTotal;
    }

    public String getContractorName()
    {
        return contractorName;
    }

    public void setContractorName(String contractorName)
    {
        this.contractorName = contractorName;
    }

    public ArrayList<StatsContent> getContents()
    {
        return contents;
    }

    public void setContents(ArrayList<StatsContent> contents)
    {
        this.contents = contents;
    }
}
