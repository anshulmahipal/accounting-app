package common.model.dto;

import common.model.BaseEntity;
import common.model.entity.Product;

public class StatsContent extends BaseEntity
{
    //this dto object - representation of product entity & some additional attributes
    //equivalent to supplyContent/saleContent db table

    private Product product;  // need only ID for db, additional fields of product for view representation
    private int productAmount;
    private double productSummary; //calculated column for view representation

    public StatsContent(Product product, int productAmount)
    {
        this.product = product;
        this.productAmount = productAmount;
    }

    public StatsContent(Product product, int productAmount, double productSummary)
    {
        this.product = product;
        this.productAmount = productAmount;
        this.productSummary = productSummary;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getProductAmount()
    {
        return productAmount;
    }

    public void setProductAmount(int productAmount)
    {
        this.productAmount = productAmount;
        productSummary = productAmount*product.getCost();
    }

    public double getProductSummary()
    {
        return productSummary;
    }

    public void setProductSummary(double productSummary)
    {
        this.productSummary = productSummary;
    }
}
