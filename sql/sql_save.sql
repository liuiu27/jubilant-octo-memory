/*商户表新增商户标识*/
alter table service_supplier add SUPPLIER_FLAG VARCHAR(25);
/*产品表新增产品子类型*/
alter table service_product add PRODUCT_SUB_TYPE VARCHAR(100);
/*订单表新增订单子类型*/
alter table service_order add ORDER_SUB_TYPE VARCHAR(100);
/*订单表新增商户标识类型*/
alter table service_order add SUPPLIER_FLAG VARCHAR(25);