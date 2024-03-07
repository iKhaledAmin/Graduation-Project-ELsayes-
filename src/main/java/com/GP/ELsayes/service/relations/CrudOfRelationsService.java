package com.GP.ELsayes.service.relations;

public interface CrudOfRelationsService<Entity,Object1 ,Object2,OperationType,ID>{
    public Entity add(Object1 object1 , Object2 object2 , OperationType operationType);
    //public Entity update(Request request, ID id);
}
